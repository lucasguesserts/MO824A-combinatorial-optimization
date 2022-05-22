package objectiveFunction.qbf;

import inputReader.InputReaderQBF;
import solutions.Solution;

public class QBF_Inverse extends QBF {

    public QBF_Inverse(final InputReaderQBF input, final Solution<Integer> solution) {
        super(input, solution);
    }

    protected QBF_Inverse(final QBF_Inverse other) {
        super(other);
    }

    @Override
    public QBF_Inverse clone() {
        return new QBF_Inverse(this);
    }

    @Override
    public Integer evaluateInsertionCost(final Integer i) {
        return -super.evaluateInsertionCost(i);
    }

    @Override
    public Integer evaluateRemovalCost(final Integer i) {
        return -super.evaluateRemovalCost(i);
    }

    @Override
    public Integer evaluateExchangeCost(final Integer in, final Integer out) {
        return -super.evaluateExchangeCost(in,out);
    }

    @Override
    public Integer evaluateTwoAdditionOneRemovalCost(
        final Integer firstElementToInsert,
        final Integer secondElementToInsert,
        final Integer elementToRemove
    ) {
        return -super.evaluateTwoAdditionOneRemovalCost(firstElementToInsert, secondElementToInsert, elementToRemove);
    }

    @Override
    public Integer evaluateOneAdditionTwoRemovalCost(
        final Integer elementToInsert,
        final Integer firstElementToRemove,
        final Integer secondElementToRemove
    ) {
        return -super.evaluateOneAdditionTwoRemovalCost(elementToInsert, firstElementToRemove, secondElementToRemove);
    }

}
