package grasp;

import java.util.ArrayList;
import java.util.Random;

import problems.Evaluator;
import solutions.Solution;

public abstract class AbstractGRASP<E> {

    public static boolean verbose = true;

    static Random randomGenerator = new Random(0);

    protected Evaluator<E> ObjFunction;

    protected Double alpha;

    protected Integer bestCost;

    protected Integer cost;

    protected Integer maxCostOfCandidates;

    protected Integer minCostOfCandidates;

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
            setMaxMinCandidateCosts();
            updateRCL();
            addCandidateToSolution();
            updateCL();
        }
        return sol;
    }

    private void setMaxMinCandidateCosts() {
        maxCostOfCandidates = Integer.MIN_VALUE;
        minCostOfCandidates = Integer.MAX_VALUE;
        // Explore all candidate elements to enter the solution, saving the
        // highest and lowest cost variation achieved by the candidates.
        for (E c : CL) {
            Integer deltaCost = ObjFunction.evaluateInsertionCost(c, sol);
            if (deltaCost < minCostOfCandidates)
                minCostOfCandidates = deltaCost;
            if (deltaCost > maxCostOfCandidates)
                maxCostOfCandidates = deltaCost;
        }
    }

    private void updateRCL() {
        // Among all candidates, insert into the RCL those with the highest
        // performance using parameter alpha as threshold.
        RCL.clear();
        for (E c : CL) {
            Integer deltaCost = ObjFunction.evaluateInsertionCost(c, sol);
            if (deltaCost <= minCostOfCandidates + alpha * (maxCostOfCandidates - minCostOfCandidates)) {
                RCL.add(c);
            }
        }
    }

    private void addCandidateToSolution() {
        // Choose a candidate randomly from the RCL
        int randomIndex = randomGenerator.nextInt(RCL.size());
        E inCand = RCL.get(randomIndex);
        sol.add(inCand);
        ObjFunction.evaluate(sol);
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
