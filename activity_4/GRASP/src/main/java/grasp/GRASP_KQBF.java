package grasp;

import java.io.IOException;
import java.util.ArrayList;

import problems.KQBF_Inverse;
import solutions.Solution;

public class GRASP_KQBF extends AbstractGRASP<Integer> {

    public GRASP_KQBF(
            final Double alpha,
            final Integer iterations,
            final boolean firstImproving,
            final ConstructionMechanism constructionMechanism,
            final String fileName) throws IOException {
        super(new KQBF_Inverse(fileName), alpha, constructionMechanism, iterations, firstImproving);
    }

    @Override
    public ArrayList<Integer> makeCL() {
        final ArrayList<Integer> candidateList = new ArrayList<Integer>();
        for (int candidate = 0; candidate < ObjFunction.getDomainSize(); ++candidate) {
            if (ObjFunction.isValidCandidate(candidate))
                candidateList.add(candidate);
        }
        return candidateList;
    }

    @Override
    public ArrayList<Integer> makeRCL() {
        ArrayList<Integer> restrictedCandidateList = new ArrayList<Integer>();
        return restrictedCandidateList;
    }

    @Override
    public void updateCL() {
        ArrayList<Integer> candidateList = new ArrayList<Integer>();
        for (int candidate = 0; candidate < ObjFunction.getDomainSize(); candidate++) {
            if (!currentSolution.contains(candidate) && ObjFunction.isValidCandidate(candidate)) {
                candidateList.add(candidate);
            }
        }
        this.CL = candidateList;
    }

    @Override
    public Solution<Integer> createEmptySol() {
        Solution<Integer> sol = new Solution<Integer>();
        sol.cost = 0;
        return sol;
    }

    @Override
    public Solution<Integer> localSearch() {
        if (firstImproving) {
            firstImproving();
        } else {
            bestImproving();
        }
        return null;
    }

    public Integer getKnapsackWeightOfSolution() {
        final KQBF_Inverse ObjFunction = (KQBF_Inverse) this.ObjFunction;
        final Integer knapsackWeight = ObjFunction.evaluateKnapsackWeight(bestSolution);
        return knapsackWeight;
    }

    @Override
    public Solution<Integer> solve() {
        bestSolution = super.solve();
        bestSolution.cost = -bestSolution.cost;
        return bestSolution;
    }

    private void bestImproving() {
        final Integer ZERO = 0;
        Integer minDeltaCost;
        Integer bestCandIn = null;
        Integer bestCandOut = null;
        do {
            minDeltaCost = Integer.MAX_VALUE;
            updateCL();
            // Evaluate insertions
            for (Integer candIn : CL) {
                Integer deltaCost = ObjFunction.evaluateInsertionCost(candIn, currentSolution);
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = candIn;
                    bestCandOut = null;
                }
            }
            // Evaluate removals
            for (Integer candOut : currentSolution) {
                Integer deltaCost = ObjFunction.evaluateRemovalCost(candOut, currentSolution);
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = null;
                    bestCandOut = candOut;
                }
            }
            // Evaluate exchanges
            for (Integer candIn : CL) {
                for (Integer candOut : currentSolution) {
                    Integer deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, currentSolution);
                    if (deltaCost < minDeltaCost) {
                        minDeltaCost = deltaCost;
                        bestCandIn = candIn;
                        bestCandOut = candOut;
                    }
                }
            }
            // Implement the best move, if it reduces the solution cost.
            if (minDeltaCost < ZERO) {
                if (bestCandOut != null) {
                    currentSolution.remove(bestCandOut);
                }
                if (bestCandIn != null) {
                    currentSolution.add(bestCandIn);
                }
                currentSolution.cost = ObjFunction.evaluate(currentSolution);
            }
        } while (minDeltaCost < ZERO);
        return;
    }

    private void firstImproving() {
        Integer firstCandIn = null;
        Integer firstCandOut = null;
        final Integer ZERO = 0;
        do {
            firstCandIn = null;
            firstCandOut = null;
            updateCL();
            // Evaluate insertions
            for (Integer candIn : CL) {
                Integer deltaCost = ObjFunction.evaluateInsertionCost(candIn, currentSolution);
                if (deltaCost < ZERO) {
                    firstCandIn = candIn;
                    break;
                }
            }
            // Implement the first move for insertion, if it reduces the solution cost.
            if (firstCandIn != null) {
                currentSolution.add(firstCandIn);
                currentSolution.cost = ObjFunction.evaluate(currentSolution);
                continue;
            }
            // Evaluate removals
            for (Integer candOut : currentSolution) {
                Integer deltaCost = ObjFunction.evaluateRemovalCost(candOut, currentSolution);
                if (deltaCost < ZERO) {
                    firstCandOut = candOut;
                    break;
                }
            }
            // Implement the first move for removal, if it reduces the solution cost.
            if (firstCandOut != null) {
                currentSolution.remove(firstCandOut);
                currentSolution.cost = ObjFunction.evaluate(currentSolution);
                continue;
            }
            // Evaluate exchanges
            for (Integer candIn : CL) {
                for (Integer candOut : currentSolution) {
                    Integer deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, currentSolution);
                    if (deltaCost < ZERO) {
                        firstCandIn = candIn;
                        firstCandOut = candOut;
                        break;
                    }
                }
                if (firstCandIn != null && firstCandOut != null) {
                    break;
                }
            }
            // Implement the fisrt move for exchange, if it reduces the solution cost.
            if (firstCandIn != null && firstCandOut != null) {
                currentSolution.remove(firstCandOut);
                currentSolution.add(firstCandIn);
                currentSolution.cost = ObjFunction.evaluate(currentSolution);
            }
        } while (firstCandIn != null && firstCandOut != null);
        return;
    }

}
