import java.io.IOException;

import SolutionCost.SolutionCostInteger;

class Main {
    public static void main(String[] args) throws IOException {
        final String INSTANCE = "../instances/qbf/qbf040";

        {
            final SolutionCostInteger solution = new SolutionCostInteger(INSTANCE);
            SolutionCostInteger bestSolution = solution.clone();
            for (Integer i = 0; i < 100000; i++) {
                for (Integer j = 0; j < solution.getDomainSize(); j++) {
                    if (Math.random() < 0.5) {
                        solution.add(j);
                    }
                }
                // System.out.println(String.format("iteration %d", i));
                if (bestSolution.getCost() < solution.getCost())
                    bestSolution = solution.clone();
                solution.reset();
            }
            System.out.println(String.format(
                "Best solution found: \n\t%s",
                bestSolution.toString()
            ));
        }

        { // evaluates the all-ones array.
            final SolutionCostInteger solution = new SolutionCostInteger(INSTANCE);
            for (Integer j = 0; j < solution.getDomainSize(); j++) {
                solution.add(j);
            }
            System.out.println(String.format(
                "All ones solution: \n\t%s",
                solution.toString()
            ));
        }
    }
}
