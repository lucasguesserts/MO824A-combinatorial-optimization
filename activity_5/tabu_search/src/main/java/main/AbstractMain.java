package main;

import java.io.IOException;
import java.security.InvalidParameterException;

import problem.Problem;
import tabuSearch.TabuSearch;


public abstract class AbstractMain {

    private long startTime;
    private long endTime;
    private TabuSearch<Integer, Integer> tabuSearch;
    private Problem<Integer, Integer> bestProblemSolution;

    public AbstractMain(final String[] args) throws IOException {
        start(args);
        solve();
        finish();
    }

    protected abstract String getProblemInstance(final String arg);
    protected abstract TabuSearch<Integer, Integer> makeSearchProcedure(final String problemInstance) throws IOException;


    private void start(final String[] args) throws IOException {
        if (args.length != 1) throw new InvalidParameterException("Give one and only one parameter to this program");
        final String problemInstance = getProblemInstance(args[0]);
        tabuSearch = makeSearchProcedure(problemInstance);
    }

    private void solve() {
        startTime = System.currentTimeMillis();
        tabuSearch.solve();
        bestProblemSolution = tabuSearch.getBestSolution();
        endTime = System.currentTimeMillis();
    }

    private void finish() {
        System.out.println(String.format(
            "Best solution found: \n\t%s",
            bestProblemSolution.toString()
        ));
        final var totalTime = (double) (endTime - startTime);
        System.out.println(String.format(
            "Running time = %f seconds",
            totalTime / 1000
        ));
    }
}
