package localSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import problem.Problem;

public class IntensiveSearch implements LocalSearch<Integer, Integer> {

    protected static final Integer NULL_CANDIDATE = null;

    private Boolean localOptimalFound = false;

    protected final Problem<Integer, Integer> originalSolution;
    protected Problem<Integer, Integer> solution;
    private Collection<Integer> elementsManipulated;

    private Integer minimumCostVariation = Integer.MAX_VALUE;
    private Collection<Integer> candidatesToAdd = new ArrayList<>();
    private Collection<Integer> candidatesToRemove = new ArrayList<>();

    public IntensiveSearch(
        final Problem<Integer, Integer> solution
    ){
        originalSolution = solution.clone();
        this.solution = solution;
        this.elementsManipulated = new ArrayList<>();
    }

    public Problem<Integer, Integer> getLocalOptimal() {
        if (!this.localOptimalFound) {
            this.findLocalOptimal();
            this.localOptimalFound = Boolean.TRUE;
        }
        return this.solution;
    }

    public Collection<Integer> getElementsManipulated() {
        return this.elementsManipulated;
    }

    private void findLocalOptimal() {
        this.searchMove();
        this.addCandidates();
        this.removeCandidates();
    }

    private void addCandidates() {
        for (final var candidateToAdd: this.candidatesToAdd) {
            this.solution.add(candidateToAdd);
        }
        this.elementsManipulated.addAll(this.candidatesToAdd);
    }

    private void removeCandidates() {
        for (final var candidateToRemove: this.candidatesToRemove) {
            this.solution.remove(candidateToRemove);
        }
        this.elementsManipulated.addAll(this.candidatesToRemove);
    }

    private void searchMove () {
        this.searchTwoAdditionsOneRemoval();
        this.searchOneAdditionTwoRemovals();
        this.solution = this.originalSolution.clone();
    }

    private void searchTwoAdditionsOneRemoval() {
        final Collection<Integer> candidateToRemoveList = this.makeCandidateToRemoveList();
        for (final var candidateToRemove: candidateToRemoveList) {
            this.solution = this.originalSolution.clone();
            this.solution.remove(candidateToRemove);
            final Collection<Integer> candidateToAddList = this.makeCandidateToAddList();
            for (final var firstCandidateToAdd: candidateToAddList)
            for (final var secondCandidateToAdd: candidateToAddList) {
                final var localSolution = this.solution.clone();
                localSolution.add(firstCandidateToAdd);
                localSolution.add(secondCandidateToAdd);
                final Integer costIncrement = localSolution.getCost() - this.originalSolution.getCost();
                this.update(
                    costIncrement,
                    Arrays.asList(firstCandidateToAdd, secondCandidateToAdd),
                    Arrays.asList(candidateToRemove)
                );
            }
        }
    }

    private void searchOneAdditionTwoRemovals() {
        final Collection<Integer> candidateToRemoveList = this.makeCandidateToRemoveList();
        for (final var firstCandidateToRemove: candidateToRemoveList)
        for (final var secondCandidateToRemove: candidateToRemoveList) {
            this.solution = this.originalSolution.clone();
            this.solution.remove(firstCandidateToRemove);
            this.solution.remove(secondCandidateToRemove);
            final Collection<Integer> candidateToAddList = this.makeCandidateToAddList();
            for (final var candidateToAdd: candidateToAddList) {
                final var localSolution = this.solution.clone();
                localSolution.add(candidateToAdd);
                final Integer costIncrement = localSolution.getCost() - this.originalSolution.getCost();
                this.update(
                    costIncrement,
                    Arrays.asList(candidateToAdd),
                    Arrays.asList(firstCandidateToRemove, secondCandidateToRemove)
                );
            }
        }
    }

    private Collection<Integer> makeCandidateToAddList() {
        final Collection<Integer> candicateList = new ArrayList<Integer>(this.solution.getDomainSize());
        for (Integer candidate = 0; candidate < this.solution.getDomainSize(); ++candidate) {
            if (this.solution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

    private Collection<Integer> makeCandidateToRemoveList() {
        final Collection<Integer> candicateList = this.solution.getElements();
        return candicateList;
    }

    protected void update(
        final Integer costIncrement,
        final Collection<Integer> candidatesToAdd,
        final Collection<Integer> candidatesToRemove
    ) {
        if (this.isBestMove(costIncrement)) {
            this.minimumCostVariation = costIncrement;
            this.candidatesToAdd = candidatesToAdd;
            this.candidatesToRemove = candidatesToRemove;
        }
    }

    private Boolean isBestMove(final Integer costIncrement) {
        return costIncrement < this.minimumCostVariation;
    }
}
