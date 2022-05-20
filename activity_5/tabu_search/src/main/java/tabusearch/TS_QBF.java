package tabusearch;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import SolutionCost.SolutionCost;
import SolutionCost.SolutionCostInteger;
import inputReader.InputReaderQBF;

public class TS_QBF extends AbstractTS<Integer, Integer> {

    private final Integer fake = -1;

    public TS_QBF(
        final Integer tenure,
        final Integer iterations,
        final InputReaderQBF input
    ) {
        super(new SolutionCostInteger(input), tenure, iterations);
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
    public SolutionCost<Integer, Integer> createEmptySol() {
        final SolutionCost<Integer, Integer> emptySolution = this.incubentSolution.clone();
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
