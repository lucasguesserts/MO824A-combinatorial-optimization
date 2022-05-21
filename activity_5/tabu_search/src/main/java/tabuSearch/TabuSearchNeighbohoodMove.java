package tabuSearch;

import java.util.ArrayDeque;
import java.util.Queue;

import costCoparer.CostComparer;
import neighborhoodMove.NeighborhoodMove;
import problem.Problem;
import solutions.SolutionKnapsack;

public class TabuSearchNeighbohoodMove extends TabuSearchConstructInitialSolution {

    public TabuSearchNeighbohoodMove(
        final Problem<Integer, Integer> initialSolution,
        final CostComparer<Integer> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, costComparer, tenure, iterations);
    }

    @Override
    protected void neighborhoodMove() {
        final var neighborhoodMove = new NeighborhoodMove(
            this.incubentSolution.getCost(),
            this.bestSolution.getCost()
        );
        updateCL();
        neighborhoodMove.searchMove(
            this.CL,
            this.incubentSolution.getElements(),
            (candidate) -> this.incubentSolution.evaluateInsertionCost(candidate),
            (candidate) -> !this.TL.contains(candidate),
            (candidate) -> this.incubentSolution.evaluateRemovalCost(candidate),
            (candidate) -> !this.TL.contains(candidate),
            (candidateToAdd, candidateToRemove) -> incubentSolution.evaluateExchangeCost(candidateToAdd, candidateToRemove),
            (candidateToAdd, candidateToRemove) -> (!this.TL.contains(candidateToAdd) && !TL.contains(candidateToRemove))
        );
        switch (neighborhoodMove.getMove()) {
            case ADD:
                this.addCandidate(neighborhoodMove);
                break;
            case REMOVE:
                this.removeCandidate(neighborhoodMove);
                break;
            case EXCHANGE:
                this.addCandidate(neighborhoodMove);
                this.removeCandidate(neighborhoodMove);
            case NO_MOVE:
                break;
        }
        this.updateTL(neighborhoodMove);
    }

    private void addCandidate(final NeighborhoodMove neighborhoodMove) {
        this.incubentSolution.add(neighborhoodMove.getBestCandidateToAdd());
        this.CL.remove(neighborhoodMove.getBestCandidateToAdd());
        this.TL.add(neighborhoodMove.getBestCandidateToAdd());
    }

    private void removeCandidate(final NeighborhoodMove neighborhoodMove) {
        this.incubentSolution.remove(neighborhoodMove.getBestCandidateToRemove());
        this.CL.add(neighborhoodMove.getBestCandidateToRemove());
        this.TL.add(neighborhoodMove.getBestCandidateToRemove());
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

    private void updateTL(final NeighborhoodMove neighborhoodMove) {
        // TL always have 2*tenure elements
        // always include two elements and remove two
        final var added = neighborhoodMove.getBestCandidateToAdd();
        final var removed = neighborhoodMove.getBestCandidateToRemove();
        this.TL.add(added == NeighborhoodMove.NULL_CANDIDATE ? SolutionKnapsack.NULL_CANDIDATE : added);
        this.TL.add(removed == NeighborhoodMove.NULL_CANDIDATE ? SolutionKnapsack.NULL_CANDIDATE : removed);
        this.TL.poll();
        this.TL.poll();
    }

}
