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
        final String filename,
        final Double targetValue
    ) throws IOException {
        super(new Kqbf(filename), generations, popSize, mutationRate, targetValue);
    }

    @Override
    public Solution<Integer> createEmptySol() {
        final Solution<Integer> sol = new Solution<Integer>();
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
        // fill chromosome with zeros
        for (int i = 0; i < chromosomeSize; i++) {
            chromosome.add(0);
        }
        // add alleles till the fitness does not increase
        Double currentFitness = 0.0;
        Double newFitness = 0.0;
        int index = -1;
        while (true) {
            currentFitness = newFitness;
            index = rng.nextInt(chromosomeSize);
            chromosome.set(index, 1);
            newFitness = fitness(chromosome);
            if (newFitness < currentFitness) {
                break;
            }
        }
        chromosome.set(index, 0); // unset index which made fitness lower
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
