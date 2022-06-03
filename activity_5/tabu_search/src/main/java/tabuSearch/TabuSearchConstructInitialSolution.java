package tabuSearch;

import costCoparer.IntegerCostComparer;
import problem.Problem;
import solutionConstruction.GRASPConstruction;

abstract class TabuSearchConstructInitialSolution extends TabuSearch<Integer, Integer> {

    private static Double ALPHA = 0.0;

    public TabuSearchConstructInitialSolution(
        final Problem<Integer, Integer> initialSolution,
        final Double tenureRatio,
        final Integer iterations
    ) {
        super(initialSolution, IntegerCostComparer.getInstance(), tenureRatio, iterations);
    }

    @Override
    protected void constructiveHeuristic() {
        final var solutionConstructor = new GRASPConstruction(
            this.incubentSolution.clone(),
            ALPHA
        );
        solutionConstructor.construct();
        this.incubentSolution = solutionConstructor.getSolution();
    }

    @Override
    protected Boolean intensificationCriteria() {
        return Boolean.FALSE;
    }

    @Override
    protected Problem<Integer, Integer> makeIntenseSearch() {
        return this.bestSolution.clone();
    }

    @Override
    protected void updateIntensificationCriteria() {}

    @Override
    protected Boolean diversificationCriteria() {
        return Boolean.FALSE;
    }

    @Override
    protected Problem<Integer, Integer> makeDiversificationConstruction() {
        return this.incubentSolution.clone();
    }

}
