package neighborhoodMove;

import java.util.ArrayList;
import java.util.Collection;

import problem.Problem;

public class FirstImproving {

    public static final Integer NULL_CANDIDATE = null;

    private Problem<Integer, Integer> incubentSolution;
    private final Integer originalBestSolutionCost;
    private Collection<Integer> elementsManipulated;
    private final Collection<Integer> tabuList;

    private Integer minimumCostVariation = Integer.MAX_VALUE;
    private Integer bestCandidateToAdd = null;
    private Integer bestCandidateToRemove = null;
    private Move move = Move.NO_MOVE;
    private Boolean improvementFound = false;

    public FirstImproving(
        final Problem<Integer, Integer> incubentSolution,
        final Integer originalBestSolutionCost,
        final Collection<Integer> tabuList
    ){
        this.incubentSolution = incubentSolution;
        this.originalBestSolutionCost = originalBestSolutionCost;
        this.tabuList = tabuList;
        this.elementsManipulated = new ArrayList<>();
        this.findLocalOptimal();
    }

    public Problem<Integer, Integer> getLocalOptimal() {
        return this.incubentSolution;
    }

    public Collection<Integer> getElementsManipulated() {
        return this.elementsManipulated;
    }

    private enum Move {
        ADD,
        REMOVE,
        EXCHANGE,
        NO_MOVE
    }

    private void findLocalOptimal() {
        this.searchMove();
        switch (this.move) {
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
        this.incubentSolution.add(this.bestCandidateToAdd);
        this.elementsManipulated.add(this.bestCandidateToAdd);
    }

    private void removeCandidate() {
        this.incubentSolution.remove(this.bestCandidateToRemove);
        this.elementsManipulated.add(this.bestCandidateToRemove);
    }


    private void searchMove () {
        final Collection<Integer> candidateToAddList = this.makeCandidateToAddList();
        final Collection<Integer> candidateToRemoveList = this.makeCandidateToRemoveList();
        this.searchAddMove(candidateToAddList);
        this.searchRemoveMove(candidateToRemoveList);
        this.searchExchangesMove(candidateToAddList, candidateToRemoveList);
    }

    private Collection<Integer> makeCandidateToAddList() {
        final Collection<Integer> candicateList = new ArrayList<Integer>(this.incubentSolution.getDomainSize());
        for (Integer candidate = 0; candidate < this.incubentSolution.getDomainSize(); ++candidate) {
            if (this.incubentSolution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

    private Collection<Integer> makeCandidateToRemoveList() {
        final Collection<Integer> candicateList = this.incubentSolution.getElements();
        return candicateList;
    }

    private void searchAddMove(
        final Collection<Integer> candidateList
    ) {
        if (!this.improvementFound) {
            for (final var candidate: candidateList) {
                final Integer costIncrement = this.incubentSolution.evaluateInsertionCost(candidate);
                if ((!this.tabuList.contains(candidate)) || this.aspirationCriteria(costIncrement)) {
                    this.improvementFound = true;
                    this.update(costIncrement, candidate, NULL_CANDIDATE, Move.ADD);
                    break;
                }
            }
        }
    }

    private void searchRemoveMove(
        final Collection<Integer> candidateList
    ) {
        if (!this.improvementFound) {
            for (final var candidate: candidateList) {
                final Integer costIncrement = this.incubentSolution.evaluateRemovalCost(candidate);
                if ((!this.tabuList.contains(candidate)) || this.aspirationCriteria(costIncrement)) {
                    this.improvementFound = true;
                    this.update(costIncrement, NULL_CANDIDATE, candidate, Move.REMOVE);
                    break;
                }
            }
        }

    }

    private void searchExchangesMove(
        final Collection<Integer> candidateToAddList,
        final Collection<Integer> candidateToRemoveList
    ) {
        if (!this.improvementFound) {
            outerLoop:
            for (final var candidateToAdd: candidateToAddList)
            for (final var candidateToRemove: candidateToRemoveList) {
                final Integer costIncrement = incubentSolution.evaluateExchangeCost(candidateToAdd, candidateToRemove);
                if ((!this.tabuList.contains(candidateToAdd) && !tabuList.contains(candidateToRemove)) || this.aspirationCriteria(costIncrement)) {
                    this.improvementFound = true;
                    this.update(costIncrement, candidateToAdd, candidateToRemove, Move.EXCHANGE);
                    break outerLoop;
                }
            }
        }
    }

    private Boolean aspirationCriteria(final Integer costIncrement) {
        final Integer newSolutionCandidateCost = this.incubentSolution.getCost() + costIncrement;
        return newSolutionCandidateCost < this.originalBestSolutionCost;
    }

    private void update(
        final Integer costIncrement,
        final Integer bestCandidateToAdd,
        final Integer bestCandidateToRemove,
        final Move move
    ) {
        if (this.isBestMove(costIncrement)) {
            this.move = move;
            this.minimumCostVariation = costIncrement;
            switch (move) {
                case ADD:
                    this.bestCandidateToAdd = bestCandidateToAdd;
                    this.bestCandidateToRemove = NULL_CANDIDATE;
                    break;
                case REMOVE:
                    this.bestCandidateToAdd = NULL_CANDIDATE;
                    this.bestCandidateToRemove = bestCandidateToRemove;
                    break;
                case EXCHANGE:
                    this.bestCandidateToAdd = bestCandidateToAdd;
                    this.bestCandidateToRemove = bestCandidateToRemove;
                    break;
                case NO_MOVE:
                    throw new RuntimeException(String.format(
                        "%s class got a %s move",
                        BestImproving.class.getName(),
                        move.toString()
                    ));
            }
        }
    }

    private Boolean isBestMove(final Integer costIncrement) {
        return costIncrement < this.minimumCostVariation;
    }
}
