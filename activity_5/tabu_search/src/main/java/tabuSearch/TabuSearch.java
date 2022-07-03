package tabuSearch;

import java.util.Queue;
import java.util.Random;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearch<E, V extends Number> {

    static Random rng = new Random(0);

    protected Problem<E, V> bestSolution;
    protected Problem<E, V> incubentSolution;
    protected Queue<E> TL;
    protected final CostComparer<V> costComparer;
    protected final Integer tenure;
    protected final Integer maximumNumberOfIterations;
    protected Integer currentIteration = 0;
    private final Boolean verbose = Boolean.TRUE;

    private long startTime;
    private long elapsedTime;
    private final long maximimRunningTime;

    protected abstract Queue<E> makeTL();
    protected abstract void updateTL();

    protected abstract void constructiveHeuristic();
    protected abstract void neighborhoodMove();

    protected abstract Boolean intensificationCriteria();
    protected abstract void updateIntensificationCriteria();
    protected abstract Problem<E, V> makeIntenseSearch();

    protected abstract Boolean diversificationCriteria();
    protected abstract Problem<E, V> makeDiversificationConstruction();

    protected Boolean targetValueHasBeenReached() {
        return Boolean.FALSE;
    };

    protected TabuSearch(
        final Problem<E, V> emptySolution,
        final CostComparer<V> costComparer,
        final Double tenureRatio,
        final Integer iterations
    ) {
        this.incubentSolution = emptySolution;
        this.costComparer = costComparer;
        this.tenure = (int) Math.round(tenureRatio * emptySolution.getDomainSize());
        this.maximumNumberOfIterations = iterations;
        this.maximimRunningTime = 30*60*1000; // milliseconds
    }

    protected TabuSearch(
        final Problem<E, V> emptySolution,
        final CostComparer<V> costComparer,
        final Double tenureRatio,
        final Integer iterations,
        final V targetValue
    ) {
        this.incubentSolution = emptySolution;
        this.costComparer = costComparer;
        this.tenure = (int) Math.round(tenureRatio * emptySolution.getDomainSize());
        this.maximumNumberOfIterations = iterations;
        this.maximimRunningTime = 30*60*1000; // milliseconds
    }

    public void solve() {
        this.startTime = System.currentTimeMillis();
        constructiveHeuristic();
        System.out.println(String.format("initialSolution: %s", this.incubentSolution.toString()));
        this.bestSolution = this.incubentSolution.clone();
        this.TL = makeTL();
        this.elapsedTime = System.currentTimeMillis() - this.startTime;
        for (this.currentIteration = 0;
            this.currentIteration < maximumNumberOfIterations && this.elapsedTime < this.maximimRunningTime;
            ++this.currentIteration
        ) {
            neighborhoodMove();
            updateBestSolution();
            updateTL();
            if (intensificationCriteria()) {
                this.incubentSolution = makeIntenseSearch();
                updateBestSolution();
            }
            if (!intensificationCriteria() && diversificationCriteria()) {
                this.incubentSolution = makeDiversificationConstruction();
                this.TL = this.makeTL(); // reset tabu list
                this.makeIntensiveSearchOnDiversificationConstruction();
                updateBestSolution();
            }
            this.elapsedTime = System.currentTimeMillis() - this.startTime;
            if (this.targetValueHasBeenReached()) {
                System.out.println("Target value reached, algorithm stops here");
                break;
            }
        }
    }

    private void makeIntensiveSearchOnDiversificationConstruction() {
        final var originalBestSolution = this.bestSolution;
        this.bestSolution = this.incubentSolution.clone();
        this.incubentSolution = makeIntenseSearch();
        this.bestSolution = originalBestSolution;
    }

    public Problem<E, V> getBestSolution() {
        return this.bestSolution;
    }

    protected void updateBestSolution() {
        if (this.costComparer.isSmaller(this.incubentSolution.getCost(), this.bestSolution.getCost())) {
            this.updateIntensificationCriteria();
            this.bestSolution = this.incubentSolution.clone();
            this.iterationLog();
        }
    }

    private void iterationLog() {
        if(this.verbose) {
            System.out.println(String.format(
                "Iteration %d: best solution: %s",
                this.currentIteration,
                this.bestSolution.toString()));
        }
    }

}
