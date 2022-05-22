package localSearch;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import problem.Problem;

public class IntensiveSearch implements LocalSearch<Integer, Integer> {

    protected static final Integer NULL_CANDIDATE = null;

    private Boolean localOptimalFound = false;

    protected Problem<Integer, Integer> solution;
    private Collection<Integer> elementsManipulated = new HashSet<>();

    private Integer minimumCostVariation = Integer.MAX_VALUE;
    private Collection<Integer> candidatesToAdd = new HashSet<>();
    private Collection<Integer> candidatesToRemove = new HashSet<>();

    public IntensiveSearch(
        final Problem<Integer, Integer> solution
    ){
        this.solution = solution;
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
    }

    private void searchTwoAdditionsOneRemoval() {
        final Collection<Integer> candidateToRemoveList = this.makeCandidateToRemoveList();
        final Collection<Integer> candidateToAddList = this.makeCandidateToAddList();
        for (final var candidateToRemove: candidateToRemoveList)
        for (final var firstCandidateToAdd: candidateToAddList)
        for (final var secondCandidateToAdd: candidateToAddList) {
            final Collection<Integer> elementsToInsert =
                firstCandidateToAdd.equals(secondCandidateToAdd)
                ? Set.of(firstCandidateToAdd)
                : Set.of(firstCandidateToAdd, secondCandidateToAdd);
            final Collection<Integer> elementsToRemove = Set.of(candidateToRemove);
            final Integer costIncrement = this.solution.evaluate(elementsToInsert, elementsToRemove);
            this.update(
                costIncrement,
                elementsToInsert,
                elementsToRemove
            );
        }
    }

    private void searchOneAdditionTwoRemovals() {
        final Collection<Integer> candidateToRemoveList = this.makeCandidateToRemoveList();
        final Collection<Integer> candidateToAddList = this.makeCandidateToAddList();
        for (final var firstCandidateToRemove: candidateToRemoveList)
        for (final var secondCandidateToRemove: candidateToRemoveList)
        for (final var candidateToAdd: candidateToAddList) {
            final Collection<Integer> elementsToInsert = Set.of(candidateToAdd);
            final Collection<Integer> elementsToRemove =
                firstCandidateToRemove.equals(secondCandidateToRemove)
                ? Set.of(firstCandidateToRemove)
                : Set.of(firstCandidateToRemove, secondCandidateToRemove);
            final Integer costIncrement = this.solution.evaluate(elementsToInsert, elementsToRemove);
            this.update(
                costIncrement,
                elementsToInsert,
                elementsToRemove
            );
        }
    }

    private Collection<Integer> makeCandidateToAddList() {
        final Collection<Integer> candicateList = new HashSet<Integer>(this.solution.getDomainSize());
        for (Integer candidate = 0; candidate < this.solution.getDomainSize(); ++candidate) {
            candicateList.add(candidate);
        }
        candicateList.removeAll(this.solution.getElements());
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
