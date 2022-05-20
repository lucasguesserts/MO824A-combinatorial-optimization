package tabuSearch;

import java.util.List;
import java.util.Queue;
import java.util.Random;

import problem.Problem;

public abstract class TabuSearchAbstract<E, V extends Number> {

    public static boolean verbose = true;
    static Random rng = new Random(0);

    protected V bestCost;
    protected V incubentCost;
    protected Problem<E, V> bestSolution;
    protected Problem<E, V> incubentSolution;
    protected Integer iterations;
    protected Integer tenure;
    protected List<E> CL;
    protected List<E> RCL;
    protected Queue<E> TL;

    public abstract List<E> makeCL();
    public abstract List<E> makeRCL();
    public abstract Queue<E> makeTL();
    public abstract void updateCL();
    public abstract Problem<E, V> createEmptySol();
    public abstract void neighborhoodMove();

    public TabuSearchAbstract(
        final Problem<E, V> initialSolution,
        final Integer tenure,
        final Integer iterations
    ) {
        this.incubentSolution = initialSolution;
        this.tenure = tenure;
        this.iterations = iterations;
    }

    public Problem<E, V> constructiveHeuristic() {
        this.CL = makeCL();
        this.RCL = makeRCL();
        this.incubentSolution = createEmptySol();
        do {
            Double maxCost = Double.NEGATIVE_INFINITY;
            Double minCost = Double.POSITIVE_INFINITY;
            this.incubentCost = incubentSolution.getCost();
            updateCL();
            for (final E candidate : this.CL) {
                final Double deltaCost = this.incubentSolution.evaluateInsertionCost(candidate).doubleValue();
                if (deltaCost < minCost)
                    minCost = deltaCost;
                if (deltaCost > maxCost)
                    maxCost = deltaCost;
            }
            for (final E candidate: this.CL) {
                final Double deltaCost = this.incubentSolution.evaluateInsertionCost(candidate).doubleValue();
                if (deltaCost <= minCost) {
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
            if (this.bestSolution.getCost().doubleValue() > this.incubentSolution.getCost().doubleValue()) {
                this.bestSolution = this.incubentSolution.clone();
                if (verbose)
                    System.out.println(
                        String.format("Iteration %d: Best Solution: %s", i, this.bestSolution.toString()
                    ));
            }
        }
        return this.bestSolution;
    }

    /**
     * A standard stopping criteria for the constructive heuristic is to repeat
     * until the incumbent solution improves by inserting a new candidate
     * element.
     *
     * @return true if the criteria is met.
     */
    public Boolean constructiveStopCriteria() {
        return !(incubentCost.doubleValue() > incubentSolution.getCost().doubleValue());
    }

}
