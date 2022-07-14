package problems.kqbf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import problems.Evaluator;
import solutions.Solution;

public class KQBF implements Evaluator {

    public final Integer size;


    public Integer W;

    public final Integer[] variables;

    public Integer[] weights;

    public Integer[][] A;

    public KQBF(String filename) throws IOException {
        size = readInput(filename);
        variables = allocateVariables();
    }

    public void setVariables(Solution sol) {

        resetVariables();
        if (!sol.isEmpty()) {
            for (Integer elem : sol) {
                variables[elem] = 1;
            }
        }

    }

    @Override
    public Integer getDomainSize() {
        return size;
    }

    @Override
    public Integer evaluate(Solution sol) {

        setVariables(sol);
        return sol.cost = evaluateQBF();

    }

    public Integer evaluateQBF() {

        Integer aux = 0, sum = 0;
        Integer vecAux[] = new Integer[size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aux += variables[j] * A[i][j];
            }
            vecAux[i] = aux;
            sum += aux * variables[i];
            aux = 0;
        }

        return sum;

    }

    @Override
    public Integer evaluateInsertionCost(Integer elem, Solution sol) {

        setVariables(sol);
        return evaluateInsertionQBF(elem);

    }

    public Integer evaluateInsertionQBF(int i) {

        if (variables[i] == 1)
            return 0;

        return evaluateContributionQBF(i);
    }

    @Override
    public Integer evaluateRemovalCost(Integer elem, Solution sol) {

        setVariables(sol);
        return evaluateRemovalQBF(elem);

    }

    public Integer evaluateRemovalQBF(int i) {

        if (variables[i] == 0)
            return 0;

        return -evaluateContributionQBF(i);

    }

    @Override
    public Integer evaluateExchangeCost(Integer elemIn, Integer elemOut, Solution sol) {

        setVariables(sol);
        return evaluateExchangeQBF(elemIn, elemOut);

    }

    public boolean exchangeFitsKnapsack(Integer w, Integer elemIn, Integer elemOut) {

        Integer deltaWeight = weights[elemIn] - weights[elemOut];

        return w + deltaWeight > W ? false : true;

    }

    public Integer evaluateExchangeQBF(int in, int out) {

        Integer sum = 0;

        if (in == out)
            return 0;
        if (variables[in] == 1)
            return evaluateRemovalQBF(out);
        if (variables[out] == 0)
            return evaluateInsertionQBF(in);

        sum += evaluateContributionQBF(in);
        sum -= evaluateContributionQBF(out);
        sum -= (A[in][out] + A[out][in]);

        return sum;
    }

    public Integer evaluateKnapsackWeight(Solution sol) {

        setVariables(sol);

        Integer sum = 0;

        for (int i = 0; i < size; i++) {
            sum += variables[i] * weights[i];
        }

        return sum;
    }

    public boolean fitsKnapsack(Integer w, int i) {

        return w + weights[i] > W ? false : true;

    }

    private Integer evaluateContributionQBF(int i) {

        Integer sum = 0;

        for (int j = 0; j < size; j++) {
            if (i != j)
                sum += variables[j] * (A[i][j] + A[j][i]);
        }
        sum += A[i][i];

        return sum;
    }

    protected Integer readInput(String filename) throws IOException {

        Reader fileInst = new BufferedReader(new FileReader(filename));
        StreamTokenizer stok = new StreamTokenizer(fileInst);

        stok.nextToken();
        Integer _size = (int) stok.nval;

        stok.nextToken();
        W = (int) stok.nval;

        weights = new Integer[_size];
        for (int i = 0; i < _size; i++) {
            stok.nextToken();
            weights[i] = (int) stok.nval;
        }

        A = new Integer[_size][_size];
        for (int i = 0; i < _size; i++) {
            for (int j = i; j < _size; j++) {
                stok.nextToken();
                A[i][j] = (int) stok.nval;
                if (j > i)
                    A[j][i] = 0;
            }
        }

        return _size;

    }

    protected Integer[] allocateVariables() {
        Integer[] _variables = new Integer[size];
        return _variables;
    }

    public void resetVariables() {
        Arrays.fill(variables, 0);
    }

    public void printMatrix() {

        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                System.out.print(A[i][j] + " ");
            }
            System.out.println();
        }

    }

    public static void main(String[] args) throws IOException {

        KQBF qbf = new KQBF("instances/qbf/qbf040");
        qbf.printMatrix();
        Integer maxVal = Integer.MIN_VALUE;

        for (int i = 0; i < 10000000; i++) {
            for (int j = 0; j < qbf.size; j++) {
                if (Math.random() < 0.5)
                    qbf.variables[j] = 0;
                else
                    qbf.variables[j] = 1;
            }
            Integer eval = qbf.evaluateQBF();
            if (maxVal < eval)
                maxVal = eval;
        }
        System.out.println("maxVal = " + maxVal);

        for (int j = 0; j < qbf.size; j++) {
            qbf.variables[j] = 0;
        }
        System.out.println("x = " + Arrays.toString(qbf.variables));
        System.out.println("f(x) = " + qbf.evaluateQBF());

        for (int j = 0; j < qbf.size; j++) {
            qbf.variables[j] = 1;
        }
        System.out.println("x = " + Arrays.toString(qbf.variables));
        System.out.println("f(x) = " + qbf.evaluateQBF());

    }

}
