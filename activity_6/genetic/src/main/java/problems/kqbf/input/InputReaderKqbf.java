package problems.kqbf.input;

import java.io.IOException;

public class InputReaderKqbf extends InputReaderQbf {

    private Integer knapsackCapacity;
    private Integer[] knapsackWeights;

    public InputReaderKqbf(final String fileName) throws IOException {
        super(fileName);
    }

    public Integer getKnapsackCapacity() {
        return this.knapsackCapacity;
    }

    public Integer[] getKnapsackWeights() {
        return this.knapsackWeights;
    }

    public void printKnapsack() {
        System.out.println(String.format(
            "knapsack: {capacity: %s, weights: %s}",
            this.knapsackCapacity,
            this.knapsackWeightsToString()
        ));
    }

    private String knapsackWeightsToString() {
        final var str = new StringBuilder();
        str.append("[");
        for (int i = 0; i < this.knapsackWeights.length - 1; ++i) {
            final var value = this.knapsackWeights[i];
            str.append(String.format("%2d, ", value));
        }
        // last element
        final int i = this.knapsackWeights.length - 1;
        final var value = this.knapsackWeights[i];
        str.append(String.format("%2d", value));
        str.append("]");
        return new String(str);
    }

    @Override
    protected void construct() throws IOException {
        this.size = this.readInteger();
        this.knapsackCapacity = this.readInteger();
        this.knapsackWeights = this.readLine();
        this.matrix = this.allocateMatrix();
        this.fillMatrixWithZeros();
        this.readMatrix();
    }

    protected Integer[] readLine() throws IOException {
        final var line = new Integer[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.stok.nextToken();
            line[i] = ((int) Math.round(stok.nval));
        }
        return line;
    }

}
