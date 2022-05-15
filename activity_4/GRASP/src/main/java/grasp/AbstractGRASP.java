package grasp;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

import problems.Evaluator;
import solutions.Solution;

public abstract class AbstractGRASP<E> {

    public enum ConstructionMechanism {
        DEFAULT,
        RANDOM_PLUS_GREEDY,
        SAMPLED_GREEDY
    };

    protected ConstructionMechanism constructionMechanism;

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

    protected long startTime;

    protected long timeElapsed;

    protected final long MAXIMUM_RUNNING_TIME_MILLISECONDS = 30*60*1000; // 30 minutes

    public abstract ArrayList<E> makeCL();

    public abstract ArrayList<E> makeRCL();

    public abstract void updateCL();

    public abstract Solution<E> createEmptySol();

    public abstract Solution<E> localSearch();


    public AbstractGRASP(Evaluator<E> objFunction, Double alpha, ConstructionMechanism constructionMechanism,
            Integer iterations, boolean firstImproving) {
        this.ObjFunction = objFunction;
        this.alpha = alpha;
        this.constructionMechanism = constructionMechanism;
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

    public Solution<E> randomPlusGreedy() {
        CL = makeCL();
        RCL = makeRCL();
        currentSolution = createEmptySol();
        costOfLastSolution = Integer.MAX_VALUE;
        int p = (int) (ObjFunction.getDomainSize() * 0.80); // Use randomness in 80% of the construction
        while (!constructiveStopCriteria()) {
            costOfLastSolution = currentSolution.cost;
            setMaxMinCandidateCosts();
            if (p > 0) {
                updateRCL(1.0);
            } else {
                updateRCL(0.0);
            }
            addCandidateToSolution();
            updateCL();
            p -= 1;
        }
        return currentSolution;
    }

    public Solution<E> sampledGreedy() {
        CL = makeCL();
        RCL = makeRCL();
        currentSolution = createEmptySol();
        costOfLastSolution = Integer.MAX_VALUE;
        int p = (int) (ObjFunction.getDomainSize() * 0.70);
        while (!constructiveStopCriteria()) {
            costOfLastSolution = currentSolution.cost;
            setMaxMinCandidateCosts();
            updateRCL(p);
            addBestCandidateToSolution();
            updateCL();
            p -= 1;
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

    private void updateRCL(Double currentAlpha) {
        // Among all candidates, insert into the RCL those with the highest
        // performance using parameter currentAlpha as threshold.
        // currentAlpha will be 1 or 0.
        RCL.clear();
        for (E c : CL) {
            Integer deltaCost = ObjFunction.evaluateInsertionCost(c, currentSolution);
            if (deltaCost <= minCostOfCandidates + currentAlpha * (maxCostOfCandidates - minCostOfCandidates)) {
                RCL.add(c);
            }
        }
    }

    private void updateRCL(int p) {
        int RCL_size = Math.min(p, CL.size());
        RCL.clear();
        Collections.shuffle(CL);
        for (int i = 0; i < RCL_size; ++i) {
            RCL.add(CL.get(i));
        }
    }

    private void addCandidateToSolution() {
        // Choose a candidate randomly from the RCL
        int randomIndex = randomGenerator.nextInt(RCL.size());
        E inCand = RCL.get(randomIndex);
        currentSolution.add(inCand);
        currentSolution.cost = ObjFunction.evaluate(currentSolution);
    }

    private void addBestCandidateToSolution() {
        // Choose the best candidate from the RCL
        Integer bestCost = Integer.MIN_VALUE;
        E bestCandidate = CL.get(0);
        for (E c : CL) {
            Integer cost = ObjFunction.evaluateInsertionCost(c, currentSolution);
            if (cost < bestCost) {
                bestCost = cost;
                bestCandidate = c;
            }
        }
        currentSolution.add(bestCandidate);
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
        timeElapsed = 0;
        startTime = System.currentTimeMillis();
        for (int iterationCount = 0; iterationCount < maximumNumberOfIterations; ++iterationCount) {
            final Boolean tooMuchTimeRunning = timeElapsed > MAXIMUM_RUNNING_TIME_MILLISECONDS;
            if (tooMuchTimeRunning) break;
            if (constructionMechanism == ConstructionMechanism.DEFAULT) {
                constructiveHeuristic();
            } else if (constructionMechanism == ConstructionMechanism.RANDOM_PLUS_GREEDY) {
                randomPlusGreedy();
            } else if (constructionMechanism == ConstructionMechanism.SAMPLED_GREEDY) {
                sampledGreedy();
            }
            localSearch();
            if (bestSolution.cost > currentSolution.cost) {
                bestSolution = new Solution<E>(currentSolution);
                if (verbose)
                    displayIterationStatus(iterationCount);
            }
            updateCurrentTime();
        }
        return bestSolution;
    }

    private void updateCurrentTime() {
        final var currentTime = System.currentTimeMillis();
        timeElapsed = currentTime - startTime;
    }

    private void displayIterationStatus(final Integer iterationCount) {
        System.out.println(
                "> Iteration " + iterationCount
                        + "\n\t" + "BestSolution: " + bestSolution);
    }

}
