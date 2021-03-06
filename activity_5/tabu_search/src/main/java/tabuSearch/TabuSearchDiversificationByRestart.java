package tabuSearch;

import problem.Problem;
import solutionConstruction.GRASPConstruction;

public class TabuSearchDiversificationByRestart extends TabuSearchIntensificationByRestart {

    private static Double ALPHA = 0.2;

    private static final Double RESTART_TOLERANCE = 0.20;
    private Integer iterationOfLastRestart = 0;

    public TabuSearchDiversificationByRestart(
        final Problem<Integer, Integer> initialSolution,
        final Double tenureRatio,
        final Integer iterations
    ) {
        super(initialSolution, tenureRatio, iterations);
    }

    public TabuSearchDiversificationByRestart(
        final Problem<Integer, Integer> initialSolution,
        final Double tenureRatio,
        final Integer iterations,
        final Integer targetValue
    ) {
        super(initialSolution, tenureRatio, iterations, targetValue);
    }

    @Override
    protected Boolean diversificationCriteria() {
        final var iterationDiff = this.currentIteration - this.iterationOfLastRestart;
        final var tolerance = RESTART_TOLERANCE * this.maximumNumberOfIterations;
        return (iterationDiff > tolerance);
    }

    @Override
    protected Problem<Integer, Integer> makeDiversificationConstruction() {
        System.out.println(">>>>>>>>>>>>>>>>>>>> Diversification by restart <<<<<<<<<<<<<<<<<<<");
        this.iterationOfLastRestart = this.currentIteration;
        final var solutionConstructor = new GRASPConstruction(
            this.incubentSolution.clone(),
            ALPHA
        );
        solutionConstructor.construct();
        this.incubentSolution = solutionConstructor.getSolution();
        return this.bestSolution;
    }

}
