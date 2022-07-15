package localsearch;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

import com.google.common.graph.Graphs;

import graph.ElementValuePair;
import graph.GraphTools;
import graph.NodeSubstitutionTriple;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;

public class TabuSearch implements LocalSearch {

    private final Problem problem;
    private final GreedyCriteria greedyCriteria;
    private final Integer maximumNumberOfIterationsWithoutImprovement;
    private final Double tenureRatio;
    private final Double capacityExpansionRatio;

    private Solution currentSolution;
    private Solution bestSolution;
    private Integer iterationsWithoutImprovementCount;
    private ArrayDeque<Integer> tabuList;
    private Boolean elementHasBeenAdded;
    private Boolean elementHasBeenSubstituted;
    private Boolean betterSolutionHasBeenFound;

    public TabuSearch(
        final Problem problem,
        final GreedyCriteria greedyCriteria,
        final Double tenureRatio,
        final Double capacityExpansionRatio,
        final Integer maximumNumberOfIterationsWithoutImprovement
    ) {
        this.problem = problem;
        this.greedyCriteria = greedyCriteria;
        this.tenureRatio = tenureRatio;
        this.capacityExpansionRatio = capacityExpansionRatio;
        this.maximumNumberOfIterationsWithoutImprovement = maximumNumberOfIterationsWithoutImprovement;
        return;
    }

    public Solution search(final Solution initialSoution) {
        setInitialState(initialSoution);
        while (!stopCriteriaMet()) {
            setNothingDone();
            do {
                addElement();
            } while (elementHasBeenAdded);
            do {
                substituteElement();
            } while (elementHasBeenSubstituted);
            while (currentSolutionIsInfeasible()) {
                removeElement();
            }
            updateBestSolution();
            updateIterationCount();
        }
        return bestSolution;
    }

    private void setInitialState(final Solution initialSoution) {
        this.initializeFlags();
        this.initializeSolutions(initialSoution);
        this.initializeTabuList();
        this.initializeIterationsCount();
        return;
    }

    private void initializeFlags() {
        this.elementHasBeenAdded = Boolean.TRUE;
        this.elementHasBeenSubstituted = Boolean.TRUE;
        this.betterSolutionHasBeenFound = Boolean.FALSE;
        return;
    }

    private void initializeSolutions(final Solution initialSoution) {
        final var newCapacity = initialSoution.getCapacity().multiply(1 + this.capacityExpansionRatio);
        this.currentSolution = initialSoution.cloneWithExpandedCapacity(newCapacity);
        this.bestSolution = initialSoution.clone();
        return;
    }

    private void initializeTabuList() {
        final Integer numberOfNodes = this.problem.getGraph().nodes().size();
        final Integer tabuListSize = ((Long) Math.round(Math.ceil(numberOfNodes * this.tenureRatio))).intValue();
        final Integer fakeElement = -1;
        this.tabuList = new ArrayDeque<>(tabuListSize);
        for (int i = 0; i < tabuListSize; ++i) {
            this.tabuList.addLast(fakeElement);
        }
        return;
    }

    private void initializeIterationsCount() {
        this.iterationsWithoutImprovementCount = 0;
        return;
    }

    private void setNothingDone() {
        this.elementHasBeenAdded = Boolean.FALSE;
        this.elementHasBeenSubstituted = Boolean.FALSE;
        this.betterSolutionHasBeenFound = Boolean.FALSE;
        return;
    }

    private void addElement() {
        final var elementsOfCurrentSolution = this.currentSolution.getElements();
        final var allSuccessors = new HashSet<>(elementsOfCurrentSolution
            .stream()
            .flatMap(node -> this.problem.getGraph().successors(node).stream())
            .toList()
        );
        final var allPossibleCandidates = new HashSet<>(allSuccessors
            .stream()
            .filter(node -> !elementsOfCurrentSolution.contains(node))
            .filter(node -> !this.tabuList.contains(node))
            .toList()
        );
        final var remainingCapacity = this.currentSolution.getCapacity().subtract(this.currentSolution.getWeight());
        final var elementWeightMap = this.problem.getWeightMap();
        final var candidates = allPossibleCandidates
            .stream()
            .filter(node -> elementsOfCurrentSolution.containsAll(
                this.problem.getGraph().predecessors(node)
            ))
            .filter(node -> !elementWeightMap.get(node).exceeds(remainingCapacity));
        final var bestCandidate = candidates
            .map(node -> new ElementValuePair<Double>(node, greedyCriteria.evaluateCombined(this.problem, this.currentSolution, node)))
            .min((lhs, rhs) -> (int) Math.signum(lhs.value - rhs.value))
            .map(pair -> pair.element);
        if (bestCandidate.isPresent()) {
            this.elementHasBeenAdded = Boolean.TRUE;
            final var elementToAdd = bestCandidate.get();
            final var elementWeight = this.problem.getWeightMap().get(elementToAdd);
            this.addElementToTabuList(elementToAdd);
            this.currentSolution.addElement(elementToAdd, elementWeight);
        }
        else {
            this.elementHasBeenAdded = Boolean.FALSE;
        }
        return;
    }

