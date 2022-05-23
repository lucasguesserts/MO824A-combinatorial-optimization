package tabuSearch;

import localSearch.IntensiveSearch;
import problem.Problem;

public class TabuSearchIntensificationByRestart extends TabuSearchBestImproving {

    private static final Double RESTART_TOLERANCE = 0.20;
    private Integer iterationOfLastRestart = 0;
    private Boolean intensificationByRestartDoneforCurrentBestSolution = Boolean.FALSE;

    public TabuSearchIntensificationByRestart(
        final Problem<Integer, Integer> initialSolution,
        final Double tenureRatio,
        final Integer iterations
    ) {
        super(initialSolution, tenureRatio, iterations);
    }

    @Override
    protected Boolean intensificationCriteria() {
        final var iterationDiff = this.currentIteration - this.iterationOfLastRestart;
        final var tolerance = RESTART_TOLERANCE * this.maximumNumberOfIterations;
        return (iterationDiff > tolerance) && (!this.intensificationByRestartDoneforCurrentBestSolution);
    }

    @Override
    protected Problem<Integer, Integer> makeIntenseSearch() {
        System.out.println(">>>>>>>>>>>>>>>>>>>> Intensification by restart <<<<<<<<<<<<<<<<<<<");
        this.intensificationByRestartDoneforCurrentBestSolution = Boolean.TRUE;
        this.iterationOfLastRestart = this.currentIteration;
        final var localSearch = new IntensiveSearch(this.bestSolution.clone());
        final var elementsManipulated = localSearch.getElementsManipulated();
        for (int i = 0; i < elementsManipulated.size(); ++i) {
            this.TL.poll();
        }
        TL.addAll(elementsManipulated);
        return localSearch.getLocalOptimal();
    }

    @Override
    protected void updateIntensificationCriteria() {
        this.intensificationByRestartDoneforCurrentBestSolution = Boolean.FALSE;
    }
}
