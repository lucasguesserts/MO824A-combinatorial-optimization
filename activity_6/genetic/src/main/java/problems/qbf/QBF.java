package problems.qbf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import problems.Evaluator;
import solutions.Solution;

public class QBF implements Evaluator<Integer> {

    public final Integer size;
    public final Double[] variables;
    public Double[][] A;

    public QBF(String filename) throws IOException {
        size = readInput(filename);
        variables = allocateVariables();
    }

    public void setVariables(Solution<Integer> sol) {
        resetVariables();
        if (!sol.isEmpty()) {
            for (Integer elem : sol) {
                variables[elem] = 1.0;
            }
        }
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

    public Double evaluateQBF() {
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

    @Override
    public Double evaluateInsertionCost(Integer elem, Solution<Integer> sol) {
        setVariables(sol);
        return evaluateInsertionQBF(elem);
    }

    public Double evaluateInsertionQBF(int i) {
        if (variables[i] == 1)
            return 0.0;
        return evaluateContributionQBF(i);
    }

    @Override
    public Double evaluateRemovalCost(Integer elem, Solution<Integer> sol) {
        setVariables(sol);
        return evaluateRemovalQBF(elem);
    }

    public Double evaluateRemovalQBF(int i) {
        if (variables[i] == 0)
            return 0.0;
        return -evaluateContributionQBF(i);

    }

    @Override
    public Double evaluateExchangeCost(Integer elemIn, Integer elemOut, Solution<Integer> sol) {
        setVariables(sol);
        return evaluateExchangeQBF(elemIn, elemOut);
    }

    public Double evaluateExchangeQBF(int in, int out) {
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

    private Double evaluateContributionQBF(int i) {
        Double sum = 0.0;
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
    public static void main(String[] args) throws IOException {
        QBF qbf = new QBF("instances/qbf/qbf040");
        qbf.printMatrix();
        Double maxVal = Double.NEGATIVE_INFINITY;
        // evaluates randomly generated values for the domain, saving the best
        // one.
        for (int i = 0; i < 10000000; i++) {
            for (int j = 0; j < qbf.size; j++) {
                if (Math.random() < 0.5)
                    qbf.variables[j] = 0.0;
                else
                    qbf.variables[j] = 1.0;
            }
            // System.out.println("x = " + Arrays.toString(qbf.variables));
            Double eval = qbf.evaluateQBF();
            // System.out.println("f(x) = " + eval);
            if (maxVal < eval)
                maxVal = eval;
        }
        System.out.println("maxVal = " + maxVal);
        // evaluates the zero array.
        for (int j = 0; j < qbf.size; j++) {
            qbf.variables[j] = 0.0;
        }
        System.out.println("x = " + Arrays.toString(qbf.variables));
        System.out.println("f(x) = " + qbf.evaluateQBF());
        // evaluates the all-ones array.
        for (int j = 0; j < qbf.size; j++) {
            qbf.variables[j] = 1.0;
        }
        System.out.println("x = " + Arrays.toString(qbf.variables));
        System.out.println("f(x) = " + qbf.evaluateQBF());
    }

}
