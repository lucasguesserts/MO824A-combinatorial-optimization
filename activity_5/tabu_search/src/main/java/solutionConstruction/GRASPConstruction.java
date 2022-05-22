package solutionConstruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import problem.Problem;

public class GRASPConstruction {

    private static Random randomGenerator = new Random(0);
    private static Double TOLERANCE = 1e-6;

    private final Problem<Integer, Integer> solution;
    private final Double alpha;

    private Integer previousCost = Integer.MIN_VALUE;
    private Integer nextCost = Integer.MAX_VALUE;

    public GRASPConstruction(
        final Problem<Integer, Integer> initialSolution,
        final Double alpha
    ) {
        this.solution = initialSolution;
        this.alpha = alpha;
    }

    public Problem<Integer, Integer> getSolution() {
        return this.solution;
    }

    public void construct() {
        this.solution.reset();
        final Collection<Integer> candidateList = makeCandidateList();
        final List<Integer> RCL = new ArrayList<Integer>();
        do {
            Integer maxCost = Integer.MIN_VALUE;
            Integer minCost = Integer.MAX_VALUE;
            for (final Integer candidate: candidateList) {
                final Integer deltaCost = this.solution.evaluateInsertionCost(candidate);
                if (deltaCost < minCost)
                    minCost = deltaCost;
                if (maxCost < deltaCost)
                    maxCost = deltaCost;
            }
            for (final Integer candidate: candidateList) {
                final Integer deltaCost = this.solution.evaluateInsertionCost(candidate);
                if (deltaCost - TOLERANCE <= this.alpha*(maxCost - minCost) + minCost) {
                    RCL.add(candidate);
                }
            }
            final int randomIndex = randomGenerator.nextInt(RCL.size());
            final Integer inCand = RCL.get(randomIndex);
            this.previousCost = this.solution.getCost();
            this.solution.add(inCand);
            this.nextCost = this.solution.getCost();
            candidateList.remove(inCand);
            RCL.clear();
        } while (!constructiveStopCriteria());
    }

    private List<Integer> makeCandidateList() {
        final List<Integer> candicateList = new ArrayList<Integer>(this.solution.getDomainSize());
        for (Integer candidate = 0; candidate < this.solution.getDomainSize(); ++candidate) {
            if (this.solution.isValidCandidate(candidate))
                candicateList.add(candidate);
        }
        return candicateList;
    }

    private Boolean constructiveStopCriteria() {
        final var deltaCost = this.nextCost - this.previousCost;
        return deltaCost < 0;
    }

}
