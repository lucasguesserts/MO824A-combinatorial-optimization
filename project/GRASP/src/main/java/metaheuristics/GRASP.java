package metaheuristics;

import java.util.ArrayList;
import java.util.Random;

import problems.Evaluator;
import solutions.Solution;

public abstract class GRASP {

    public static boolean verbose = true;

    static Random rng = new Random(0);

    protected Evaluator ObjFunction;

    protected Double alpha;

    protected Double bestCost;

    protected Double cost;

    protected Solution bestSol;

    protected Solution sol;

    protected Integer iterations;

    protected ArrayList<Integer> CL;

    protected ArrayList<Integer> RCL;

    protected boolean firstImproving;

    public abstract ArrayList<Integer> makeCL();

    public abstract ArrayList<Integer> makeRCL();

    public abstract void updateCL();

    public abstract Solution createEmptySol();

    public abstract Solution localSearch();

    public GRASP(Evaluator objFunction, Double alpha, Integer iterations) {
        this.ObjFunction = objFunction;
        this.alpha = alpha;
        this.iterations = iterations;
        this.firstImproving = false;
    }

    public GRASP(Evaluator objFunction, Double alpha, Integer iterations, boolean firstImproving) {
        this.ObjFunction = objFunction;
        this.alpha = alpha;
        this.iterations = iterations;
        this.firstImproving = firstImproving;
    }

    public Solution constructiveHeuristic() {

        CL = makeCL();
        RCL = makeRCL();
        sol = createEmptySol();
        cost = Double.POSITIVE_INFINITY;

        while (!constructiveStopCriteria()) {

            double maxCost = Double.NEGATIVE_INFINITY, minCost = Double.POSITIVE_INFINITY;
            cost = ObjFunction.evaluate(sol).doubleValue();
            updateCL();

            if (CL.isEmpty()) {
                break;
            }

            for (Integer c : CL) {
                Double deltaCost = ObjFunction.evaluateInsertionCost(c, sol).doubleValue();
                if (deltaCost < minCost)
                    minCost = deltaCost;
                if (deltaCost > maxCost)
                    maxCost = deltaCost;
            }

            for (Integer c : CL) {
                Double deltaCost = ObjFunction.evaluateInsertionCost(c, sol).doubleValue();
                if (deltaCost <= minCost + alpha * (maxCost - minCost)) {
                    RCL.add(c);
                }
            }

            int rndIndex = rng.nextInt(RCL.size());
            Integer inCand = RCL.get(rndIndex);
            CL.remove(inCand);
            sol.add(inCand);
            ObjFunction.evaluate(sol);
            RCL.clear();

        }

        return sol;
    }

    public Solution solve() {

        bestSol = createEmptySol();
        for (int i = 0; i < iterations; i++) {
            constructiveHeuristic();
            localSearch();
            if (bestSol.cost > sol.cost) {
                bestSol = new Solution(sol);
                if (verbose)
                    System.out.println("(Iter. " + i + ") BestSol = " + bestSol);
            }
        }

        return bestSol;
    }

    public Boolean constructiveStopCriteria() {
        return (cost > sol.cost) && !CL.isEmpty() ? false : true;
    }

}
