package problems.qbf.solvers;

import java.io.IOException;
import metaheuristics.ga.AbstractGA;
import problems.qbf.QBF;
import solutions.Solution;

public class GA_QBF extends AbstractGA<Integer, Integer> {

    public GA_QBF(Integer generations, Integer popSize, Double mutationRate, String filename) throws IOException {
        super(new QBF(filename), generations, popSize, mutationRate);
    }

    @Override
    public Solution<Integer> createEmptySol() {
        Solution<Integer> sol = new Solution<Integer>();
        sol.cost = 0.0;
        return sol;
    }

    @Override
    protected Solution<Integer> decode(Chromosome chromosome) {
        Solution<Integer> solution = createEmptySol();
        for (int locus = 0; locus < chromosome.size(); locus++) {
            if (chromosome.get(locus) == 1) {
                solution.add(locus);
            }
        }
        ObjFunction.evaluate(solution);
        return solution;
    }

    @Override
    protected Chromosome generateRandomChromosome() {
        Chromosome chromosome = new Chromosome();
        for (int i = 0; i < chromosomeSize; i++) {
            chromosome.add(rng.nextInt(2));
        }
        return chromosome;
    }

    @Override
    protected Double fitness(Chromosome chromosome) {
        return decode(chromosome).cost;
    }

    @Override
    protected void mutateGene(Chromosome chromosome, Integer locus) {
        chromosome.set(locus, 1 - chromosome.get(locus));
    }

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        GA_QBF ga = new GA_QBF(1000, 100, 1.0 / 100.0, "../instances/qbf/qbf100");
        Solution<Integer> bestSol = ga.solve();
        System.out.println("maxVal = " + bestSol);
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        System.out.println("Time = " + (double) totalTime / (double) 1000 + " seg");
    }

}
