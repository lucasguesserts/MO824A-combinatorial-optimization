package tabuSearch;

import localSearch.FirstImproving;
import problem.Problem;

public class TabuSearchFirstImproving extends TabuSearchBestImproving {

    public TabuSearchFirstImproving(
        final Problem<Integer, Integer> initialSolution,
        final Double tenureRatio,
        final Integer iterations
    ) {
        super(initialSolution, tenureRatio, iterations);
    }

    @Override
    protected void neighborhoodMove() {
        final var neighborhoodSearch = new FirstImproving(
            this.incubentSolution.clone(),
            this.bestSolution.getCost(),
            this.TL
        );
        this.incubentSolution = neighborhoodSearch.getLocalOptimal();
        this.elementsManipulated = neighborhoodSearch.getElementsManipulated();
    }

}
