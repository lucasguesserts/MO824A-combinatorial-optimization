package objectiveFunction.qbf;

import java.util.Arrays;
import java.util.Collection;

import inputReader.InputReaderQBF;
import objectiveFunction.ObjectiveFunction;
import solutions.Solution;


public class QBF implements ObjectiveFunction<Integer, Integer> {

    public static final Integer INITIAL_COST = 0;

    private final Integer size;
    private final Integer[] variables;
    private final Integer[][] matrix;

    public QBF(final InputReaderQBF input, final Solution<Integer> solution) {
        this.size = input.getSize();
        this.matrix = input.getMatrix();
        this.variables = new Integer[this.size];
        this.resetVariables();
    }

    protected QBF(final QBF other) {
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
    public QBF clone() {
        return new QBF(this);
    }

    @Override
    public Integer getDomainSize() {
        return size;
    }

    @Override
    public Boolean isValidCandidate(final Integer element) {
        return (0 <= element) && (element < this.size);
    }

    @Override
    public Integer evaluateInsertionCost(final Integer element) {
        if (variables[element].equals(1))
            return 0;
        return evaluateContributionQBF(element);
    }

    @Override
    public Integer evaluateRemovalCost(final Integer element) {
        if (variables[element].equals(0))
            return 0;
        final Integer thisEvaluation = this.evaluateQBF();
        final Integer diff = - evaluateContributionQBF(element);
        final Integer thisNewEvaluation = thisEvaluation + diff;
        final var other = this.clone();
        other.removeVariable(element);
        final Integer otherEvaluation = other.evaluateQBF();
        if (!thisNewEvaluation.equals(otherEvaluation)) {
            throw new RuntimeException("removal is wrong!");
        }
        return diff;
    }

    @Override
    public Integer evaluateExchangeCost(final Integer elementToInsert, final Integer elementToRemove) {
        if (elementToInsert.equals(elementToRemove))
            return 0;
        if (variables[elementToInsert].equals(1))
            return evaluateRemovalCost(elementToRemove);
        if (variables[elementToRemove].equals(0))
            return evaluateInsertionCost(elementToInsert);
        Integer sum = 0;
        sum += evaluateContributionQBF(elementToInsert);
        sum -= evaluateContributionQBF(elementToRemove);
        sum -= matrix[elementToInsert][elementToRemove] + matrix[elementToRemove][elementToInsert];
        return sum;
    }

    public Integer evaluate(
        final Collection<Integer> elementsToInsert,
        final Collection<Integer> elementsToRemove
    ) {
        // this is very inneficient, but it does the job
        final var other = this.clone();
        for (final var toInsert: elementsToInsert) {
            other.addVariable(toInsert);
        }
        for (final var toRemove: elementsToRemove) {
            other.removeVariable(toRemove);
        }
        final var diff = this.evaluateQBF() - other.evaluateQBF();
        return diff;
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

    public void addVariable(final Integer element) {
        this.variables[element] = 1;
    }

    public void removeVariable(final Integer element) {
        this.variables[element] = 0;
    }

    public void reset() {
        this.resetVariables();
    }

    private Integer evaluateContributionQBF(final Integer element) {
        Integer sum = 0;
        for (Integer j = 0; j < this.size; ++j) {
            if (!element.equals(j))
                sum += this.variables[j] * (this.matrix[element][j] + this.matrix[j][element]);
        }
        sum += this.matrix[element][element];
        return sum;
    }

    private void resetVariables() {
        Arrays.fill(variables, 0);
    }

    private Integer evaluateQBF() {
        Integer aux = 0;
        Integer sum = 0;
        Integer vecAux[] = new Integer[this.size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                aux += this.variables[j] * this.matrix[i][j];
            }
            vecAux[i] = aux;
            sum += aux * variables[i];
            aux = 0;
        }
        return sum;
    }

}
