package problems;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;

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
public class KQBF implements Evaluator<Integer> {

    public final Integer size;

    public Double W;

    public final Integer[] variables;

    public Double[] weights;

    public Double[][] A;

    public KQBF(String filename) throws IOException {
        size = readInput(filename);
        variables = allocateVariables();
    }


    @Override
    public Integer getDomainSize() {
        return size;
    }

    @Override
    public Double evaluate(Solution<Integer> sol) {
        setVariables(sol);
        return sol.cost = evaluateQBF();
    }

    @Override
    public Double evaluateInsertionCost(Integer elem, Solution<Integer> sol) {
        setVariables(sol);
        return evaluateInsertionQBF(elem);
    }

    @Override
    public Double evaluateRemovalCost(Integer elem, Solution<Integer> sol) {
        setVariables(sol);
        return evaluateRemovalQBF(elem);
    }

    @Override
    public Double evaluateExchangeCost(Integer elemIn, Integer elemOut, Solution<Integer> sol) {
        setVariables(sol);
        return evaluateExchangeQBF(elemIn, elemOut);
    }

    protected Double evaluateQBF() {
        Double aux = (double) 0, sum = (double) 0;
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

    protected Double evaluateInsertionQBF(int i) {
        if (variables[i] == 1) return 0.0;
        return evaluateContributionQBF(i);
    }

    protected Double evaluateRemovalQBF(int i) {
        if (variables[i] == 0) return 0.0;
        else return -evaluateContributionQBF(i);
    }

    public boolean exchangeFitsKnapsack(Double w, Integer elemIn, Integer elemOut) {
        Double deltaWeight = weights[elemIn] - weights[elemOut];
        return w + deltaWeight > W ? false : true;
    }

    protected Double evaluateExchangeQBF(int in, int out) {
        Double sum = 0.0;
        if (in == out)
            return 0.0;
        if (variables[in] == 1)
            return evaluateRemovalQBF(out);
        if (variables[out] == 0)
            return evaluateInsertionQBF(in);
        sum += evaluateContributionQBF(in);
        sum -= evaluateContributionQBF(out);
        sum -= (A[in][out] + A[out][in]);
        return sum;
    }

    public Double evaluateKnapsackWeight(Solution<Integer> sol) {
        setVariables(sol);
        Double sum = 0.0;
        for (int i = 0; i < size; i++) {
            sum += variables[i] * weights[i];
        }
        return sum;
    }

    public boolean fitsKnapsack(Double w, int i) {
        return w + weights[i] > W
            ? false
            : true;
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

    private Integer readInput(String filename) throws IOException {
        Reader fileInst = new BufferedReader(new FileReader(filename));
        StreamTokenizer stok = new StreamTokenizer(fileInst);
        stok.nextToken();
        Integer _size = (int) stok.nval;
        stok.nextToken();
        W = stok.nval;
        weights = new Double[_size];
        for (int i = 0; i < _size; i++) {
            stok.nextToken();
            weights[i] = stok.nval;
        }
        A = new Double[_size][_size];
        for (int i = 0; i < _size; i++) {
            for (int j = i; j < _size; j++) {
                stok.nextToken();
                A[i][j] = stok.nval;
                if (j > i)
                    A[j][i] = 0.0;
            }
        }
        return _size;
    }

    private Integer[] allocateVariables() {
        Integer[] _variables = new Integer[size];
        return _variables;
    }

    private void resetVariables() {
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

    private void setVariables(Solution<Integer> sol) {
        resetVariables();
        if (!sol.isEmpty()) {
            for (Integer elem : sol) {
                variables[elem] = 1;
            }
        }
    }

    public static void staticSolve(String instance) throws IOException {
        // naive approach: try solutions randomly
        KQBF kqbf = new KQBF(instance);
        Double maxVal = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < 10000000; i++) {
            for (int j = 0; j < kqbf.size; j++) {
                kqbf.variables[j] = getBinaryRandomValue();
            }
            Double eval = kqbf.evaluateQBF();
            if (maxVal < eval)
                maxVal = eval;
        }
        System.out.println("maxVal = " + maxVal);
        // evaluates the zero array.
        for (int j = 0; j < kqbf.size; j++) {
            kqbf.variables[j] = 0;
        }
        System.out.println("x = " + Arrays.toString(kqbf.variables));
        System.out.println("f(x) = " + kqbf.evaluateQBF());
        // evaluates the all-ones array.
        for (int j = 0; j < kqbf.size; j++) {
            kqbf.variables[j] = 1;
        }
        System.out.println("x = " + Arrays.toString(kqbf.variables));
        System.out.println("f(x) = " + kqbf.evaluateQBF());
    }

    private static Integer getBinaryRandomValue() {
        return Math.random() < 0.5
            ? 0
            : 1;
    }

}
