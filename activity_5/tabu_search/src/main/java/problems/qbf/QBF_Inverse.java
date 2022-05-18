package problems.qbf;

import java.io.IOException;

import solutions.Solution;

public class QBF_Inverse extends QBF {

    public QBF_Inverse(final String fileName, final Solution<Integer> solution) throws IOException {
        super(fileName, solution);
    }

    public QBF_Inverse(final QBF_Inverse other) {
        super(other);
    }

    @Override
    protected Integer evaluateContributionQBF(final Integer element) {
        return -super.evaluateContributionQBF(element);
    }

    @Override
    protected Integer evaluateQBF() {
        return -super.evaluateQBF();
    }

}
