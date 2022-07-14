package problems.kqbf.solvers;

import java.io.IOException;
import java.util.ArrayList;

import metaheuristics.GRASP;
import problems.kqbf.KQBF_Inverse;
import solutions.Solution;

public class GRASP_KQBF extends GRASP {
    public GRASP_KQBF(Double alpha, Integer iterations, String filename) throws IOException {
        super(new KQBF_Inverse(filename), alpha, iterations);
    }


    public GRASP_KQBF(Double alpha, Integer iterations, boolean firstImproving, String filename) throws IOException {
        super(new KQBF_Inverse(filename), alpha, iterations, firstImproving);
    }

    @Override
    public ArrayList<Integer> makeCL() {

        ArrayList<Integer> _CL = new ArrayList<Integer>();
        for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
            Integer cand = i;
            _CL.add(cand);
        }

        return _CL;

    }

    @Override
    public ArrayList<Integer> makeRCL() {

        ArrayList<Integer> _RCL = new ArrayList<Integer>();

        return _RCL;

    }

    @Override
    public void updateCL() {

        Integer knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(sol);

        ArrayList<Integer> _CL = new ArrayList<Integer>();
        for (int i = 0; i < ObjFunction.getDomainSize(); i++) {
            if (!sol.contains(i) && ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, i)) {
                Integer cand = i;
                _CL.add(cand);
            }

        }

        CL = _CL;

    }

    @Override
    public Solution createEmptySol() {
        Solution sol = new Solution();
        sol.cost = 0;
        return sol;
    }

    public Solution bestImproving() {

        Integer minDeltaCost;
        Integer knapsackWeight;
        Integer bestCandIn = null, bestCandOut = null;

        do {
            minDeltaCost = Integer.MAX_VALUE;
            knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(sol);
            updateCL();

            for (Integer candIn : CL) {
                Integer deltaCost = ObjFunction.evaluateInsertionCost(candIn, sol);
                boolean fitsKnapsack = ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, candIn);
                if (deltaCost < minDeltaCost && fitsKnapsack) {
                    minDeltaCost = deltaCost;
                    bestCandIn = candIn;
                    bestCandOut = null;
                }
            }
            for (Integer candOut : sol) {
                Integer deltaCost = ObjFunction.evaluateRemovalCost(candOut, sol);
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = null;
                    bestCandOut = candOut;
                }
            }
            for (Integer candIn : CL) {
                for (Integer candOut : sol) {
                    Integer deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, sol);
                    boolean exchangeFitsKnapsack = ((KQBF_Inverse) ObjFunction).exchangeFitsKnapsack(knapsackWeight, candIn,
                            candOut);
                    if (deltaCost < minDeltaCost && exchangeFitsKnapsack) {
                        minDeltaCost = deltaCost;
                        bestCandIn = candIn;
                        bestCandOut = candOut;
                    }
                }
            }
            if (minDeltaCost < 0) {
                if (bestCandOut != null) {
                    sol.remove(bestCandOut);
                    CL.add(bestCandOut);
                }
                if (bestCandIn != null) {
                    sol.add(bestCandIn);
                    CL.remove(bestCandIn);
                }
                ObjFunction.evaluate(sol);
            }
        } while (minDeltaCost < 0);

        return null;
    }

    public Solution firstImproving() {

        Integer knapsackWeight;
        Integer firstCandIn = null, firstCandOut = null;

        do {
            knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(sol);
            firstCandIn = null;
            firstCandOut = null;
            updateCL();

            for (Integer candIn : CL) {
                Integer deltaCost = ObjFunction.evaluateInsertionCost(candIn, sol);
                boolean fitsKnapsack = ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, candIn);
                if (deltaCost < 0 && fitsKnapsack) {
                    firstCandIn = candIn;
                    break;
                }
            }
            if (firstCandIn != null) {
                sol.add(firstCandIn);
                CL.remove(firstCandIn);
                ObjFunction.evaluate(sol);
                continue;
            }

            for (Integer candOut : sol) {
                Integer deltaCost = ObjFunction.evaluateRemovalCost(candOut, sol);
                if (deltaCost < 0) {
                    firstCandOut = candOut;
                    break;
                }
            }
            if (firstCandOut != null) {
                sol.remove(firstCandOut);
                CL.add(firstCandOut);
                ObjFunction.evaluate(sol);
                continue;
            }

            for (Integer candIn : CL) {
                for (Integer candOut : sol) {
                    Integer deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, sol);
                    boolean exchangeFitsKnapsack = ((KQBF_Inverse) ObjFunction).exchangeFitsKnapsack(knapsackWeight, candIn,
                            candOut);
                    if (deltaCost < 0 && exchangeFitsKnapsack) {
                        firstCandIn = candIn;
                        firstCandOut = candOut;
                        break;
                    }
                }
                if (firstCandIn != null && firstCandOut != null) {
                    break;
                }
            }
            if (firstCandIn != null && firstCandOut != null) {
                sol.remove(firstCandOut);
                CL.add(firstCandOut);
                sol.add(firstCandIn);
                CL.remove(firstCandIn);
                ObjFunction.evaluate(sol);
            }
        } while (firstCandIn != null && firstCandOut != null);

        return null;
    }

    @Override
    public Solution localSearch() {
        bestImproving();
        return null;
    }

    public static void main(String[] args) throws IOException {

        long startTime = System.currentTimeMillis();
        GRASP_KQBF grasp = new GRASP_KQBF(0.5, 1000, false, "instances/kqbf/kqbf100");
        Solution bestSol = grasp.solve();
        KQBF_Inverse ObjFunction = (KQBF_Inverse) grasp.ObjFunction;
        Integer knapsackWeight = ObjFunction.evaluateKnapsackWeight(bestSol);
        System.out.println("maxVal = " + bestSol);
        System.out.println("knapsackWeight = " + knapsackWeight);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
    }

}