    private void substituteElement() {
        final var elementsOfCurrentSolution = this.currentSolution.getElements();
        final var allSuccessors = new HashSet<>(elementsOfCurrentSolution
            .stream()
            .flatMap(node -> this.problem.getGraph().successors(node).stream())
            .filter(node -> elementsOfCurrentSolution.containsAll(this.problem.getGraph().predecessors(node)))
            .toList()
        );
        final var allPossibleCandidatesToAdd = new HashSet<>(allSuccessors
            .stream()
            .filter(node -> !elementsOfCurrentSolution.contains(node))
            .filter(node -> !this.tabuList.contains(node))
            .toList()
        );
        final var subGraphInducedBySolution = Graphs.inducedSubgraph(this.problem.getGraph(), elementsOfCurrentSolution);
        final var leafNodes = GraphTools.findLeafNodes(subGraphInducedBySolution); // those are the only ones which can be replaced
        final var weightOfCurrentSolution = this.currentSolution.getWeight();
        final var elementWeightMap = this.problem.getWeightMap();
        final var bestSubstitution = leafNodes
            .stream()
            .flatMap(node -> {
                final var successorsOfLeaf = this.problem.getGraph().successors(node);
                final Set<Integer> candidateList = new HashSet<>(allPossibleCandidatesToAdd);
                candidateList.removeAll(successorsOfLeaf);
                final var candidatePairs = candidateList
                    .stream()
                    .map(candidate -> new ElementValuePair<>(node, candidate));
                return candidatePairs;
            })
            .map(pair -> {
                final var elementToRemove = pair.element;
                final var elementToAdd = pair.value;
                final var weightOfSubstitution = weightOfCurrentSolution
                    .subtract(elementWeightMap.get(elementToRemove))
                    .add(elementWeightMap.get(elementToAdd));
                return new NodeSubstitutionTriple(elementToRemove, elementToAdd, weightOfSubstitution);
            })
            .filter(triple -> !triple.weightOfSubstitution.exceeds(weightOfCurrentSolution))
            .filter(triple -> greedyCriteria.evaluate(triple.weightOfSubstitution) < greedyCriteria.evaluate(weightOfCurrentSolution))
            .min((lhs, rhs) -> (int) Math.signum(greedyCriteria.evaluate(lhs.weightOfSubstitution) - greedyCriteria.evaluate(rhs.weightOfSubstitution)));
        if (bestSubstitution.isPresent()) {
            this.elementHasBeenSubstituted = Boolean.TRUE;
            final var elementToRemove = bestSubstitution.get().toRemove;
            final var weightOfElementToRemove = elementWeightMap.get(elementToRemove);
            final var elementToAdd = bestSubstitution.get().toAdd;
            final var weightOfElementToAdd = elementWeightMap.get(elementToAdd);
            this.addElementToTabuList(elementToRemove);
            this.addElementToTabuList(elementToAdd);
            this.currentSolution.substituteElement(elementToRemove, weightOfElementToRemove, elementToAdd, weightOfElementToAdd);
        }
        else {
            this.elementHasBeenSubstituted = Boolean.FALSE;
        }
        return;
    }

    private void removeElement() {
        final var elementsOfCurrentSolution = this.currentSolution.getElements();
        final var subGraphInducedBySolution = Graphs.inducedSubgraph(this.problem.getGraph(), elementsOfCurrentSolution);
        final var leafNodes = GraphTools.findLeafNodes(subGraphInducedBySolution); // those are the only ones which can be removed
        final var leafNodesNotInTabu = new HashSet<>(leafNodes
            .stream()
            .filter(node -> !this.tabuList.contains(node))
            .toList());
        final Set<Integer> candidates = leafNodesNotInTabu.isEmpty()
            ? leafNodes
            : leafNodesNotInTabu;
        final var bestCandidate = candidates
            .stream()
            .map(node -> new ElementValuePair<Double>(node, greedyCriteria.evaluateRemoval(this.problem, this.currentSolution, node)))
            .min((lhs, rhs) -> (int) Math.signum(lhs.value - rhs.value))
            .map(pair -> pair.element);
        assertBestCandidateIsPresent(bestCandidate.isPresent());
        final var elementToRemove = bestCandidate.get();
        final var elementWeight = this.problem.getWeightMap().get(elementToRemove);
        this.addElementToTabuList(elementToRemove);
        this.currentSolution.removeElement(elementToRemove, elementWeight);
        return;
    }

    private void updateBestSolution() {
        assert !this.currentSolutionIsInfeasible() : String.format("current solution must not be infeasible %s", this.currentSolution.toString());
        final Boolean betterCostFound = this.currentSolution.getCost() > this.bestSolution.getCost();
        final Boolean sameCostFound = this.currentSolution.getCost().equals(this.bestSolution.getCost());
        final Boolean betterWeightFound = this.greedyCriteria.evaluate(this.currentSolution.getWeight()) < this.greedyCriteria.evaluate(this.bestSolution.getWeight());
        if (betterCostFound || (sameCostFound && betterWeightFound)) {
            this.betterSolutionHasBeenFound = Boolean.TRUE;
            this.bestSolution = this.currentSolution.clone();
        }
        return;
    }

    private void addElementToTabuList(final Integer element) {
        this.tabuList.pollFirst();
        this.tabuList.addLast(element);
        return;
    }

    private void updateIterationCount() {
        if (this.betterSolutionHasBeenFound) {
            this.iterationsWithoutImprovementCount = 0;
        } else {
            ++this.iterationsWithoutImprovementCount;
        }
        return;
    }

    private Boolean currentSolutionIsInfeasible() {
        return this.currentSolution.getWeight().exceeds(this.problem.getCapacity());
    }

    private Boolean stopCriteriaMet() {
        // stop after a certain number of iterations
        return this.iterationsWithoutImprovementCount >= this.maximumNumberOfIterationsWithoutImprovement;
    }

    private static void assertBestCandidateIsPresent(final Boolean isPresent) {
        assert isPresent : "Best candidate is not present";
        return;
    }
}
