package tabuSearch;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Queue;

import neighborhoodMove.FirstImproving;
import problem.Problem;
import solutions.SolutionKnapsack;

public class TabuSearchFirstImproving extends TabuSearchConstructInitialSolution {

    private Collection<Integer> elementsManipulated;

    public TabuSearchFirstImproving(
        final Problem<Integer, Integer> initialSolution,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, tenure, iterations);
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

}
