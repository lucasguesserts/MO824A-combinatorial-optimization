package tabuSearch;

import java.util.List;
import java.util.Queue;
import java.util.Random;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearchAbstract<E, V extends Number> {

    public static boolean verbose = false;
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

    protected abstract List<E> makeCL();
    protected abstract List<E> makeRCL();
    protected abstract Queue<E> makeTL();
    protected abstract void updateCL();
    protected abstract Problem<E, V> createEmptySol();
    protected abstract void constructiveHeuristic();
    protected abstract void neighborhoodMove();

    public TabuSearchAbstract(
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

    public Problem<E, V> solve() {
        this.bestSolution = createEmptySol();
        constructiveHeuristic();
        this.TL = makeTL();
        for (int i = 0; i < iterations; ++i) {
            neighborhoodMove();
            if (this.costComparer.isSmaller(this.incubentSolution.getCost(), this.bestSolution.getCost())) {
                this.bestSolution = this.incubentSolution.clone();
                if (verbose)
                    System.out.println(
                        String.format("Iteration %d: Best Solution: %s", i, this.bestSolution.toString()
                    ));
            }
        }
        return this.bestSolution;
    }

}
