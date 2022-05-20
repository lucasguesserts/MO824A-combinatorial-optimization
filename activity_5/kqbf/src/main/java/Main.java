import java.io.IOException;
import java.util.Random;

import SolutionCost.SolutionCostKQBF;
import inputReader.InputReaderKQBF;


class Main {

    static final Random rng = new Random(0);
    static final Integer ITERATIONS = 1000;

    public static void main(String[] args) throws IOException {
        final String INSTANCE = "../instances/kqbf/kqbf020";
        final var startTime = System.currentTimeMillis();
        final var input = new InputReaderKQBF(INSTANCE);
        input.printMatrix();
        input.printKnapsack();
        final var solutionCost = new SolutionCostKQBF(input);
        int rndIndex = 0;
        for (int i = 0; i < ITERATIONS; ++i) {
            rndIndex = rng.nextInt(solutionCost.getDomainSize());
            solutionCost.add(rndIndex);
            rndIndex = rng.nextInt(solutionCost.getDomainSize());
            solutionCost.remove(rndIndex);
        }
        System.out.println(solutionCost.toString());
        final var endTime = System.currentTimeMillis();
        final var totalTime = (double) (endTime - startTime);
        System.out.println(String.format(
            "Running time = %f seconds",
            totalTime / 1000
        ));
    }
}
