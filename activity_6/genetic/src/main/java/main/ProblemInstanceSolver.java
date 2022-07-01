package main;

import java.io.IOException;
import org.json.JSONObject;
import problems.kqbf.solvers.GaKqbf;
import solutions.Solution;

public class ProblemInstanceSolver {

    protected final int numberOfGenerations;
    protected final int populationSize;
    protected final double mutationRate;
    protected final String problemInstance;
    protected final String instanceIdentifier;

    protected final GaKqbf ga;

    protected Solution<Integer> bestSol;
    protected Integer knapsackCapacity;
    protected Integer domainSize;

    protected long startTime = System.currentTimeMillis();
    protected long endTime = System.currentTimeMillis();
    public long totalTime = System.currentTimeMillis();

    public ProblemInstanceSolver(
            final int numberOfGenerations,
            final int populationSize,
            final double mutationRate,
            final String problemInstance
    ) throws IOException {
        this.numberOfGenerations = numberOfGenerations;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.problemInstance = problemInstance;
        this.instanceIdentifier = this.getInstanceIdentifier();
        this.ga = new GaKqbf(
            this.numberOfGenerations,
            this.populationSize,
            this.mutationRate,
            this.problemInstance
        );
    }

    public void solve() {
        this.startTime = System.currentTimeMillis();
        System.out.println(">>>>> Solving problem:\n");
        this.bestSol = ga.solve();
        this.knapsackCapacity = ga.getKnapsackCapacity();
        this.domainSize = ga.getDomainSize();
        System.out.println("\n<<<<< Problem Solved\n");
        this.endTime = System.currentTimeMillis();
        this.totalTime = this.endTime - this.startTime;
    }

    public void logStart() {
        System.out.println(String.format(
            "\n\n========== Start: %s ==========\n\n",
            instanceIdentifier
        ));
    }

    public void logEnd() {
        System.out.println(String.format(
            "\n\n========== End: %s ==========\n\n",
            instanceIdentifier
        ));
    }

    protected String getInstanceIdentifier() {
        final String[] parts = this.problemInstance.split("/");
        final String lastPart = parts[parts.length - 1];
        return lastPart;
    }

    public JSONObject getSetup() {
        final var obj = new JSONObject();
        obj.put("numberOfGenerations", this.numberOfGenerations);
        obj.put("populationSize", this.populationSize);
        obj.put("mutationRate", this.mutationRate);
        obj.put("problemInstance", this.problemInstance);
        obj.put("instanceIdentifier", this.instanceIdentifier);
        obj.put("knapsackCapacity", this.knapsackCapacity);
        obj.put("domainSize", this.domainSize);
        return obj;
    }

    protected JSONObject getSolution() {
        final var obj = new JSONObject();
        obj.put("cost", this.bestSol.cost);
        obj.put("weight", this.bestSol.weight);
        obj.put("elements", this.bestSol.elementsToString());
        return obj;
    }

}
