package tabuSearch;

import java.util.ArrayList;
import java.util.List;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearchConstructInitialSolution extends TabuSearchAbstract<Integer, Integer> {


    public TabuSearchConstructInitialSolution(
        final Problem<Integer, Integer> initialSolution,
        final CostComparer<Integer> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        super(initialSolution, costComparer, tenure, iterations);
    }

    @Override
    protected List<Integer> makeCL() {
        final List<Integer> candicateList = new ArrayList<Integer>(incubentSolution.getDomainSize());
        for (Integer candidate = 0; candidate < incubentSolution.getDomainSize(); ++candidate) {
            if (this.incubentSolution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

    @Override
    protected List<Integer> makeRCL() {
        return new ArrayList<Integer>();
    }

    @Override
    protected void updateCL() {}

    @Override
    protected Problem<Integer, Integer> createEmptySol() {
        final Problem<Integer, Integer> emptySolution = this.incubentSolution.clone();
        emptySolution.reset();
        return emptySolution;
    }

    @Override
    protected void constructiveHeuristic() {
        // This is the construction procedure of the GRASP algorithm
        this.CL = makeCL();
        this.RCL = makeRCL();
        this.incubentSolution = createEmptySol();
        do {
            Integer maxCost = this.costComparer.getMinCost();
            Integer minCost = this.costComparer.getMaxCost();
            this.incubentCost = incubentSolution.getCost();
            updateCL();
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

    private Boolean constructiveStopCriteria() {
        return ! this.costComparer.isSmaller(incubentCost, incubentSolution.getCost());
    }

}
