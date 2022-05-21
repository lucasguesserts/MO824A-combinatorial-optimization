package neighborhoodMove;

public class NeighborhoodMove {
    private Integer minimumCostVariation = Integer.MAX_VALUE;
    private Integer bestCandidateToAdd = null;
    private Integer bestCandidateToRemove = null;
    private Move move = Move.NO_MOVE;

    public enum Move {
        ADD,
        REMOVE,
        EXCHANGE,
        NO_MOVE
    }

    public interface CostEvaluator {
        Integer operate(final Integer candidate);
    }

    public interface CostEvaluatorExchange {
        Integer operate(final Integer candidateToAdd, final Integer candidateToRemove);
    }

    public interface ConditionEvaluator {
        Boolean operate(final Integer candidate, final Integer costIncrement);
    }

    public interface ConditionEvaluatorExchange {
        Boolean operate(final Integer candidateToAdd, final Integer candidateToRemove, final Integer costIncrement);
    }

    public NeighborhoodMove(){}

    public Move getMove() {
        return this.move;
    }

    public Integer getBestCandidateToAdd() {
        return this.bestCandidateToAdd;
    }

    public Integer getBestCandidateToRemove () {
        return this.bestCandidateToRemove;
    }

    public void markAddMove(
        final Integer candidate,
        final CostEvaluator costEvaluator,
        final ConditionEvaluator conditionEvaluator
    ) {
        final Integer costIncrement = costEvaluator.operate(candidate);
        if (conditionEvaluator.operate(candidate, costIncrement) && (costIncrement < this.minimumCostVariation)) {
            this.update(costIncrement, candidate, null, Move.ADD);
        }
    }

    public void markRemoveMove(
        final Integer candidate,
        final CostEvaluator costEvaluator,
        final ConditionEvaluator conditionEvaluator
    ) {
        final Integer costIncrement = costEvaluator.operate(candidate);
        if (conditionEvaluator.operate(candidate, costIncrement) && (costIncrement < this.minimumCostVariation)) {
            this.update(costIncrement, null, candidate, Move.REMOVE);
        }

    }

    public void markExchangesMove(
        final Integer candidateToAdd,
        final Integer candidateToRemove,
        final CostEvaluatorExchange costEvaluator,
        final ConditionEvaluatorExchange conditionEvaluator
    ) {
        final Integer costIncrement = costEvaluator.operate(candidateToAdd, candidateToRemove);
        if (conditionEvaluator.operate(candidateToAdd, candidateToRemove, costIncrement) && (costIncrement < this.minimumCostVariation)) {
            this.update(costIncrement, candidateToAdd, candidateToRemove, Move.EXCHANGE);
        }
    }

    private void update(
        final Integer minimumCostVariation,
        final Integer bestCandidateToAdd,
        final Integer bestCandidateToRemove,
        final Move move
    ) {
        this.move = move;
        this.minimumCostVariation = minimumCostVariation;
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
