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

    protected Integer costOfLastSolution;

    protected Integer maxCostOfCandidates;

    protected Integer minCostOfCandidates;

    protected Solution<E> bestSolution;

    protected Solution<E> currentSolution;

    protected Integer maximumNumberOfIterations;

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
        this.maximumNumberOfIterations = iterations;
        this.firstImproving = false;
    }
    public AbstractGRASP(Evaluator<E> objFunction, Double alpha, Integer iterations, boolean firstImproving) {
        this.ObjFunction = objFunction;
        this.alpha = alpha;
        this.maximumNumberOfIterations = iterations;
        this.firstImproving = firstImproving;
    }

    public Solution<E> constructiveHeuristic() {
        CL = makeCL();
        RCL = makeRCL();
        currentSolution = createEmptySol();
        costOfLastSolution = Integer.MAX_VALUE;
        while (!constructiveStopCriteria()) {
            costOfLastSolution = currentSolution.cost;
            setMaxMinCandidateCosts();
            updateRCL();
            addCandidateToSolution();
            updateCL();
        }
        return currentSolution;
    }

    private void setMaxMinCandidateCosts() {
        maxCostOfCandidates = Integer.MIN_VALUE;
        minCostOfCandidates = Integer.MAX_VALUE;
        // Explore all candidate elements to enter the solution, saving the
        // highest and lowest cost variation achieved by the candidates.
        for (E c : CL) {
            Integer deltaCost = ObjFunction.evaluateInsertionCost(c, currentSolution);
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
            Integer deltaCost = ObjFunction.evaluateInsertionCost(c, currentSolution);
            if (deltaCost <= minCostOfCandidates + alpha * (maxCostOfCandidates - minCostOfCandidates)) {
                RCL.add(c);
            }
        }
    }

    private void addCandidateToSolution() {
        // Choose a candidate randomly from the RCL
        int randomIndex = randomGenerator.nextInt(RCL.size());
        E inCand = RCL.get(randomIndex);
        currentSolution.add(inCand);
        currentSolution.cost = ObjFunction.evaluate(currentSolution);
    }

    public Boolean constructiveStopCriteria() {
        // if the stop criteria has been met (true case),
        // then stop,
        // else continue running
        final Boolean solutionHasImproved = costOfLastSolution > currentSolution.cost;
        final Boolean thereAreNoMoreCandidates = CL.isEmpty();
        return (!solutionHasImproved) || thereAreNoMoreCandidates;
    }

    public Solution<E> solve() {
        bestSolution = createEmptySol();
        for (int iterationCount = 0; iterationCount < maximumNumberOfIterations; ++iterationCount) {
            constructiveHeuristic();
            localSearch();
            if (bestSolution.cost > currentSolution.cost) {
                bestSolution = new Solution<E>(currentSolution);
                if (verbose) displayIterationStatus(iterationCount);
            }
        }
        return bestSolution;
    }

    private void displayIterationStatus(final Integer iterationCount) {
        System.out.println(
            "> Iteration " + iterationCount
            + "\n\t" + "BestSolution: " + bestSolution
        );
    }

}
