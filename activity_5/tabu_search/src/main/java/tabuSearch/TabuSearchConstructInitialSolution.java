package tabuSearch;

import java.util.ArrayList;
import java.util.List;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearchConstructInitialSolution extends TabuSearch<Integer, Integer> {


    public TabuSearchConstructInitialSolution(
        final Problem<Integer, Integer> initialSolution,
        final CostComparer<Integer> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, costComparer, tenure, iterations);
    }

    @Override
    protected void constructiveHeuristic() {
        // This is the construction procedure of the GRASP algorithm
        this.incubentSolution.reset();
        this.CL = makeCL();
        this.RCL = new ArrayList<Integer>();
        do {
            Integer maxCost = this.costComparer.getMinCost();
            Integer minCost = this.costComparer.getMaxCost();
            this.incubentCost = incubentSolution.getCost();
            for (final Integer candidate : this.CL) {
                final Integer deltaCost = this.incubentSolution.evaluateInsertionCost(candidate);
                if (this.costComparer.isSmaller(deltaCost, minCost))
                    minCost = deltaCost;
                if (this.costComparer.isSmaller(maxCost, deltaCost))
                    maxCost = deltaCost;
            }
            for (final Integer candidate: this.CL) {
                final Integer deltaCost = this.incubentSolution.evaluateInsertionCost(candidate);
                if (deltaCost.equals(minCost)) { // GRASP parameter alpha = 0
                    this.RCL.add(candidate);
                }
            }
            final int randomIndex = rng.nextInt(RCL.size());
            final Integer inCand = RCL.get(randomIndex);
            this.CL.remove(inCand);
            this.incubentSolution.add(inCand);
            RCL.clear();
        } while (!constructiveStopCriteria());
    }

    private List<Integer> makeCL() {
        final List<Integer> candicateList = new ArrayList<Integer>(incubentSolution.getDomainSize());
        for (Integer candidate = 0; candidate < incubentSolution.getDomainSize(); ++candidate) {
            if (this.incubentSolution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

    private Boolean constructiveStopCriteria() {
        return ! this.costComparer.isSmaller(incubentCost, incubentSolution.getCost());
    }

}
