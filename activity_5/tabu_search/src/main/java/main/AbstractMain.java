package main;

import java.io.IOException;

import inputReader.InputReaderKQBF;
import main.Parameters.LocalSearchMethod;
import main.Parameters.Variation;
import problem.Problem;
import problem.ProblemKQBF;
import tabuSearch.TabuSearch;
import tabuSearch.TabuSearchBestImproving;
import tabuSearch.TabuSearchFirstImproving;


public abstract class AbstractMain {

    private long startTime;
    private long endTime;

    private TabuSearch<Integer, Integer> tabuSearch;
    private Problem<Integer, Integer> bestProblemSolution;

    protected Parameters parameters = new Parameters();

    private String instance;
    private LocalSearchMethod localSearch;
    private Double tenureRatio;
    private Variation methodVariation;
    private Integer timeLimitMilliseconds;
    private Integer numberOfIterations;

    public AbstractMain() {}

    public void run() throws IOException {
        for (final var instance: parameters.instanceList)
        for (final var localSearch: parameters.localSearchList)
        for (final var tenureRatio: parameters.tenureRatioList)
        for (final var methodVariation: parameters.methodVariationList)
        for (final var timeLimitMilliseconds: parameters.timeLimitMillisecondsList)
        for (final var numberOfIterations: parameters.numberOfIterationsList) {
            this.instance = instance;
            this.localSearch = localSearch;
            this.tenureRatio = tenureRatio;
            this.methodVariation = methodVariation;
            this.timeLimitMilliseconds = timeLimitMilliseconds;
            this.numberOfIterations = numberOfIterations;
            System.out.println("\n\n>>>>>>>>>>>>> START Case:");
            System.out.println(String.format("instance = %s", this.instance.toString()));
            System.out.println(String.format("localSearch = %s", this.localSearch.toString()));
            System.out.println(String.format("tenureRatio = %s", this.tenureRatio.toString()));
            System.out.println(String.format("methodVariation = %s", this.methodVariation.toString()));
            System.out.println(String.format("timeLimitMilliseconds = %s", this.timeLimitMilliseconds.toString()));
            System.out.println(String.format("numberOfIterations = %s", this.numberOfIterations.toString()));
            start();
            solve();
            finish();
            System.out.println("\n\n<<<<<<<<<<<<<<<<<<< END");
        }
    }

    private void start() throws IOException {
        final var input = new InputReaderKQBF(instance);
        final var emptySolution = new ProblemKQBF(input);
        switch (localSearch) {
            case BEST_IMPROVING:
                tabuSearch = new TabuSearchBestImproving(
                    emptySolution,
                    tenureRatio,
                    numberOfIterations
                );
                break;
            case FIRST_IMPROVING:
                tabuSearch = new TabuSearchFirstImproving(
                    emptySolution,
                    tenureRatio,
                    numberOfIterations
                );
                break;
            default:
                throw new RuntimeException("the Local Search Method provided is invalid");
        }
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
