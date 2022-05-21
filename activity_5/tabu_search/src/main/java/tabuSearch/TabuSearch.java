package tabuSearch;

import java.util.List;
import java.util.Queue;
import java.util.Random;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearch<E, V extends Number> {

    static Random rng = new Random(0);

    protected V incubentCost;
    protected Problem<E, V> bestSolution;
    protected Problem<E, V> incubentSolution;
    protected List<E> CL;
    protected List<E> RCL;
    protected Queue<E> TL;
    protected final CostComparer<V> costComparer;
    protected final Integer tenure;
    protected final Integer iterations;

    protected abstract Queue<E> makeTL();
    protected abstract void updateTL();
    protected abstract void constructiveHeuristic();
    protected abstract void neighborhoodMove();

    public TabuSearch(
        final Problem<E, V> emptySolution,
        final CostComparer<V> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        this.incubentSolution = emptySolution;
        this.costComparer = costComparer;
        this.tenure = tenure;
        this.iterations = iterations;
    }

    public void solve() {
        constructiveHeuristic();
        this.bestSolution = this.incubentSolution.clone();
        this.TL = makeTL();
        for (int i = 0; i < iterations; ++i) {
            neighborhoodMove();
            updateBestSolution();
            updateTL();
        }
    }

    public Problem<E, V> getBestSolution() {
        return this.bestSolution;
    }

    private void updateBestSolution() {
        if (this.costComparer.isSmaller(this.incubentSolution.getCost(), this.bestSolution.getCost())) {
            this.bestSolution = this.incubentSolution.clone();
        }
    }

}
