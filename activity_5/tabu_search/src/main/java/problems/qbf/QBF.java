package problems.qbf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import problems.Evaluator;
import solutions.Solution;

/**
 * A quadractic binary function (QBF) is a function that can be expressed as the
 * sum of quadractic terms: f(x) = \sum{i,j}{a_{ij}*x_i*x_j}. In matricial form
 * a QBF can be expressed as f(x) = x'.A.x
 * The problem of minimizing a QBF is NP-hard [1], even when no constraints
 * are considered.
 *
 * [1] Kochenberger, et al. The unconstrained binary quadratic programming
 * problem: a survey. J Comb Optim (2014) 28:58–81. DOI
 * 10.1007/s10878-014-9734-0.
 *
 * @author ccavellucci, fusberti
 *
 */
public class QBF implements Evaluator<Integer, Double> {

	private Integer size;

	public final Double[] variables;

	private Double[][] A;

	public QBF(final String filename) throws IOException {
		readInput(filename);
		this.variables = allocateVariables();
	}

	public void setVariables(final Solution<Integer, Double> sol) {
		resetVariables();
		if (!sol.isEmpty()) {
			for (final Integer elem: sol) {
				this.variables[elem] = 1.0;
			}
		}
	}

	@Override
	public Integer getDomainSize() {
		return size;
	}

	@Override
	public Double evaluate(final Solution<Integer, Double> sol) {
		setVariables(sol);
		return evaluateQBF();
	}

	public Double evaluateQBF() {
		Double aux = (double) 0;
        Double sum = (double) 0;
		Double vecAux[] = new Double[size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				aux += variables[j] * A[i][j];
			}
			vecAux[i] = aux;
			sum += aux * variables[i];
			aux = (double) 0;
		}
		return sum;

	}

	@Override
	public Double evaluateInsertionCost(final Integer elem, final Solution<Integer, Double> sol) {
		setVariables(sol);
		return evaluateInsertionQBF(elem);
	}

	public Double evaluateInsertionQBF(int i) {
		if (variables[i] == 1)
			return 0.0;
		return evaluateContributionQBF(i);
	}

	@Override
	public Double evaluateRemovalCost(Integer elem, Solution<Integer, Double> sol) {
		setVariables(sol);
		return evaluateRemovalQBF(elem);
	}

	public Double evaluateRemovalQBF(int i) {
		if (variables[i] == 0)
			return 0.0;
		return -evaluateContributionQBF(i);

	}

	@Override
	public Double evaluateExchangeCost(Integer elemIn, Integer elemOut, Solution<Integer, Double> sol) {
		setVariables(sol);
		return evaluateExchangeQBF(elemIn, elemOut);
	}

	public Double evaluateExchangeQBF(int in, int out) {
		if (in == out)
			return 0.0;
		if (variables[in] == 1)
			return evaluateRemovalQBF(out);
		if (variables[out] == 0)
			return evaluateInsertionQBF(in);
        Double sum = 0.0;
		sum += evaluateContributionQBF(in);
		sum -= evaluateContributionQBF(out);
		sum -= (A[in][out] + A[out][in]);
		return sum;
	}

	private Double evaluateContributionQBF(int i) {
		Double sum = 0.0;
		for (int j = 0; j < size; j++) {
			if (i != j)
				sum += variables[j] * (A[i][j] + A[j][i]);
		}
		sum += A[i][i];
		return sum;
	}

	protected void readInput(String filename) throws IOException {
		Reader fileInst = new BufferedReader(new FileReader(filename));
		StreamTokenizer stok = new StreamTokenizer(fileInst);
		stok.nextToken();
		this.size = (int) stok.nval;
		this.A = new Double[this.size][this.size];
		for (int i = 0; i < this.size; i++) {
			for (int j = i; j < this.size; j++) {
				stok.nextToken();
				this.A[i][j] = stok.nval;
				if (j>i)
                this.A[j][i] = 0.0;
			}
		}
	}

	protected Double[] allocateVariables() {
		Double[] _variables = new Double[size];
		return _variables;
	}

	public void resetVariables() {
		Arrays.fill(variables, 0.0);
	}

	public void printMatrix() {
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
	}

}
