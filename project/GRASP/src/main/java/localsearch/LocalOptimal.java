package localsearch;

import java.util.HashSet;

import graph.ElementValuePair;
import metaheuristic.greedy_criteria.GreedyCriteria;
import problem.Problem;
import solution.Solution;

public class LocalOptimal implements LocalSearch {

    private final Problem problem;
    private final GreedyCriteria greedyCriteria;

    private Solution currentSolution;
    private Boolean elementHasBeenAdded;
    private Boolean elementHasBeenReplaced;

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
            this.replaceElement();
        }
        return this.currentSolution;
    }

    private void setInitialState(final Solution initialSoution) {
        this.currentSolution = initialSoution;
        this.elementHasBeenAdded = Boolean.TRUE;
        this.elementHasBeenReplaced = Boolean.TRUE;
    }

    private void setNothingDone() {
        this.elementHasBeenAdded = Boolean.FALSE;
        this.elementHasBeenReplaced = Boolean.FALSE;
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

    private void replaceElement() {}

    private Boolean stopCriteriaMet() {
        // stop when nothing has been done in the last iteration
        return !this.elementHasBeenAdded && !this.elementHasBeenReplaced;
    }
}
