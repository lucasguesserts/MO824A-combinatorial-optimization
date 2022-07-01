package tabuSearch;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import localSearch.BestImproving;
import problem.Problem;
import solutions.SolutionKnapsack;

public class TabuSearchBestImproving extends TabuSearchConstructInitialSolution {

    protected Collection<Integer> elementsManipulated;

    public TabuSearchBestImproving(
        final Problem<Integer, Integer> initialSolution,
        final Double tenureRatio,
        final Integer iterations
    ) {
        super(initialSolution, tenureRatio, iterations);
    }

    @Override
    protected void neighborhoodMove() {
        final var neighborhoodSearch = new BestImproving(
            this.incubentSolution.clone(),
            this.bestSolution.getCost(),
            this.TL
        );
        this.incubentSolution = neighborhoodSearch.getLocalOptimal();
        this.elementsManipulated = neighborhoodSearch.getElementsManipulated();
    }

    @Override
    protected Queue<Integer> makeTL() {
        final Integer tabuListSize = 2*tenure;
        final Queue<Integer> tabuList = new ArrayDeque<Integer>(tabuListSize);
        for (int i = 0; i < tabuListSize; ++i) {
            tabuList.add(SolutionKnapsack.NULL_CANDIDATE);
        }
        return tabuList;
    }

    @Override
    protected void updateTL() {
        // TL always have 2*tenure elements
        // always include two elements and remove two
        this.TL.addAll(this.elementsManipulated);
        for (int i = 0; i < this.elementsManipulated.size(); ++i){
            this.TL.poll();
        }
    }

    @Override
    protected Boolean diversificationCriteria() {
        return Boolean.FALSE;
    }

    @Override
    protected Problem<Integer, Integer> makeDiversificationConstruction() {
        return this.bestSolution;
    }

    @Override
    protected Boolean intensificationCriteria() {
        return Boolean.FALSE;
    }

    @Override
    protected Problem<Integer, Integer> makeIntenseSearch() {
        return this.bestSolution;
    }

    @Override
    protected void updateIntensificationCriteria() {
    }

}