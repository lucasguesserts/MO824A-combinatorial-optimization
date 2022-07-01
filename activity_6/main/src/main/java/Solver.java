import java.io.IOException;
import main.ProblemInstanceSolver;
import org.json.JSONObject;

public final class Solver extends ProblemInstanceSolver {

    public Solver(
            final int numberOfGenerations,
            final int populationSize,
            final double mutationRate,
            final String problemInstance
    ) throws IOException {
        super(
            numberOfGenerations,
            populationSize,
            mutationRate,
            problemInstance
        );
    }

    public void log() {
        System.out.println(this.getLogAsJson().toString(2));
    }

    public JSONObject getLogAsJson() {
        final var obj = new JSONObject();
        obj.put("Setup", this.getSetup());
        obj.put("Solution", this.getSolution());
        obj.put("RunningTime", (double) this.totalTime / 1000.0);
        obj.put("iterations", this.ga.logOfIterations);
        return obj;
    }

}
