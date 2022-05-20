package objectiveFunction.qbf;

import java.io.IOException;

import inputReader.InputReaderQBF;
import solutions.Solution;

public class QBF_Inverse extends QBF {

    public QBF_Inverse(final InputReaderQBF input, final Solution<Integer> solution) throws IOException {
        super(input, solution);
    }

    public QBF_Inverse(final QBF_Inverse other) {
        super(other);
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

}
