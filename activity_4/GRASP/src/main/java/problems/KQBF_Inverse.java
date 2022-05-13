package problems;

import java.io.IOException;


public class KQBF_Inverse extends KQBF {

    public KQBF_Inverse(String filename) throws IOException {
        super(filename);
    }

    @Override
    public Integer evaluateQBF() {
        return -super.evaluateQBF();
    }

    @Override
    public Integer evaluateInsertionQBF(int i) {
        return -super.evaluateInsertionQBF(i);
    }

    @Override
    public Integer evaluateRemovalQBF(int i) {
        return -super.evaluateRemovalQBF(i);
    }

    @Override
    public Integer evaluateExchangeQBF(int in, int out) {
        return -super.evaluateExchangeQBF(in, out);
    }

}
