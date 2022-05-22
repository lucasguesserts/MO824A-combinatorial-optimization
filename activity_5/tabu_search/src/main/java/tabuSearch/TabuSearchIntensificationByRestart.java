package tabuSearch;

import localSearch.IntensiveSearch;
import problem.Problem;

abstract class TabuSearchIntensificationByRestart extends TabuSearchConstructInitialSolution {

    private static final Double RESTART_TOLERANCE = 0.20;
    private Integer iterationOfLastRestart = 0;
    private Boolean intensificationByRestartDoneforCurrentBestSolution = Boolean.FALSE;

    protected TabuSearchIntensificationByRestart(
        final Problem<Integer, Integer> initialSolution,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, tenure, iterations);
    }

    @Override
    protected Boolean intensificationCriteria() {
        final var iterationDiff = this.currentIteration - this.iterationOfLastRestart;
        final var tolerance = RESTART_TOLERANCE * this.maximumNumberOfIterations;
        return (iterationDiff > tolerance) && (!this.intensificationByRestartDoneforCurrentBestSolution);
    }

    @Override
    protected Problem<Integer, Integer> makeIntenseSearch(final Problem<Integer, Integer> solution) {
        System.out.println(">>>>>>>>>>>>>>>>>>>> Intensification by restart <<<<<<<<<<<<<<<<<<<");
        this.intensificationByRestartDoneforCurrentBestSolution = Boolean.TRUE;
        this.iterationOfLastRestart = this.currentIteration;
        final var localSearch = new IntensiveSearch(this.bestSolution.clone());
        return localSearch.getLocalOptimal();
    }

    @Override
    protected void updateIntensificationCriteria() {
        this.intensificationByRestartDoneforCurrentBestSolution = Boolean.FALSE;
    }
}
