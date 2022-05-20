package tabuSearch;

import java.util.List;
import java.util.Queue;
import java.util.Random;

import costCoparer.CostComparer;
import problem.Problem;

public abstract class TabuSearchAbstract<E, V extends Number> {

    public static boolean verbose = true;
    static Random rng = new Random(0);

    protected V bestCost;
    protected V incubentCost;
    protected Problem<E, V> bestSolution;
    protected Problem<E, V> incubentSolution;
    protected List<E> CL;
    protected List<E> RCL;
    protected Queue<E> TL;
    protected final CostComparer<V> costComparer;
    protected final Integer tenure;
    protected final Integer iterations;

    public abstract List<E> makeCL();
    public abstract List<E> makeRCL();
    public abstract Queue<E> makeTL();
    public abstract void updateCL();
    public abstract Problem<E, V> createEmptySol();
    public abstract void neighborhoodMove();

    public TabuSearchAbstract(
        final Problem<E, V> initialSolution,
        final CostComparer<V> costComparer,
        final Integer tenure,
        final Integer iterations
    ) {
        this.incubentSolution = initialSolution;
        this.costComparer = costComparer;
        this.tenure = tenure;
        this.iterations = iterations;
    }

    public Problem<E, V> constructiveHeuristic() {
        this.CL = makeCL();
        this.RCL = makeRCL();
        this.incubentSolution = createEmptySol();
        do {
            V maxCost = this.costComparer.getMinCost();
            V minCost = this.costComparer.getMaxCost();
            this.incubentCost = incubentSolution.getCost();
            updateCL();
            for (final E candidate : this.CL) {
                final V deltaCost = this.incubentSolution.evaluateInsertionCost(candidate);
                if (this.costComparer.isSmaller(deltaCost, minCost))
                    minCost = deltaCost;
                if (this.costComparer.isSmaller(maxCost, deltaCost))
                    maxCost = deltaCost;
            }
            for (final E candidate: this.CL) {
                final V deltaCost = this.incubentSolution.evaluateInsertionCost(candidate);
                if (this.costComparer.isSmaller(deltaCost, minCost) || deltaCost.equals(minCost)) {
                    this.RCL.add(candidate);
                }
            }
            final int randomIndex = rng.nextInt(RCL.size());
            final E inCand = RCL.get(randomIndex);
            this.CL.remove(inCand);
            this.incubentSolution.add(inCand);
            RCL.clear();
        } while (!constructiveStopCriteria());
        return this.incubentSolution;
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

    public Boolean constructiveStopCriteria() {
        return ! this.costComparer.isSmaller(incubentCost, incubentSolution.getCost());
    }

}
