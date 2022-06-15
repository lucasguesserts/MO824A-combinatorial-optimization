package problems.qbf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import problems.Evaluator;
import solutions.Solution;

public class Qbf implements Evaluator<Integer> {

    public final Integer size;
    public final Double[] variables;
    public Double[][] matrix;

    public Qbf(final String filename) throws IOException {
        size = readInput(filename);
        variables = allocateVariables();
    }

    public void setVariables(final Solution<Integer> sol) {
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
    public Double evaluate(final Solution<Integer> sol) {
        setVariables(sol);
        return sol.cost = evaluateQbf();
    }

    public Double evaluateQbf() {
        Double aux = (double) 0;
        Double sum = (double) 0;
        final Double[] vecAux = new Double[size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aux += variables[j] * matrix[i][j];
            }
            vecAux[i] = aux;
            sum += aux * variables[i];
            aux = (double) 0;
        }
        return sum;
    }

    @Override
    public Double evaluateInsertionCost(final Integer elem, final Solution<Integer> sol) {
        setVariables(sol);
        return evaluateInsertionQbf(elem);
    }

    public Double evaluateInsertionQbf(final int i) {
        if (variables[i] == 1) {
            return 0.0;
        }
        return evaluateContributionQbf(i);
    }

    @Override
    public Double evaluateRemovalCost(final Integer elem, final Solution<Integer> sol) {
        setVariables(sol);
        return evaluateRemovalQbf(elem);
    }

    public Double evaluateRemovalQbf(final int i) {
        if (variables[i] == 0) {
            return 0.0;
        }
        return -evaluateContributionQbf(i);

    }

    @Override
    public Double evaluateExchangeCost(
        final Integer elemIn,
        final Integer elemOut,
        final Solution<Integer> sol
    ) {
        setVariables(sol);
        return evaluateExchangeQbf(elemIn, elemOut);
    }

    public Double evaluateExchangeQbf(final int in, final int out) {
        if (in == out) {
            return 0.0;
        }
        if (variables[in] == 1) {
            return evaluateRemovalQbf(out);
        }
        if (variables[out] == 0) {
            return evaluateInsertionQbf(in);
        }
        Double sum = 0.0;
        sum += evaluateContributionQbf(in);
        sum -= evaluateContributionQbf(out);
        sum -= (matrix[in][out] + matrix[out][in]);
        return sum;
    }

    private Double evaluateContributionQbf(final int i) {
        Double sum = 0.0;
        for (int j = 0; j < size; j++) {
            if (i != j) {
                sum += variables[j] * (matrix[i][j] + matrix[j][i]);
            }
        }
        sum += matrix[i][i];
        return sum;
    }

    protected Integer readInput(final String fileName) throws IOException {
        final Reader fileInst = new BufferedReader(new FileReader(fileName));
        final StreamTokenizer stok = new StreamTokenizer(fileInst);
        stok.nextToken();
        final Integer size = (int) stok.nval;
        matrix = new Double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                stok.nextToken();
                matrix[i][j] = stok.nval;
                if (j > i) {
                    matrix[j][i] = 0.0;
                }
            }
        }
        return size;

    }

    protected Double[] allocateVariables() {
        final Double[] variables = new Double[size];
        return variables;
    }

    public void resetVariables() {
        Arrays.fill(variables, 0.0);
    }

    public void printMatrix() {
        for (int i = 0; i < size; i++) {
            for (int j = i; j < size; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

}
