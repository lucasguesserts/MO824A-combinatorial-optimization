package localsearch;

import java.util.HashSet;
import java.util.Set;

import com.google.common.graph.Graphs;

import graph.ElementValuePair;
import graph.GraphTools;
import graph.NodeSubstitutionTriple;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;

public class LocalOptimal implements LocalSearch {

    private final Problem problem;
    private final GreedyCriteria greedyCriteria;

    private Solution currentSolution;
    private Boolean elementHasBeenAdded;
    private Boolean elementHasBeenSubstituted;

    public LocalOptimal(
        final Problem problem,
        final GreedyCriteria greedyCriteria
    ) {
        this.problem = problem;
        this.greedyCriteria = greedyCriteria;
    }

    public Solution search(final Solution initialSoution) {
        this.setInitialState(initialSoution);
        while (!this.stopCriteriaMet()) {
            this.setNothingDone();
            this.addElement();
            if (this.elementHasBeenAdded) continue;
            this.substituteElement();
        }
        return this.currentSolution;
    }

    private void setInitialState(final Solution initialSoution) {
        this.currentSolution = initialSoution;
        this.elementHasBeenAdded = Boolean.TRUE;
        this.elementHasBeenSubstituted = Boolean.TRUE;
    }

    private void setNothingDone() {
        this.elementHasBeenAdded = Boolean.FALSE;
        this.elementHasBeenSubstituted = Boolean.FALSE;
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
            .toList()
        );
        final var remainingCapacity = this.problem.getCapacity().subtract(this.currentSolution.getWeight());
        final var elementWeightMap = this.problem.getWeightMap();
        final var candidates = allPossibleCandidates
            .stream()
            .filter(node -> elementsOfCurrentSolution.containsAll(
                this.problem.getGraph().predecessors(node)
            ))
            .filter(node -> !elementWeightMap.get(node).exceeds(remainingCapacity));
        final var bestCandidate = candidates
            .map(node -> new ElementValuePair<Double>(node, greedyCriteria.evaluate(this.problem, this.currentSolution, node)))
            .min((lhs, rhs) -> (int) Math.signum(lhs.value - rhs.value))
            .map(pair -> pair.element);
        if (bestCandidate.isPresent()) {
            this.elementHasBeenAdded = Boolean.TRUE;
            final var elementToAdd = bestCandidate.get();
            final var elementWeight = this.problem.getWeightMap().get(elementToAdd);
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
            .toList()
        );
        final var allPossibleCandidatesToAdd = new HashSet<>(allSuccessors
            .stream()
            .filter(node -> !elementsOfCurrentSolution.contains(node))
            .toList()
        );
        final var subGraphInducedBySolution = Graphs.inducedSubgraph(this.problem.getGraph(), elementsOfCurrentSolution);
        final var leafNodes = GraphTools.findLeafNodes(subGraphInducedBySolution);
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
            .filter(triple -> triple.weightOfSubstitution.getNorm2() < weightOfCurrentSolution.getNorm2()) // TODO: replace it by a greedy criteria
            .min((lhs, rhs) -> (int) Math.signum(lhs.weightOfSubstitution.getNorm2() - rhs.weightOfSubstitution.getNorm2()));
        if (bestSubstitution.isPresent()) {
            this.elementHasBeenSubstituted = Boolean.TRUE;
            final var elementToRemove = bestSubstitution.get().toRemove;
            final var weightOfElementToRemove = elementWeightMap.get(elementToRemove);
            final var elementToAdd = bestSubstitution.get().toAdd;
            final var weightOfElementToAdd = elementWeightMap.get(elementToAdd);
            this.currentSolution.substituteElement(elementToRemove, weightOfElementToRemove, elementToAdd, weightOfElementToAdd);
        }
        else {
            this.elementHasBeenAdded = Boolean.FALSE;
        }
        return;
    }

    private Boolean stopCriteriaMet() {
        // stop when nothing has been done in the last iteration
        return !this.elementHasBeenAdded && !this.elementHasBeenSubstituted;
    }
}
