package grasp;

import java.util.ArrayList;
import java.util.Random;

import problems.Evaluator;
import solutions.Solution;

public abstract class AbstractGRASP<E> {

    public static boolean verbose = true;

    static Random rng = new Random(0);

    protected Evaluator<E> ObjFunction;

    protected Double alpha;

    protected Integer bestCost;

    protected Integer cost;

    protected Solution<E> bestSol;

    protected Solution<E> sol;

    protected Integer iterations;

    protected ArrayList<E> CL;

    protected ArrayList<E> RCL;

    protected boolean firstImproving;

    public abstract ArrayList<E> makeCL();

    public abstract ArrayList<E> makeRCL();

    public abstract void updateCL();

    public abstract Solution<E> createEmptySol();

    public abstract Solution<E> localSearch();

    public AbstractGRASP(Evaluator<E> objFunction, Double alpha, Integer iterations) {
        this.ObjFunction = objFunction;
        this.alpha = alpha;
        this.iterations = iterations;
        this.firstImproving = false;
    }
    public AbstractGRASP(Evaluator<E> objFunction, Double alpha, Integer iterations, boolean firstImproving) {
        this.ObjFunction = objFunction;
        this.alpha = alpha;
        this.iterations = iterations;
        this.firstImproving = firstImproving;
    }

    public Solution<E> constructiveHeuristic() {
        CL = makeCL();
        RCL = makeRCL();
        sol = createEmptySol();
        cost = Integer.MAX_VALUE;
        while (!constructiveStopCriteria()) {
            Integer maxCost = Integer.MIN_VALUE;
            Integer minCost = Integer.MAX_VALUE;
            cost = ObjFunction.evaluate(sol);
            updateCL();
            if (CL.isEmpty()) {
                break;
            }
            // Explore all candidate elements to enter the solution, saving the
            // highest and lowest cost variation achieved by the candidates.
            for (E c : CL) {
                Integer deltaCost = ObjFunction.evaluateInsertionCost(c, sol);
                if (deltaCost < minCost)
                    minCost = deltaCost;
                if (deltaCost > maxCost)
                    maxCost = deltaCost;
            }
            // Among all candidates, insert into the RCL those with the highest
            // performance using parameter alpha as threshold.
            for (E c : CL) {
                Integer deltaCost = ObjFunction.evaluateInsertionCost(c, sol);
                if (deltaCost <= minCost + alpha * (maxCost - minCost)) {
                    RCL.add(c);
                }
            }
            // Choose a candidate randomly from the RCL
            int rndIndex = rng.nextInt(RCL.size());
            E inCand = RCL.get(rndIndex);
            CL.remove(inCand);
            sol.add(inCand);
            ObjFunction.evaluate(sol);
            RCL.clear();
        }
        return sol;
    }

    /**
     * A standard stopping criteria for the constructive heuristic is to repeat
     * until the current solution improves by inserting a new candidate
     * element.
     *
     * @return true if the criteria is met.
     */
    public Boolean constructiveStopCriteria() {
        return (cost > sol.cost) && !CL.isEmpty() ? false : true;
    }

    public Solution<E> solve() {
        bestSol = createEmptySol();
        for (int i = 0; i < iterations; i++) {
            constructiveHeuristic();
            localSearch();
            if (bestSol.cost > sol.cost) {
                bestSol = new Solution<E>(sol);
                if (verbose)
                    System.out.println("(Iter. " + i + ") BestSol = " + bestSol);
            }
        }
        return bestSol;
    }

}
