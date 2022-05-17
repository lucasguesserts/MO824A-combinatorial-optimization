package problems.qbf;

import java.io.IOException;

public class QBF_Inverse extends QBF {

	public QBF_Inverse(String filename) throws IOException {
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
		return -super.evaluateExchangeQBF(in,out);
	}

}
