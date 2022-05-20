import java.io.IOException;
import java.security.InvalidParameterException;

import costCoparer.IntegerCostComparer;
import inputReader.InputReaderQBF;
import problem.Problem;
import problem.ProblemQBF;
import tabuSearch.TabuSearch;


class Main {

    private static final String PROBLEM_PREFIX = "qbf";
    private static final String DIR_PATH = "../instances";

    private static long startTime;
    private static long endTime;
    private static TabuSearch tabuSearch;
    private static Problem<Integer, Integer> solution;

    public static void main(String[] args) throws IOException {
        start(args);
        solve();
        finish();
    }

    private static void start(final String[] args) throws IOException {
        if (args.length != 1) throw new InvalidParameterException("Give one and only one parameter to this program");
        final String problemInstance = getProblemInstance(args[0]);
        final var input = new InputReaderQBF(problemInstance);
        final var integerCostComparer = IntegerCostComparer.getInstance();
        final var initialSolution = new ProblemQBF(input);
        tabuSearch = new TabuSearch(initialSolution, integerCostComparer, 20, 1000);
    }

    private static void solve() {
        startTime = System.currentTimeMillis();
        solution = tabuSearch.solve();
        endTime = System.currentTimeMillis();
    }

    private static void finish() {
        System.out.println(String.format(
            "Best solution found: \n\t%s",
            solution.toString()
        ));
        final var totalTime = (double) (endTime - startTime);
        System.out.println(String.format(
            "Running time = %f seconds",
            totalTime / 1000
        ));
    }

    private static String getProblemInstance(final String arg) {
        final Integer instanceNumber = Integer.parseInt(arg);
        return String.format(
            "%s/%s/%s%03d",
            DIR_PATH,
            PROBLEM_PREFIX,
            PROBLEM_PREFIX,
            instanceNumber
        );
    }
}
