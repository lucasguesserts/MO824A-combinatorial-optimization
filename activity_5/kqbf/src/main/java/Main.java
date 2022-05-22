import java.io.IOException;

import inputReader.InputReaderKQBF;
import main.AbstractMain;
import problem.ProblemKQBF;
import tabuSearch.TabuSearch;
import tabuSearch.TabuSearchBestImproving;
import tabuSearch.TabuSearchFirstImproving;

class Main extends AbstractMain {

    private Main(final String[] args) throws IOException {
        super(args);
    }

    public static void main(String[] args) throws IOException {
        new Main(args);
    }

    @Override
    protected TabuSearch<Integer, Integer> makeSearchProcedure(
        final String problemInstance,
        final LocalSearchMethod localSearchMethod
    ) throws IOException {
        final var input = new InputReaderKQBF(problemInstance);
        final var emptySolution = new ProblemKQBF(input);
        switch (localSearchMethod) {
            case BEST_IMPROVING:
                return new TabuSearchBestImproving(emptySolution, 20, 1000);
            case FIRST_IMPROVING:
                return new TabuSearchFirstImproving(emptySolution, 20, 1000);
            default:
                throw new RuntimeException("the Local Search Method provided is invalid");
        }
    }

    @Override
    protected String getProblemInstance(final String arg) {
        final String DIR_PATH = "../instances";
        final String PROBLEM_PREFIX = "kqbf";
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
