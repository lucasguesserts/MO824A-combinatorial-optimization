package problems.qbf;

import java.io.IOException;

public class QBF_Inverse extends QBF {

    public QBF_Inverse(final String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public Integer evaluateQBF() {
        return -super.evaluateQBF();
    }

    @Override
    public Integer evaluateInsertionQBF(final Integer element) {
        return -super.evaluateInsertionQBF(element);
    }

    @Override
    public Integer evaluateRemovalQBF(final Integer element) {
        return -super.evaluateRemovalQBF(element);
    }

    @Override
    public Integer evaluateExchangeQBF(final Integer elementToInsert, final Integer elementToRemove) {
        return -super.evaluateExchangeQBF(elementToInsert, elementToRemove);
    }

}
