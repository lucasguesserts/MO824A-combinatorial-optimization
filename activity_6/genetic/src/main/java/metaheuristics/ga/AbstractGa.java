package metaheuristics.ga;

import java.util.ArrayList;
import java.util.Random;
import problems.Evaluator;
import solutions.Solution;
import java.util.*;

public abstract class AbstractGa<G extends Number, F> {

    public class Chromosome extends ArrayList<G> {}

    public class Population extends ArrayList<Chromosome> {}

    public static boolean verbose = true;
    public static final Random rng = new Random(0);

    protected Evaluator<F> objFunction;
    protected int generations;
    protected int popSize;
    protected int chromosomeSize;
    protected double mutationRate;

    protected Double bestCost;
    protected Solution<F> bestSol;
    protected Chromosome bestChromosome;

    public abstract Solution<F> createEmptySol();

    protected abstract Solution<F> decode(Chromosome chromosome);

    protected abstract Chromosome generateRandomChromosome();

    protected abstract Double fitness(Chromosome chromosome);

    protected abstract void mutateGene(Chromosome chromosome, Integer locus);

    public AbstractGa(
        final Evaluator<F> objFunction,
        final Integer generations,
        final Integer popSize,
        final Double mutationRate
    ) {
        this.objFunction = objFunction;
        this.generations = generations;
        this.popSize = popSize;
        this.chromosomeSize = this.objFunction.getDomainSize();
        this.mutationRate = mutationRate;
    }

    public Solution<F> solve() {
        Population population = initializePopulation();
        bestChromosome = getBestChromosome(population);
        bestSol = decode(bestChromosome);
        System.out.println("(Gen. " + 0 + ") BestSol = " + bestSol);
        for (int g = 1; g <= generations; g++) {
            final Population parents = selectParents(population);
            final Population offsprings = crossover(parents);
            final Population mutants = mutate(offsprings);
            final Population newPopulation = selectPopulation(parents, mutants);
            population = newPopulation;
            bestChromosome = getBestChromosome(population);
            if (fitness(bestChromosome) > bestSol.cost) {
                bestSol = decode(bestChromosome);
                if (verbose) {
                    System.out.println("(Gen. " + g + ") BestSol = " + bestSol);
                }
            }
        }
        return bestSol;
    }

    protected Population initializePopulation() {
        final Population population = new Population();
        while (population.size() < popSize) {
            population.add(generateRandomChromosome());
        }
        return population;
    }

    protected Chromosome getBestChromosome(final Population population) {
        double bestFitness = Double.NEGATIVE_INFINITY;
        Chromosome bestChromosome = null;
        for (final Chromosome c : population) {
            final double fitness = fitness(c);
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestChromosome = c;
            }
        }
        if (bestChromosome == null) {
            throw new RuntimeException(
                "no chromosome satisfy the knapsack weight so all costs are '-inf'");
        }
        return bestChromosome;
    }

    protected Chromosome getWorseChromosome(final Population population) {
        double worseFitness = Double.POSITIVE_INFINITY;
        Chromosome worseChromosome = null;
        for (Chromosome c : population) {
            final double fitness = fitness(c);
            if (fitness < worseFitness) {
                worseFitness = fitness;
                worseChromosome = c;
            }
        }
        return worseChromosome;
    }

    protected Population selectParents(final Population population) {
        final Population parents = new Population();
        while (parents.size() < popSize) {
            final int index1 = rng.nextInt(popSize);
            final Chromosome parent1 = population.get(index1);
            final int index2 = rng.nextInt(popSize);
            final Chromosome parent2 = population.get(index2);
            if (fitness(parent1) > fitness(parent2)) {
                parents.add(parent1);
            } else {
                parents.add(parent2);
            }
        }
        return parents;
    }

    protected Population crossover(final Population parents) {
        final Population offsprings = new Population();
        for (int i = 0; i < popSize; i += 2) {
            final Chromosome parent1 = parents.get(i);
            final Chromosome parent2 = parents.get(i + 1);

            int crosspoint1 = rng.nextInt(chromosomeSize + 1);
            int crosspoint2 = crosspoint1 + rng.nextInt((chromosomeSize + 1) - crosspoint1);
            
            int L = -1, R = -1;
            for (int idx = 0; idx < parent1.size(); idx++) {
                if (parent1.get(idx) != parent2.get(idx)) {
					if (L == -1) {
						L = idx;
					} else {
						R = idx;
					}
                }
            }
            
            
            if (L != -1 &&  R != -1 &&  R > L) {
                crosspoint1 = rng.nextInt(R - L + 1);
                crosspoint2 = crosspoint1 + rng.nextInt((R - L + 1) - crosspoint1);
                
                crosspoint1 += L;
                crosspoint2 += L;
            }

            final Chromosome offspring1 = new Chromosome();
            final Chromosome offspring2 = new Chromosome();
            for (int j = 0; j < chromosomeSize; j++) {
                if (j >= crosspoint1 && j < crosspoint2) {
                    offspring1.add(parent2.get(j));
                    offspring2.add(parent1.get(j));
                } else {
                    offspring1.add(parent1.get(j));
                    offspring2.add(parent2.get(j));
                }
            }
            offsprings.add(offspring1);
            offsprings.add(offspring2);
        }
        return offsprings;
    }

    protected Population mutate(final Population offsprings) {
        for (final Chromosome c : offsprings) {
            for (int locus = 0; locus < chromosomeSize; locus++) {
                if (rng.nextDouble() < mutationRate) {
                    mutateGene(c, locus);
                }
            }
        }
        return offsprings;
    }

    protected Population selectPopulation(final Population parents, final Population offsprings) {
		offsprings.addAll(parents);
		
		Comparator<Chromosome> compareByFitness = (Chromosome c1, Chromosome c2) -> Double.compare(fitness(c1), fitness(c2));
		Collections.sort(offsprings, compareByFitness);
		
		while(offsprings.size() > popSize) {
			offsprings.remove(0);
		}
		
        final Chromosome worse = getWorseChromosome(offsprings);
        if (fitness(worse) < fitness(bestChromosome)) {
            offsprings.remove(worse);
            offsprings.add(bestChromosome);
        }
        return offsprings;
    }

    public Integer getKnapsackCapacity() {
        return this.objFunction.getknapsackCapacity();
    }

    public Integer getDomainSize() {
        return this.objFunction.getDomainSize();
    }

}
