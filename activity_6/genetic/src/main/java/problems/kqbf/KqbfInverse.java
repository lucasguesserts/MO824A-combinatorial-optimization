package problems.kqbf;

import java.io.IOException;


public class KqbfInverse extends Kqbf {

    public KqbfInverse(final String fileName) throws IOException {
        super(fileName);
    }

    @Override
    public Double evaluateQbf() {
        return -super.evaluateQbf();
    }

    @Override
    public Double evaluateInsertionQbf(final int i) {
        return -super.evaluateInsertionQbf(i);
    }

    @Override
    public Double evaluateRemovalQbf(final int i) {
        return -super.evaluateRemovalQbf(i);
    }

    @Override
    public Double evaluateExchangeQbf(final int in, final int out) {
        return -super.evaluateExchangeQbf(in, out);
    }

}
