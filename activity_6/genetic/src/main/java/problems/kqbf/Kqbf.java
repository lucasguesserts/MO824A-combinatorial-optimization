package problems.kqbf;

import java.io.IOException;
import java.util.Arrays;
import problems.Evaluator;
import problems.kqbf.input.InputReaderKqbf;
import solutions.Solution;

public class Kqbf implements Evaluator<Integer> {

    public final Integer size;
    public final Double[] variables;
    public final Double[][] matrix;
    public final Integer knapsackCapacity;
    public final Integer[] weights;

    public Kqbf(final String fileName) throws IOException {
        final var input = new InputReaderKqbf(fileName);
        this.size = input.getSize();
        this.matrix = input.getMatrix();
        this.knapsackCapacity = input.getKnapsackCapacity();
        this.weights = input.getKnapsackWeights();
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
        sol.cost = evaluateQbf();
        sol.weight = evaluateWeight();
        if (sol.weight > this.knapsackCapacity) {
            sol.cost = Double.NEGATIVE_INFINITY;
        }
        return sol.cost;
    }

    @Override
    public Integer evaluateWeight() {
        Integer weight = 0;
        for (int i = 0; i < this.size; ++i) {
            weight += (int) Math.round(this.variables[i]) * this.weights[i];
        }
        return weight;
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
        final var insertionCost = evaluateInsertionQbf(elem);
        final var insertionWeight = evaluateInsertionWeight(elem);
        if (sol.weight + insertionWeight > this.knapsackCapacity) {
            return Double.NEGATIVE_INFINITY;
        }
        return insertionCost;
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
        final var exchangeCost = evaluateExchangeQbf(elemIn, elemOut);
        final var exchangeWeight = evaluateExchangeWeight(elemIn, elemOut);
        if (sol.weight + exchangeWeight > knapsackCapacity) {
            return Double.NEGATIVE_INFINITY;
        }
        return exchangeCost;
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

    public Integer evaluateContributionWeight(final int i) {
        return this.weights[i];
    }

    public Integer evaluateInsertionWeight(final int i) {
        if (variables[i] == 1) {
            return 0;
        }
        return evaluateContributionWeight(i);
    }

    public Integer evaluateRemovalWeight(final int i) {
        if (variables[i] == 0) {
            return 0;
        }
        return -evaluateContributionWeight(i);
    }

    public Integer evaluateExchangeWeight(final int in, final int out) {
        if (in == out) {
            return 0;
        }
        if (variables[in] == 1) {
            return evaluateRemovalWeight(out);
        }
        if (variables[out] == 0) {
            return evaluateInsertionWeight(in);
        }
        Integer sum = 0;
        sum += evaluateContributionWeight(in);
        sum -= evaluateContributionWeight(out);
        return sum;
    }

}
