package problems.kqbf.solvers;

import java.io.IOException;
import metaheuristics.ga.AbstractGa;
import problems.kqbf.Kqbf;
import solutions.Solution;

public class GaKqbf extends AbstractGa<Integer, Integer> {

    public GaKqbf(
        final Integer generations,
        final Integer popSize,
        final Double mutationRate,
        final String filename
    ) throws IOException {
        super(new Kqbf(filename), generations, popSize, mutationRate);
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

}
