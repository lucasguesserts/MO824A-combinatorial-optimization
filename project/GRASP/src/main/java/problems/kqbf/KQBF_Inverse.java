package problems.kqbf;

import java.io.IOException;

public class KQBF_Inverse extends KQBF {

    public KQBF_Inverse(String filename) throws IOException {
        super(filename);
    }

    @Override
    public Double evaluateQBF() {
        return -super.evaluateQBF();
    }

    @Override
    public Double evaluateInsertionQBF(int i) {
        return -super.evaluateInsertionQBF(i);
    }

    @Override
    public Double evaluateRemovalQBF(int i) {
        return -super.evaluateRemovalQBF(i);
    }

    @Override
    public Double evaluateExchangeQBF(int in, int out) {
        return -super.evaluateExchangeQBF(in, out);
    }

}
