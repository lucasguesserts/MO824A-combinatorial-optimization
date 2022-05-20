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
        Integer minDeltaCost = Integer.MAX_VALUE;
        Integer bestCandIn = null;
        Integer bestCandOut = null;
        updateCL();
        // Evaluate insertions
        for (final Integer candIn: this.CL) {
            final Integer deltaCost = this.incubentSolution.evaluateInsertionCost(candIn);
            if (!this.TL.contains(candIn) || (this.incubentSolution.getCost() + deltaCost < this.bestSolution.getCost())) {
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = candIn;
                    bestCandOut = null;
                }
            }
        }
        // Evaluate removals
        for (final Integer candOut: this.incubentSolution.getElements()) {
            final Integer deltaCost = this.incubentSolution.evaluateRemovalCost(candOut);
            if (!this.TL.contains(candOut) || (this.incubentSolution.getCost() + deltaCost < this.bestSolution.getCost())) {
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = null;
                    bestCandOut = candOut;
                }
            }
        }
        // Evaluate exchanges
        for (final Integer candIn: this.CL) {
            for (final Integer candOut: this.incubentSolution.getElements()) {
                final Integer deltaCost = incubentSolution.evaluateExchangeCost(candIn, candOut);
                if ((!this.TL.contains(candIn) && !TL.contains(candOut)) || (this.incubentSolution.getCost() + deltaCost < this.bestSolution.getCost())) {
                    if (deltaCost < minDeltaCost) {
                        minDeltaCost = deltaCost;
                        bestCandIn = candIn;
                        bestCandOut = candOut;
                    }
                }
            }
        }
        // Implement the best non-tabu move
        this.TL.poll();
        if (bestCandOut != null) {
            this.incubentSolution.remove(bestCandOut);
            this.CL.add(bestCandOut);
            this.TL.add(bestCandOut);
        } else {
            this.TL.add(fake);
        }
        this.TL.poll();
        if (bestCandIn != null) {
            this.incubentSolution.add(bestCandIn);
            this.CL.remove(bestCandIn);
            this.TL.add(bestCandIn);
        } else {
            this.TL.add(fake);
        }
    }

}
