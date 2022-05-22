package tabuSearch;

import costCoparer.IntegerCostComparer;
import problem.Problem;
import solutionConstruction.GRASPConstruction;

abstract class TabuSearchConstructInitialSolution extends TabuSearch<Integer, Integer> {

    private static Double ALPHA = 0.0;

    protected TabuSearchConstructInitialSolution(
        final Problem<Integer, Integer> initialSolution,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, IntegerCostComparer.getInstance(), tenure, iterations);
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

}
