import java.io.IOException;

import problems.qbf.QBF;
import solutions.Solution;
import solutions.SolutionInteger;

class Main {
    public static void main(String[] args) throws IOException {
        QBF qbf = new QBF("../instances/qbf/qbf040");

        {
            System.out.println(qbf.toString());
            Integer maxVal = Integer.MIN_VALUE;
            final Solution<Integer, Integer> solution = new SolutionInteger(0);
            for (Integer i = 0; i < 1000; i++) {
                for (Integer j = 0; j < qbf.getDomainSize(); j++) {
                    if (Math.random() < 0.5) {
                        final var insertionCost = qbf.evaluateInsertionCost(j, solution);
                        solution.add(j, insertionCost);
                    }
                }
                if (maxVal < solution.getCost())
                    maxVal = solution.getCost();
            }
            System.out.println("maxVal = " + maxVal);
        }

        { // evaluates the all-ones array.
            final Solution<Integer, Integer> solution = new SolutionInteger(0);
            for (Integer j = 0; j < qbf.getDomainSize(); j++) {
                final Integer insertionCost = qbf.evaluateInsertionCost(j, solution);
                solution.add(j, insertionCost);
            }
            System.out.println("x = " + solution.getElements().toString());
            System.out.println("f(x) = " + solution.getCost());
        }
    }
}
