import java.io.IOException;

import inputReader.InputReaderKQBF;

class Main {
    public static void main(String[] args) throws IOException {
        final String INSTANCE = "../instances/kqbf/kqbf020";
        final var startTime = System.currentTimeMillis();
        final var input = new InputReaderKQBF(INSTANCE);
        input.printMatrix();
        input.printKnapsack();
        final var endTime = System.currentTimeMillis();
        final var totalTime = (double) (endTime - startTime);
        System.out.println(String.format(
            "Running time = %f seconds",
            totalTime / 1000
        ));
    }
}
