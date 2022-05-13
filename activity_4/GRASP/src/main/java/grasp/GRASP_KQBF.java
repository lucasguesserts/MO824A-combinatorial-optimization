package grasp;

import java.io.IOException;
import java.util.ArrayList;

import problems.KQBF_Inverse;
import solutions.Solution;

public class GRASP_KQBF extends AbstractGRASP<Integer> {

    public GRASP_KQBF(
        final Double alpha,
        final Integer iterations,
        final String fileName
    ) throws IOException {
        super(new KQBF_Inverse(fileName), alpha, iterations);
    }

    public GRASP_KQBF(
        final Double alpha,
        final Integer iterations,
        final boolean firstImproving,
        final String fileName
    ) throws IOException {
        super(new KQBF_Inverse(fileName), alpha, iterations, firstImproving);
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

    public Solution<Integer> bestImproving() {
        Double minDeltaCost;
        Integer knapsackWeight;
        Integer bestCandIn = null, bestCandOut = null;
        do {
            minDeltaCost = Double.POSITIVE_INFINITY;
            knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(currentSolution);
            updateCL();
            // Evaluate insertions
            for (Integer candIn : CL) {
                double deltaCost = ObjFunction.evaluateInsertionCost(candIn, currentSolution);
                boolean fitsKnapsack = ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, candIn);
                if (deltaCost < minDeltaCost && fitsKnapsack) {
                    minDeltaCost = deltaCost;
                    bestCandIn = candIn;
                    bestCandOut = null;
                }
            }
            // Evaluate removals
            for (Integer candOut : currentSolution) {
                double deltaCost = ObjFunction.evaluateRemovalCost(candOut, currentSolution);
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = null;
                    bestCandOut = candOut;
                }
            }
            // Evaluate exchanges
            for (Integer candIn : CL) {
                for (Integer candOut : currentSolution) {
                    double deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, currentSolution);
                    boolean exchangeFitsKnapsack = ((KQBF_Inverse) ObjFunction).exchangeFitsKnapsack(knapsackWeight, candIn, candOut);
                    if (deltaCost < minDeltaCost && exchangeFitsKnapsack) {
                        minDeltaCost = deltaCost;
                        bestCandIn = candIn;
                        bestCandOut = candOut;
                    }
                }
            }
            // Implement the best move, if it reduces the solution cost.
            if (minDeltaCost < -Double.MIN_VALUE) {
                if (bestCandOut != null) {
                    currentSolution.remove(bestCandOut);
                    CL.add(bestCandOut);
                }
                if (bestCandIn != null) {
                    currentSolution.add(bestCandIn);
                    CL.remove(bestCandIn);
                }
                ObjFunction.evaluate(currentSolution);
            }
        } while (minDeltaCost < -Double.MIN_VALUE);
        return null;
    }

    public Solution<Integer> firstImproving() {

        Integer knapsackWeight;
        Integer firstCandIn = null, firstCandOut = null;
        do {
            knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(currentSolution);
            firstCandIn = null;
            firstCandOut = null;
            updateCL();
            // Evaluate insertions
            for (Integer candIn : CL) {
                double deltaCost = ObjFunction.evaluateInsertionCost(candIn, currentSolution);
                boolean fitsKnapsack = ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, candIn);
                if (deltaCost < -Double.MIN_VALUE && fitsKnapsack) {
                    firstCandIn = candIn;
                    break;
                }
            }
            // Implement the first move for insertion, if it reduces the solution cost.
            if (firstCandIn != null) {
                currentSolution.add(firstCandIn);
                CL.remove(firstCandIn);
                ObjFunction.evaluate(currentSolution);
                continue;
            }
            // Evaluate removals
            for (Integer candOut : currentSolution) {
                double deltaCost = ObjFunction.evaluateRemovalCost(candOut, currentSolution);
                if (deltaCost < -Double.MIN_VALUE) {
                    firstCandOut = candOut;
                    break;
                }
            }
            // Implement the first move for removal, if it reduces the solution cost.
            if (firstCandOut != null) {
                currentSolution.remove(firstCandOut);
                CL.add(firstCandOut);
                ObjFunction.evaluate(currentSolution);
                continue;
            }
            // Evaluate exchanges
            for (Integer candIn : CL) {
                for (Integer candOut : currentSolution) {
                    double deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, currentSolution);
                    boolean exchangeFitsKnapsack = ((KQBF_Inverse) ObjFunction).exchangeFitsKnapsack(knapsackWeight, candIn,
                            candOut);
                    if (deltaCost < -Double.MIN_VALUE && exchangeFitsKnapsack) {
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
                CL.add(firstCandOut);
                currentSolution.add(firstCandIn);
                CL.remove(firstCandIn);
                ObjFunction.evaluate(currentSolution);
            }
        } while (firstCandIn != null && firstCandOut != null);
        return null;
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

    public static void staticSolve(final String fileName) throws IOException {
        staticSolve(
            0.5,
            1000,
            false,
            fileName
        );
    }

    public static void staticSolve(
        final Double alpha,
        final Integer iterations,
        final boolean firstImproving,
        final String fileName
    ) throws IOException {
        long startTime = System.currentTimeMillis();
        GRASP_KQBF grasp = new GRASP_KQBF(alpha, iterations, firstImproving, fileName);
        Solution<Integer> bestSol = grasp.solve();
        KQBF_Inverse ObjFunction = (KQBF_Inverse) grasp.ObjFunction;
        Integer knapsackWeight = ObjFunction.evaluateKnapsackWeight(bestSol);
        System.out.println("");
        System.out.println("Best Found Solution: " + bestSol);
        System.out.println("KnapsackWeight: " + knapsackWeight);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("RunningTime = " + (double) totalTime / (double) 1000 + " seconds");
    }

}
