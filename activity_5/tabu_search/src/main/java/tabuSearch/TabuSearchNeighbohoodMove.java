package tabuSearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import costCoparer.CostComparer;
import neighborhoodMove.NeighborhoodMove;
import problem.Problem;
import solutions.SolutionKnapsack;

public class TabuSearchNeighbohoodMove extends TabuSearchConstructInitialSolution {

    private NeighborhoodMove neighborhoodMove;
    private List<Integer> candidateList;

    public TabuSearchNeighbohoodMove(
        final Problem<Integer, Integer> initialSolution,
        final CostComparer<Integer> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, costComparer, tenure, iterations);
        this.candidateList = this.makeCandidateList();
    }

    @Override
    protected void neighborhoodMove() {
        this.neighborhoodMove = new NeighborhoodMove(
            this.incubentSolution.getCost(),
            this.bestSolution.getCost()
        );
        this.neighborhoodMove.searchMove(
            this.candidateList,
            this.incubentSolution.getElements(),
            (candidate) -> this.incubentSolution.evaluateInsertionCost(candidate),
            (candidate) -> !this.TL.contains(candidate),
            (candidate) -> this.incubentSolution.evaluateRemovalCost(candidate),
            (candidate) -> !this.TL.contains(candidate),
            (candidateToAdd, candidateToRemove) -> incubentSolution.evaluateExchangeCost(candidateToAdd, candidateToRemove),
            (candidateToAdd, candidateToRemove) -> (!this.TL.contains(candidateToAdd) && !TL.contains(candidateToRemove))
        );
        switch (this.neighborhoodMove.getMove()) {
            case ADD:
                this.addCandidate();
                break;
            case REMOVE:
                this.removeCandidate();
                break;
            case EXCHANGE:
                this.addCandidate();
                this.removeCandidate();
            case NO_MOVE:
                break;
        }
    }

    private void addCandidate() {
        this.incubentSolution.add(this.neighborhoodMove.getBestCandidateToAdd());
        this.candidateList.remove(this.neighborhoodMove.getBestCandidateToAdd());
        this.TL.add(this.neighborhoodMove.getBestCandidateToAdd());
    }

    private void removeCandidate() {
        this.incubentSolution.remove(this.neighborhoodMove.getBestCandidateToRemove());
        this.candidateList.add(this.neighborhoodMove.getBestCandidateToRemove());
        this.TL.add(this.neighborhoodMove.getBestCandidateToRemove());
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
        final var added = this.neighborhoodMove.getBestCandidateToAdd();
        final var removed = this.neighborhoodMove.getBestCandidateToRemove();
        this.TL.add(added == NeighborhoodMove.NULL_CANDIDATE ? SolutionKnapsack.NULL_CANDIDATE : added);
        this.TL.add(removed == NeighborhoodMove.NULL_CANDIDATE ? SolutionKnapsack.NULL_CANDIDATE : removed);
        this.TL.poll();
        this.TL.poll();
    }

    private List<Integer> makeCandidateList() {
        final List<Integer> candicateList = new ArrayList<Integer>(incubentSolution.getDomainSize());
        for (Integer candidate = 0; candidate < incubentSolution.getDomainSize(); ++candidate) {
            if (this.incubentSolution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

}
