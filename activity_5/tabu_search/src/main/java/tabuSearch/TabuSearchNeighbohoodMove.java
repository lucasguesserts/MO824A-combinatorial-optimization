package tabuSearch;

import costCoparer.CostComparer;
import neighborhoodMove.NeighborhoodMove;
import problem.Problem;

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

}
