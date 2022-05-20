import java.io.IOException;
import java.util.Random;

import inputReader.InputReaderKQBF;
import problem.ProblemKQBF;
import tabuSearch.TabuSearch;


class Main {

    static final Random rng = new Random(0);
    static final Integer ITERATIONS = 1000;

    public static void main(String[] args) throws IOException {
        final String INSTANCE = "../instances/kqbf/kqbf020";
        final var startTime = System.currentTimeMillis();
        final var input = new InputReaderKQBF(INSTANCE);
        final var initialSolution = new ProblemKQBF(input);
        final var tabuSearch = new TabuSearch(initialSolution, 20, 1000);
        final var bestSolution = tabuSearch.solve();
        System.out.println(String.format(
            "Best solution found: \n\t%s",
            bestSolution.toString()
        ));
		final var endTime = System.currentTimeMillis();
		final var totalTime = (double) (endTime - startTime);
		System.out.println(String.format(
            "Running time = %f seconds",
            totalTime / 1000
        ));
    }
}
