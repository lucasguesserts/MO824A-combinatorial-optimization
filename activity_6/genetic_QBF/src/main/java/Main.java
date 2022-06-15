import java.io.IOException;
import problems.qbf.solvers.GaQbf;
import solutions.Solution;

public class Main {

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
