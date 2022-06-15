package problems.qbf.solvers;

import java.io.IOException;
import metaheuristics.ga.AbstractGa;
import problems.qbf.Qbf;
import solutions.Solution;

public class GaQbf extends AbstractGa<Integer, Integer> {

    public GaQbf(
        final Integer generations,
        final Integer popSize,
        final Double mutationRate,
        final String filename
    ) throws IOException {
        super(new Qbf(filename), generations, popSize, mutationRate);
    }

    @Override
    public Solution<Integer> createEmptySol() {
        final Solution<Integer> sol = new Solution<Integer>();
        sol.cost = 0.0;
        return sol;
    }

    @Override
    protected Solution<Integer> decode(final Chromosome chromosome) {
        final Solution<Integer> solution = createEmptySol();
        for (int locus = 0; locus < chromosome.size(); locus++) {
            if (chromosome.get(locus) == 1) {
                solution.add(locus);
            }
        }
        objFunction.evaluate(solution);
        return solution;
    }

    @Override
    protected Chromosome generateRandomChromosome() {
        final Chromosome chromosome = new Chromosome();
        for (int i = 0; i < chromosomeSize; i++) {
            chromosome.add(rng.nextInt(2));
        }
        return chromosome;
    }

    @Override
    protected Double fitness(final Chromosome chromosome) {
        return decode(chromosome).cost;
    }

    @Override
    protected void mutateGene(final Chromosome chromosome, final Integer locus) {
        chromosome.set(locus, 1 - chromosome.get(locus));
    }

    public static void main(String[] args) throws IOException {
        final long startTime = System.currentTimeMillis();
        final GaQbf ga = new GaQbf(1000, 100, 1.0 / 100.0, "../instances/qbf/qbf100");
        final Solution<Integer> bestSol = ga.solve();
        System.out.println("maxVal = " + bestSol);
        final long endTime = System.currentTimeMillis();
        final long totalTime = endTime - startTime;
        System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
    }

}
