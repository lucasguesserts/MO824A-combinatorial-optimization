package tabuSearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import costCoparer.CostComparer;
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
        Integer minimumCostVariation = Integer.MAX_VALUE;
        Integer bestCandidateToAdd = null;
        Integer bestCandidateToRemove = null;
        updateCL();
        // Evaluate insertions
        for (final Integer candidateToAdd: this.CL) {
            final Integer costIncrement = this.incubentSolution.evaluateInsertionCost(candidateToAdd);
            if (!this.TL.contains(candidateToAdd) || this.aspirationCriteria(costIncrement)) {
                if (costIncrement < minimumCostVariation) {
                    minimumCostVariation = costIncrement;
                    bestCandidateToAdd = candidateToAdd;
                    bestCandidateToRemove = null;
                }
            }
        }
        // Evaluate removals
        for (final Integer candidateToRemove: this.incubentSolution.getElements()) {
            final Integer costIncrement = this.incubentSolution.evaluateRemovalCost(candidateToRemove);
            if (!this.TL.contains(candidateToRemove) || this.aspirationCriteria(costIncrement)) {
                if (costIncrement < minimumCostVariation) {
                    minimumCostVariation = costIncrement;
                    bestCandidateToAdd = null;
                    bestCandidateToRemove = candidateToRemove;
                }
            }
        }
        // Evaluate exchanges
        for (final Integer candIn: this.CL) {
            for (final Integer candOut: this.incubentSolution.getElements()) {
                final Integer costIncrement = incubentSolution.evaluateExchangeCost(candIn, candOut);
                if ((!this.TL.contains(candIn) && !TL.contains(candOut)) || this.aspirationCriteria(costIncrement)) {
                    if (costIncrement < minimumCostVariation) {
                        minimumCostVariation = costIncrement;
                        bestCandidateToAdd = candIn;
                        bestCandidateToRemove = candOut;
                    }
                }
            }
        }
        // Implement the best non-tabu move
        this.TL.poll();
        if (bestCandidateToRemove != null) {
            this.incubentSolution.remove(bestCandidateToRemove);
            this.CL.add(bestCandidateToRemove);
            this.TL.add(bestCandidateToRemove);
        } else {
            this.TL.add(fake);
        }
        this.TL.poll();
        if (bestCandidateToAdd != null) {
            this.incubentSolution.add(bestCandidateToAdd);
            this.CL.remove(bestCandidateToAdd);
            this.TL.add(bestCandidateToAdd);
        } else {
            this.TL.add(fake);
        }
    }

    private Boolean aspirationCriteria(final Integer costIncrement) {
        final Integer newSolutionCandidateCost = this.incubentSolution.getCost() + costIncrement;
        return newSolutionCandidateCost < this.bestSolution.getCost();
    }

}
