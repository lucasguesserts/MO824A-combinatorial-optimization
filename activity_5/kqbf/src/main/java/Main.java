import java.io.IOException;

import costCoparer.IntegerCostComparer;
import inputReader.InputReaderKQBF;
import main.AbstractMain;
import problem.ProblemKQBF;
import tabuSearch.TabuSearch;
import tabuSearch.TabuSearchNeighbohoodMove;

class Main extends AbstractMain {

    private Main(final String[] args) throws IOException {
        super(args);
    }

    public static void main(String[] args) throws IOException {
        new Main(args);
    }

    @Override
    protected TabuSearch<Integer, Integer> makeSearchProcedure(final String problemInstance) throws IOException {
        final var input = new InputReaderKQBF(problemInstance);
        final var integerCostComparer = IntegerCostComparer.getInstance();
        final var emptySolution = new ProblemKQBF(input);
        final var tabuSearch = new TabuSearchNeighbohoodMove(emptySolution, integerCostComparer, 20, 1000);
        return tabuSearch;
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
