package neighborhoodMove;

import java.util.Collection;

public class NeighborhoodMove {
    private Integer minimumCostVariation = Integer.MAX_VALUE;
    private Integer bestCandidateToAdd = null;
    private Integer bestCandidateToRemove = null;
    private final Integer currentSolutionCost;
    private final Integer bestSolutionCost;
    private Move move = Move.NO_MOVE;

    public enum Move {
        ADD,
        REMOVE,
        EXCHANGE,
        NO_MOVE
    }

    public interface CostEvaluator {
        Integer evaluate(final Integer candidate);
    }

    public interface CostEvaluatorExchange {
        Integer evaluate(final Integer candidateToAdd, final Integer candidateToRemove);
    }

    public interface ConditionEvaluator {
        Boolean satisfy(final Integer candidate);
    }

    public interface ConditionEvaluatorExchange {
        Boolean satisfy(final Integer candidateToAdd, final Integer candidateToRemove);
    }

    public NeighborhoodMove(
        final Integer currentSolutionCost,
        final Integer bestSolutionCost
    ){
        this.currentSolutionCost = currentSolutionCost;
        this.bestSolutionCost = bestSolutionCost;
    }

    public Move getMove() {
        return this.move;
    }

    public Integer getBestCandidateToAdd() {
        return this.bestCandidateToAdd;
    }

    public Integer getBestCandidateToRemove () {
        return this.bestCandidateToRemove;
    }

    public void searchAddMove(
        final Collection<Integer> candidateList,
        final CostEvaluator costEvaluator,
        final ConditionEvaluator conditionEvaluator
    ) {
        for (final var candidate: candidateList) {
            final Integer costIncrement = costEvaluator.evaluate(candidate);
            if (conditionEvaluator.satisfy(candidate) || this.aspirationCriteria(costIncrement)) {
                this.update(costIncrement, candidate, null, Move.ADD);
            }
        }
    }

    public void searchRemoveMove(
        final Collection<Integer> candidateList,
        final CostEvaluator costEvaluator,
        final ConditionEvaluator conditionEvaluator
    ) {
        for (final var candidate: candidateList) {
            final Integer costIncrement = costEvaluator.evaluate(candidate);
            if (conditionEvaluator.satisfy(candidate) || this.aspirationCriteria(costIncrement)) {
                this.update(costIncrement, null, candidate, Move.REMOVE);
            }
        }

    }

    public void searchExchangesMove(
        final Collection<Integer> candidateToAddList,
        final Collection<Integer> candidateToRemoveList,
        final CostEvaluatorExchange costEvaluator,
        final ConditionEvaluatorExchange conditionEvaluator
    ) {
        for (final var candidateToAdd: candidateToAddList)
        for (final var candidateToRemove: candidateToRemoveList) {
            final Integer costIncrement = costEvaluator.evaluate(candidateToAdd, candidateToRemove);
            if (conditionEvaluator.satisfy(candidateToAdd, candidateToRemove) || this.aspirationCriteria(costIncrement)) {
                this.update(costIncrement, candidateToAdd, candidateToRemove, Move.EXCHANGE);
            }
        }
    }

    private Boolean isBestMove(final Integer costIncrement) {
        return costIncrement < this.minimumCostVariation;
    }

    private Boolean aspirationCriteria(final Integer costIncrement) {
        final Integer newSolutionCandidateCost = this.currentSolutionCost + costIncrement;
        return newSolutionCandidateCost < this.bestSolutionCost;
    }

    private void update(
        final Integer costIncrement,
        final Integer bestCandidateToAdd,
        final Integer bestCandidateToRemove,
        final Move move
    ) {
        if (this.isBestMove(costIncrement)) {
            this.move = move;
            this.minimumCostVariation = costIncrement;
            switch (move) {
                case ADD:
                    this.bestCandidateToAdd = bestCandidateToAdd;
                    this.bestCandidateToRemove = null;
                    break;
                case REMOVE:
                    this.bestCandidateToAdd = null;
                    this.bestCandidateToRemove = bestCandidateToRemove;
                    break;
                case EXCHANGE:
                    this.bestCandidateToAdd = bestCandidateToAdd;
                    this.bestCandidateToRemove = bestCandidateToRemove;
                    break;
                case NO_MOVE:
                    throw new RuntimeException(String.format(
                        "%s class got a %s move",
                        NeighborhoodMove.class.getName(),
                        move.toString()
                    ));
            }
        }
    }
}
