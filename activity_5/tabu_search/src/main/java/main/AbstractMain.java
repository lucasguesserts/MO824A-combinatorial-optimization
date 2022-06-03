package main;

import java.io.IOException;

import inputReader.InputReaderKQBF;
import main.Parameters.LocalSearchMethod;
import main.Parameters.Variation;
import problem.Problem;
import problem.ProblemKQBF;
import tabuSearch.TabuSearch;
import tabuSearch.TabuSearchBestImproving;
import tabuSearch.TabuSearchDiversificationByRestart;
import tabuSearch.TabuSearchDiversificationByRestartFirst;
import tabuSearch.TabuSearchFirstImproving;
import tabuSearch.TabuSearchIntensificationByRestart;
import tabuSearch.TabuSearchIntensificationByRestartFirst;


public abstract class AbstractMain {

    private long startTime;
    private long endTime;

    private TabuSearch<Integer, Integer> tabuSearch;
    private Double runningTime;
    private Problem<Integer, Integer> bestProblemSolution;

    protected Parameters parameters = new Parameters();

    private String instance;
    private LocalSearchMethod localSearch;
    private Double tenureRatio;
    private Variation methodVariation;
    private Integer numberOfIterations;

    public AbstractMain() {}

    public void run() throws IOException {
        for (final var instance: parameters.instanceList)
        for (final var localSearch: parameters.localSearchList)
        for (final var tenureRatio: parameters.tenureRatioList)
        for (final var methodVariation: parameters.methodVariationList)
        for (final var numberOfIterations: parameters.numberOfIterationsList) {
            this.instance = instance;
            this.localSearch = localSearch;
            this.tenureRatio = tenureRatio;
            this.methodVariation = methodVariation;
            this.numberOfIterations = numberOfIterations;
            System.out.println("\n\n========== START ==========");
            start();
            solve();
            finish();
            System.out.println("========== END ==========\n\n");
        }
    }

    private void start() throws IOException {
        final var input = new InputReaderKQBF(instance);
        final var emptySolution = new ProblemKQBF(input);
        if (localSearch.equals(Parameters.LocalSearchMethod.BEST_IMPROVING) && methodVariation.equals(Parameters.Variation.NONE)) {
            tabuSearch = new TabuSearchBestImproving(emptySolution, tenureRatio, numberOfIterations);
        }
        if (localSearch.equals(Parameters.LocalSearchMethod.BEST_IMPROVING) && methodVariation.equals(Parameters.Variation.INTENSIFICATION_BY_RESTART)) {
            tabuSearch = new TabuSearchIntensificationByRestart(emptySolution, tenureRatio, numberOfIterations);
        }
        if (localSearch.equals(Parameters.LocalSearchMethod.BEST_IMPROVING) && methodVariation.equals(Parameters.Variation.INTENSIFICATION_BY_RESTART_AND_DIVERSIFICATION_BY_RESTART)) {
            tabuSearch = new TabuSearchDiversificationByRestart(emptySolution, tenureRatio, numberOfIterations);
        }
        if (localSearch.equals(Parameters.LocalSearchMethod.FIRST_IMPROVING) && methodVariation.equals(Parameters.Variation.NONE)) {
            tabuSearch = new TabuSearchFirstImproving(emptySolution, tenureRatio, numberOfIterations);
        }
        if (localSearch.equals(Parameters.LocalSearchMethod.FIRST_IMPROVING) && methodVariation.equals(Parameters.Variation.INTENSIFICATION_BY_RESTART)) {
            tabuSearch = new TabuSearchIntensificationByRestartFirst(emptySolution, tenureRatio, numberOfIterations);
        }
        if (localSearch.equals(Parameters.LocalSearchMethod.FIRST_IMPROVING) && methodVariation.equals(Parameters.Variation.INTENSIFICATION_BY_RESTART_AND_DIVERSIFICATION_BY_RESTART)) {
            tabuSearch = new TabuSearchDiversificationByRestartFirst(emptySolution, tenureRatio, numberOfIterations);
        }
    }

    private void solve() {
        startTime = System.currentTimeMillis();
        tabuSearch.solve();
        bestProblemSolution = tabuSearch.getBestSolution();
        endTime = System.currentTimeMillis();
        this.runningTime = (double) (endTime - startTime);
    }

    private void finish() {
        System.out.println(String.format("instance = %s", this.instance.toString()));
        System.out.println(String.format("localSearch = %s", this.localSearch.toString()));
        System.out.println(String.format("tenureRatio = %s", this.tenureRatio.toString()));
        System.out.println(String.format("methodVariation = %s", this.methodVariation.toString()));
        System.out.println(String.format("numberOfIterations = %s", this.numberOfIterations.toString()));
        System.out.println(String.format("runningTime = %f seconds", this.runningTime / 1000));
        System.out.println(String.format("bestProblemSolution = %s", this.bestProblemSolution.toString()));
        System.out.flush();
        System.gc();
    }

}
