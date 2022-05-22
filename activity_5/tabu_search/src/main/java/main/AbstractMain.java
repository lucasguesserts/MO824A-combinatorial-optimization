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

    protected enum LocalSearchMethod {
        BEST_IMPROVING,
        FIRST_IMPROVING
    }

    public AbstractMain(final String[] args) throws IOException {
        start(args);
        solve();
        finish();
    }

    protected abstract String getProblemInstance(final String arg);
    protected abstract TabuSearch<Integer, Integer> makeSearchProcedure(
        final String problemInstance,
        final LocalSearchMethod localSearchMethod
    ) throws IOException;


    private void start(final String[] args) throws IOException {
        if (args.length != 2) throw new InvalidParameterException("Give exactly two parameters to this program");
        final var problemInstance = getProblemInstance(args[0]);
        final var localSearchMethod = getLocaSearchMethod(args[1]);
        tabuSearch = makeSearchProcedure(problemInstance, localSearchMethod);
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

    private LocalSearchMethod getLocaSearchMethod(final String arg) {
        if (arg.equals(LocalSearchMethod.BEST_IMPROVING.toString().toLowerCase())) {
            return LocalSearchMethod.BEST_IMPROVING;
        } else if (arg.equals(LocalSearchMethod.FIRST_IMPROVING.toString().toLowerCase())) {
            return LocalSearchMethod.FIRST_IMPROVING;
        } else throw new RuntimeException("the Local Search Method provided is invalid");
    }
}
