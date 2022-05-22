package tabuSearch;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import costCoparer.IntegerCostComparer;
import problem.Problem;

public abstract class TabuSearchConstructInitialSolution extends TabuSearch<Integer, Integer> {

    public TabuSearchConstructInitialSolution(
        final Problem<Integer, Integer> initialSolution,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, IntegerCostComparer.getInstance(), tenure, iterations);
    }

    @Override
    protected void constructiveHeuristic() {
        // This is the construction procedure of the GRASP algorithm
        this.incubentSolution.reset();
        final Collection<Integer> candidateList = makeCandidateList();
        final List<Integer> RCL = new ArrayList<Integer>();
        do {
            Integer maxCost = this.costComparer.getMinCost();
            Integer minCost = this.costComparer.getMaxCost();
            this.incubentCost = incubentSolution.getCost();
            for (final Integer candidate : candidateList) {
                final Integer deltaCost = this.incubentSolution.evaluateInsertionCost(candidate);
                if (this.costComparer.isSmaller(deltaCost, minCost))
                    minCost = deltaCost;
                if (this.costComparer.isSmaller(maxCost, deltaCost))
                    maxCost = deltaCost;
            }
            for (final Integer candidate: candidateList) {
                final Integer deltaCost = this.incubentSolution.evaluateInsertionCost(candidate);
                if (deltaCost.equals(minCost)) { // GRASP parameter alpha = 0
                    RCL.add(candidate);
                }
            }
            final int randomIndex = rng.nextInt(RCL.size());
            final Integer inCand = RCL.get(randomIndex);
            candidateList.remove(inCand);
            this.incubentSolution.add(inCand);
            RCL.clear();
        } while (!constructiveStopCriteria());
    }

    private List<Integer> makeCandidateList() {
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
