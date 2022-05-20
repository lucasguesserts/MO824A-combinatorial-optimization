import java.io.IOException;

import inputReader.InputReaderQBF;
import problem.ProblemQBF;
import tabuSearch.TabuSearch;

class Main {
    public static void main(String[] args) throws IOException {
        final String INSTANCE = "../instances/qbf/qbf100";
		final var startTime = System.currentTimeMillis();
        final var input = new InputReaderQBF(INSTANCE);
        final var initialSolution = new ProblemQBF(input);
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
