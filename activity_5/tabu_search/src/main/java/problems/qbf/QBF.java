package problems.qbf;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.List;

import problems.ObjectiveFunction;
import solutions.Solution;


public class QBF implements ObjectiveFunction<Integer, Integer> {

    public static final Integer INITIAL_COST = 0;

    private final Integer size;
    private final Integer[] variables;
    private final Integer[][] matrix;

    public QBF(final String fileName, final Solution<Integer> solution) throws IOException {
        final var inputReader = new InputReader(fileName);
        this.size = inputReader.size;
        this.matrix = inputReader.matrix;
        this.variables = new Integer[this.size];
        this.setVariables(solution);
    }

    public QBF(final QBF other) {
        this.size = other.size;
        this.matrix = new Integer[this.size][this.size];
        this.variables = new Integer[this.size];
        for (Integer i = 0; i < size; ++i) {
            this.variables[i] = other.variables[i];
            for (Integer j = 0; j < size; ++j) {
                this.matrix[i][j] = other.matrix[i][j];
            }
        }
    }

    @Override
    public Integer getDomainSize() {
        return size;
    }

    public void addVariable(final Integer element) {
        this.variables[element] = 1;
    }

    public void removeVariable(final Integer element) {
        this.variables[element] = 0;
    }

    public void reset() {
        this.resetVariables();
    }

    @Override
    public Integer evaluateInsertionCost(final Integer element) {
        if (variables[element] == 1)
            return 0;
        return evaluateContributionQBF(element);
    }

    @Override
    public Integer evaluateRemovalCost(final Integer element) {
        if (variables[element] == 0)
            return 0;
        return -evaluateContributionQBF(element);
    }

    @Override
    public Integer evaluateExchangeCost(final Integer elementToInsert, final Integer elementToRemove) {
        if (elementToInsert == elementToRemove)
            return 0;
        if (variables[elementToInsert] == 1)
            return evaluateRemovalCost(elementToRemove);
        if (variables[elementToRemove] == 0)
            return evaluateInsertionCost(elementToInsert);
        Integer sum = 0;
        sum += evaluateContributionQBF(elementToInsert);
        sum -= evaluateContributionQBF(elementToRemove);
        sum -= matrix[elementToInsert][elementToRemove] + matrix[elementToRemove][elementToInsert];
        return sum;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("QBF Evaluator:");
        str.append("\n\tmatrix:");
        for (int i = 0; i < size; i++) {
            str.append("\t");
            for (int j = i; j < size; j++) {
                str.append(String.format("%d ", matrix[i][j]));
            }
            str.append("\n");
        }
        return str.toString();
    }

    protected Integer evaluateQBF() {
        Integer sum = 0;
        for (int i = 0; i < this.size; i++) {
            Integer aux = 0;
            Integer vecAux[] = new Integer[this.size];
            for (int j = 0; j < this.size; j++) {
                aux += this.variables[j] * this.matrix[i][j];
            }
            vecAux[i] = aux;
            sum += aux * variables[i];
        }
        return sum;

    }

    protected Integer evaluateContributionQBF(final Integer element) {
        Integer sum = 0;
        for (Integer j = 0; j < size; j++) {
            if (element != j)
                sum += this.variables[j] * (this.matrix[element][j] + this.matrix[j][element]);
        }
        sum += this.matrix[element][element];
        return sum;
    }

    private class InputReader {

        public final Integer size;
        public final Integer[][] matrix;

        InputReader(final String fileName) throws IOException {
            final Reader file = new BufferedReader(new FileReader(fileName));
            final StreamTokenizer stok = new StreamTokenizer(file);
            stok.nextToken();
            this.size = (int) stok.nval;
            this.matrix = new Integer[this.size][this.size];
            for (int i = 0; i < this.size; ++i) {
                Arrays.fill(this.matrix[i], 0);
            }
            for (int i = 0; i < this.size; ++i) {
                for (int j = i; j < this.size; ++j) {
                    stok.nextToken();
                    this.matrix[i][j] = (int) Math.round(stok.nval);
                }
            }
        }
    }

    private Integer setVariables(final Solution<Integer> solution) {
        this.resetVariables();
        final List<Integer> elementsOfSolution = solution.getElements();
        for (final Integer element: elementsOfSolution) {
            this.variables[element] = 1;
        }
        return evaluateQBF();
    }

    private void resetVariables() {
        Arrays.fill(variables, 0);
    }

}
