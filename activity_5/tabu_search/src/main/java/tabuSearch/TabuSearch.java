package tabuSearch;

import java.util.Queue;
import java.util.Random;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearch<E, V extends Number> {

    static Random rng = new Random(0);

    protected V incubentCost;
    protected Problem<E, V> bestSolution;
    protected Problem<E, V> incubentSolution;
    protected Queue<E> TL;
    protected final CostComparer<V> costComparer;
    protected final Integer tenure;
    protected final Integer maximumNumberOfIterations;
    protected Integer currentIteration = 0;
    private final Boolean verbose = Boolean.TRUE;

    protected abstract Queue<E> makeTL();
    protected abstract void updateTL();

    protected abstract void constructiveHeuristic();
    protected abstract void neighborhoodMove();

    protected abstract Boolean intensificationCriteria();
    protected abstract void updateIntensificationCriteria();
    protected abstract Problem<E, V> makeIntenseSearch();

    protected abstract Boolean diversificationCriteria();
    protected abstract Problem<E, V> makeDiversificationConstruction();

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
    }

    public void solve() {
        constructiveHeuristic();
        this.bestSolution = this.incubentSolution.clone();
        this.TL = makeTL();
        for (this.currentIteration = 0; this.currentIteration < maximumNumberOfIterations; ++this.currentIteration) {
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
