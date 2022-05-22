package tabuSearch;

import problem.Problem;
import solutionConstruction.GRASPConstruction;

abstract class TabuSearchDiversificationByRestart extends TabuSearchIntensificationByRestart {

    private static Double ALPHA = 0.2;

    private static final Double RESTART_TOLERANCE = 0.20;
    private Integer iterationOfLastRestart = 0;

    protected TabuSearchDiversificationByRestart(
        final Problem<Integer, Integer> initialSolution,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, tenure, iterations);
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
