package problems.kqbf;

import java.io.IOException;
import java.util.ArrayList;

import grasp.AbstractGRASP;
import solutions.Solution;

/**
 * Metaheuristic GRASP (Greedy Randomized Adaptive Search Procedure) for
 * obtaining an optimal solution to a QBF (Quadractive Binary Function --
 * {@link #QuadracticBinaryFunction}). Since by default this GRASP considers
 * minimization problems, an inverse QBF function is adopted.
 *
 * @author ccavellucci, fusberti
 */
public class GRASP_KQBF extends AbstractGRASP<Integer> {
    /**
     * the objective function being optimized
     */
    // protected QBF_Inverse ObjFunction;

    /**
     * Constructor for the GRASP_KQBF class. An inverse QBF objective function is
     * passed as argument for the superclass constructor. First-improving false.
     *
     * @param alpha
     *                   The GRASP greediness-randomness parameter (within the range
     *                   [0,1])
     * @param iterations
     *                   The number of iterations which the GRASP will be executed.
     * @param filename
     *                   Name of the file for which the objective function
     *                   parameters
     *                   should be read.
     * @throws IOException
     *                     necessary for I/O operations.
     */
    public GRASP_KQBF(Double alpha, Integer iterations, String filename) throws IOException {
        super(new KQBF_Inverse(filename), alpha, iterations);
    }

    /**
     * Constructor for the GRASP_KQBF class. An inverse QBF objective function is
     * passed as argument for the superclass constructor. First-improving difined as
     * parameter.
     *
     * @param alpha
     *                       The GRASP greediness-randomness parameter (within the
     *                       range
     *                       [0,1])
     * @param iterations
     *                       The number of iterations which the GRASP will be
     *                       executed.
     * @param firstImproving
     *                       The GRASP first-improving parameter.
     * @param filename
     *                       Name of the file for which the objective function
     *                       parameters
     *                       should be read.
     * @throws IOException
     *                     necessary for I/O operations.
     */
    public GRASP_KQBF(
        final Double alpha,
        final Integer iterations,
        final boolean firstImproving,
        final String filename
    ) throws IOException {
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

        Double knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(sol);

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
    public Solution<Integer> createEmptySol() {
        Solution<Integer> sol = new Solution<Integer>();
        sol.cost = 0.0;
        return sol;
    }

    /**
     * The GRASP local search with best-improving.
     *
     * @return An local optimum solution.
     */
    public Solution<Integer> bestImproving() {

        Double minDeltaCost;
        Double knapsackWeight;
        Integer bestCandIn = null, bestCandOut = null;

        do {
            minDeltaCost = Double.POSITIVE_INFINITY;
            knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(sol);
            updateCL();

            // Evaluate insertions
            for (Integer candIn : CL) {
                double deltaCost = ObjFunction.evaluateInsertionCost(candIn, sol);
                boolean fitsKnapsack = ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, candIn);
                if (deltaCost < minDeltaCost && fitsKnapsack) {
                    minDeltaCost = deltaCost;
                    bestCandIn = candIn;
                    bestCandOut = null;
                }
            }
            // Evaluate removals
            for (Integer candOut : sol) {
                double deltaCost = ObjFunction.evaluateRemovalCost(candOut, sol);
                if (deltaCost < minDeltaCost) {
                    minDeltaCost = deltaCost;
                    bestCandIn = null;
                    bestCandOut = candOut;
                }
            }
            // Evaluate exchanges
            for (Integer candIn : CL) {
                for (Integer candOut : sol) {
                    double deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, sol);
                    boolean exchangeFitsKnapsack = ((KQBF_Inverse) ObjFunction).exchangeFitsKnapsack(knapsackWeight, candIn,
                            candOut);
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
                    sol.remove(bestCandOut);
                    CL.add(bestCandOut);
                }
                if (bestCandIn != null) {
                    sol.add(bestCandIn);
                    CL.remove(bestCandIn);
                }
                ObjFunction.evaluate(sol);
            }
        } while (minDeltaCost < -Double.MIN_VALUE);

        return null;
    }

    /**
     * The GRASP local search with first-improving.
     *
     * @return An local optimum solution.
     */
    public Solution<Integer> firstImproving() {

        Double knapsackWeight;
        Integer firstCandIn = null, firstCandOut = null;

        do {
            knapsackWeight = ((KQBF_Inverse) ObjFunction).evaluateKnapsackWeight(sol);
            firstCandIn = null;
            firstCandOut = null;
            updateCL();

            // Evaluate insertions
            for (Integer candIn : CL) {
                double deltaCost = ObjFunction.evaluateInsertionCost(candIn, sol);
                boolean fitsKnapsack = ((KQBF_Inverse) ObjFunction).fitsKnapsack(knapsackWeight, candIn);
                if (deltaCost < -Double.MIN_VALUE && fitsKnapsack) {
                    firstCandIn = candIn;
                    break;
                }
            }
            // Implement the first move for insertion, if it reduces the solution cost.
            if (firstCandIn != null) {
                sol.add(firstCandIn);
                CL.remove(firstCandIn);
                ObjFunction.evaluate(sol);
                continue;
            }

            // Evaluate removals
            for (Integer candOut : sol) {
                double deltaCost = ObjFunction.evaluateRemovalCost(candOut, sol);
                if (deltaCost < -Double.MIN_VALUE) {
                    firstCandOut = candOut;
                    break;
                }
            }
            // Implement the first move for removal, if it reduces the solution cost.
            if (firstCandOut != null) {
                sol.remove(firstCandOut);
                CL.add(firstCandOut);
                ObjFunction.evaluate(sol);
                continue;
            }

            // Evaluate exchanges
            for (Integer candIn : CL) {
                for (Integer candOut : sol) {
                    double deltaCost = ObjFunction.evaluateExchangeCost(candIn, candOut, sol);
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
    public Solution<Integer> localSearch() {

        if (firstImproving) {
            firstImproving();
        } else {
            bestImproving();
        }

        return null;
    }

    public static void staticSolve(final String filename) throws IOException {
        staticSolve(
            0.5,
            1000,
            false,
            filename
        );
    }

    public static void staticSolve(
        final Double alpha,
        final Integer iterations,
        final boolean firstImproving,
        final String filename
    ) throws IOException {
        long startTime = System.currentTimeMillis();
        GRASP_KQBF grasp = new GRASP_KQBF(alpha, iterations, firstImproving, filename);
        Solution<Integer> bestSol = grasp.solve();
        KQBF_Inverse ObjFunction = (KQBF_Inverse) grasp.ObjFunction;
        Double knapsackWeight = ObjFunction.evaluateKnapsackWeight(bestSol);
        System.out.println("maxVal = " + bestSol);
        System.out.println("knapsackWeight = " + knapsackWeight);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time = " + (double) totalTime / (double) 1000 + " seconds");
    }

}
