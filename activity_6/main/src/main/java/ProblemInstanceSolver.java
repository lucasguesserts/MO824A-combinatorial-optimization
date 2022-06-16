import java.io.IOException;
import org.json.JSONObject;
import problems.kqbf.solvers.GaKqbf;
import solutions.Solution;

public class ProblemInstanceSolver {

    private final int numberOfGenerations;
    private final int populationSize;
    private final double mutationRate;
    private final String problemInstance;
    private final String instanceIdentifier;

    private Solution<Integer> bestSol;
    private Integer knapsackCapacity;
    private Integer domainSize;

    private long startTime = System.currentTimeMillis();
    private long endTime = System.currentTimeMillis();
    private long totalTime = System.currentTimeMillis();

    public ProblemInstanceSolver(
            final int numberOfGenerations,
            final int populationSize,
            final double mutationRate,
            final String problemInstance
    ) {
        this.numberOfGenerations = numberOfGenerations;
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.problemInstance = problemInstance;
        this.instanceIdentifier = this.getInstanceIdentifier();
    }

    public void solve() throws IOException {
        this.startTime = System.currentTimeMillis();
        final var ga = new GaKqbf(
            numberOfGenerations,
            populationSize,
            mutationRate,
            problemInstance
        );
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

    public void log() {
        System.out.println(this.getLogAsJson().toString(2));
    }

    private String getInstanceIdentifier() {
        final String[] parts = this.problemInstance.split("/");
        final String lastPart = parts[parts.length - 1];
        return lastPart;
    }

    public JSONObject getLogAsJson() {
        final var obj = new JSONObject();
        obj.put("Setup", this.getSetup());
        obj.put("Solution", this.getSolution());
        obj.put("RunningTime", (double) this.totalTime / 1000.0);
        return obj;
    }

    private JSONObject getSetup() {
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

    private JSONObject getSolution() {
        final var obj = new JSONObject();
        obj.put("cost", this.bestSol.cost);
        obj.put("weight", this.bestSol.weight);
        obj.put("elements", this.bestSol.elementsToString());
        return obj;
    }

}
