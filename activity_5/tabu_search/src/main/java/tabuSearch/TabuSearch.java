package tabuSearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import costCoparer.CostComparer;
import neighborhoodMove.NeighborhoodMove;
import problem.Problem;

public class TabuSearch extends TabuSearchAbstract<Integer, Integer> {

    private final Integer fake = -1;

    public TabuSearch(
        final Problem<Integer, Integer> initialSolution,
        final CostComparer<Integer> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, costComparer, tenure, iterations);
    }

    @Override
    public List<Integer> makeCL() {
        final List<Integer> candicateList = new ArrayList<Integer>(incubentSolution.getDomainSize());
        for (Integer candidate = 0; candidate < incubentSolution.getDomainSize(); ++candidate) {
            if (this.incubentSolution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

    @Override
    public List<Integer> makeRCL() {
        return new ArrayList<Integer>();
    }

    @Override
    public Queue<Integer> makeTL() {
        final Queue<Integer> _TS = new ArrayDeque<Integer>(2*tenure);
        for (int i=0; i<2*tenure; i++) {
            _TS.add(fake);
        }
        return _TS;
    }

    @Override
    public void updateCL() {}

    @Override
    public Problem<Integer, Integer> createEmptySol() {
        final Problem<Integer, Integer> emptySolution = this.incubentSolution.clone();
        emptySolution.reset();
        return emptySolution;
    }

    @Override
    public void neighborhoodMove() {
        final var neighborhoodMove = new NeighborhoodMove();
        updateCL();
        // Evaluate insertions
        for (final Integer candidateToAdd: this.CL) {
            neighborhoodMove.markAddMove(
                candidateToAdd,
                (candidate) -> this.incubentSolution.evaluateInsertionCost(candidate),
                (candidate, costIncrement) -> (!this.TL.contains(candidate) || this.aspirationCriteria(costIncrement))
            );
        }
        // Evaluate removals
        for (final Integer candidateToRemove: this.incubentSolution.getElements()) {
            neighborhoodMove.markRemoveMove(
                candidateToRemove,
                (candidate) -> this.incubentSolution.evaluateRemovalCost(candidate),
                (candidate, costIncrement) -> (!this.TL.contains(candidate) || this.aspirationCriteria(costIncrement))
            );
        }
        // Evaluate exchanges
        for (final Integer candIn: this.CL) {
            for (final Integer candOut: this.incubentSolution.getElements()) {
                neighborhoodMove.markExchangesMove(
                    candIn,
                    candOut,
                    (candidateToAdd, candidateToRemove) -> incubentSolution.evaluateExchangeCost(candidateToAdd, candidateToRemove),
                    (candidateToAdd, candidateToRemove, costIncrement) -> ((!this.TL.contains(candidateToAdd) && !TL.contains(candidateToRemove)) || this.aspirationCriteria(costIncrement))
                );
            }
        }
        // Implement the best non-tabu move
        this.TL.poll();
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
    }

    private void addCandidate(final NeighborhoodMove neighborhoodMove) {
        this.incubentSolution.add(neighborhoodMove.getBestCandidateToAdd());
        this.CL.remove(neighborhoodMove.getBestCandidateToAdd());
        this.TL.add(neighborhoodMove.getBestCandidateToAdd());
        this.TL.poll();
    }

    private void removeCandidate(final NeighborhoodMove neighborhoodMove) {
        this.incubentSolution.remove(neighborhoodMove.getBestCandidateToRemove());
        this.CL.add(neighborhoodMove.getBestCandidateToRemove());
        this.TL.add(neighborhoodMove.getBestCandidateToRemove());
        this.TL.poll();
    }

    private Boolean aspirationCriteria(final Integer costIncrement) {
        final Integer newSolutionCandidateCost = this.incubentSolution.getCost() + costIncrement;
        return newSolutionCandidateCost < this.bestSolution.getCost();
    }

}
