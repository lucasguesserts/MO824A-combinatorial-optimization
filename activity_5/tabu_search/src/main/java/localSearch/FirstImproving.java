package localSearch;

import java.util.Collection;

import problem.Problem;

public class FirstImproving extends ImprovingSearchAbstract {

    private Boolean improvementFound = false;

    public FirstImproving(
        final Problem<Integer, Integer> incubentSolution,
        final Integer originalBestSolutionCost,
        final Collection<Integer> tabuList
    ) {
        super(incubentSolution, originalBestSolutionCost, tabuList);
    }

    @Override
    protected void searchAddMove(
        final Collection<Integer> candidateList
    ) {
        if (!this.improvementFound) {
            for (final var candidate: candidateList) {
                final Integer costIncrement = this.incubentSolution.evaluateInsertionCost(candidate);
                if ((!this.tabuList.contains(candidate)) || this.aspirationCriteria(costIncrement)) {
                    this.improvementFound = true;
                    this.update(costIncrement, candidate, NULL_CANDIDATE, Move.ADD);
                    break;
                }
            }
        }
    }

    @Override
    protected void searchRemoveMove(
        final Collection<Integer> candidateList
    ) {
        if (!this.improvementFound) {
            for (final var candidate: candidateList) {
                final Integer costIncrement = this.incubentSolution.evaluateRemovalCost(candidate);
                if ((!this.tabuList.contains(candidate)) || this.aspirationCriteria(costIncrement)) {
                    this.improvementFound = true;
                    this.update(costIncrement, NULL_CANDIDATE, candidate, Move.REMOVE);
                    break;
                }
            }
        }

    }

    @Override
    protected void searchExchangesMove(
        final Collection<Integer> candidateToAddList,
        final Collection<Integer> candidateToRemoveList
    ) {
        if (!this.improvementFound) {
            outerLoop:
            for (final var candidateToAdd: candidateToAddList)
            for (final var candidateToRemove: candidateToRemoveList) {
                final Integer costIncrement = incubentSolution.evaluateExchangeCost(candidateToAdd, candidateToRemove);
                if ((!this.tabuList.contains(candidateToAdd) && !tabuList.contains(candidateToRemove)) || this.aspirationCriteria(costIncrement)) {
                    this.improvementFound = true;
                    this.update(costIncrement, candidateToAdd, candidateToRemove, Move.EXCHANGE);
                    break outerLoop;
                }
            }
        }
    }
}
