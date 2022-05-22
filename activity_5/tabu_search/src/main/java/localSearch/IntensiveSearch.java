package localSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import problem.Problem;

public class IntensiveSearch implements LocalSearch<Integer, Integer> {

    protected static final Integer NULL_CANDIDATE = null;

    private Boolean localOptimalFound = false;

    protected Problem<Integer, Integer> solution;
    private Collection<Integer> elementsManipulated;

    private Integer minimumCostVariation = Integer.MAX_VALUE;
    private Collection<Integer> candidatesToAdd = new ArrayList<>();
    private Collection<Integer> candidatesToRemove = new ArrayList<>();

    public IntensiveSearch(
        final Problem<Integer, Integer> solution
    ){
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
        final Collection<Integer> candidateToAddList = this.makeCandidateToAddList();
        final Collection<Integer> candidateToRemoveList = this.makeCandidateToRemoveList();
        this.searchTwoAdditionsOneRemoval(candidateToAddList, candidateToRemoveList);
        this.searchOneAdditionTwoRemovals(candidateToAddList, candidateToRemoveList);
    }

    private void searchTwoAdditionsOneRemoval(
        final Collection<Integer> candidateToAddList,
        final Collection<Integer> candidateToRemoveList
    ) {
        for (final var firstCandidateToAdd: candidateToAddList)
        for (final var secondCandidateToAdd: candidateToAddList)
        for (final var candidateToRemove: candidateToRemoveList) {
            final Integer costIncrement = solution.evaluateTwoAdditionOneRemovalCost(firstCandidateToAdd, secondCandidateToAdd, candidateToRemove);
            this.update(
                costIncrement,
                Arrays.asList(firstCandidateToAdd, secondCandidateToAdd),
                Arrays.asList(candidateToRemove)
            );
        }
    }

    private void searchOneAdditionTwoRemovals(
        final Collection<Integer> candidateToAddList,
        final Collection<Integer> candidateToRemoveList
    ) {
        for (final var candidateToAdd: candidateToAddList)
        for (final var firstCandidateToRemove: candidateToRemoveList)
        for (final var secondCandidateToRemove: candidateToRemoveList) {
            final Integer costIncrement = solution.evaluateOneAdditionTwoRemovalCost(candidateToAdd, firstCandidateToRemove, secondCandidateToRemove);
            this.update(
                costIncrement,
                Arrays.asList(candidateToAdd),
                Arrays.asList(firstCandidateToRemove, secondCandidateToRemove)
            );
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
