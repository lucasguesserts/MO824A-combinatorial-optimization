import java.io.IOException;

import inputReader.InputReaderQBF;
import main.AbstractMain;
import problem.ProblemQBF;
import tabuSearch.TabuSearch;
import tabuSearch.TabuSearchBestImproving;

class Main extends AbstractMain {

    private Main(final String[] args) throws IOException {
        super(args);
    }

    public static void main(String[] args) throws IOException {
        new Main(args);
    }

    @Override
    protected TabuSearch<Integer, Integer> makeSearchProcedure(final String problemInstance) throws IOException {
        final var input = new InputReaderQBF(problemInstance);
        final var emptySolution = new ProblemQBF(input);
        final var tabuSearch = new TabuSearchBestImproving(emptySolution, 20, 1000);
        return tabuSearch;
    }

    @Override
    protected String getProblemInstance(final String arg) {
        final String DIR_PATH = "../instances";
        final String PROBLEM_PREFIX = "qbf";
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
