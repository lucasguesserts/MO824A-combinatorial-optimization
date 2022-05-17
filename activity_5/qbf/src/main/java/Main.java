import java.io.IOException;
import java.util.Arrays;

import problems.qbf.QBF;

class Main {
    public static void main(String[] args) throws IOException {

        QBF qbf = new QBF("../instances/qbf/qbf040");
        qbf.printMatrix();
        Double maxVal = Double.NEGATIVE_INFINITY;

        // evaluates randomly generated values for the domain, saving the best
        // one.
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < qbf.getDomainSize(); j++) {
                if (Math.random() < 0.5)
                    qbf.variables[j] = 0.0;
                else
                    qbf.variables[j] = 1.0;
            }
            //System.out.println("x = " + Arrays.toString(qbf.variables));
            Double eval = qbf.evaluateQBF();
            //System.out.println("f(x) = " + eval);
            if (maxVal < eval)
                maxVal = eval;
        }
        System.out.println("maxVal = " + maxVal);

        // evaluates the zero array.
        for (int j = 0; j < qbf.getDomainSize(); j++) {
            qbf.variables[j] = 0.0;
        }
        System.out.println("x = " + Arrays.toString(qbf.variables));
        System.out.println("f(x) = " + qbf.evaluateQBF());

        // evaluates the all-ones array.
        for (int j = 0; j < qbf.getDomainSize(); j++) {
            qbf.variables[j] = 1.0;
        }
        System.out.println("x = " + Arrays.toString(qbf.variables));
        System.out.println("f(x) = " + qbf.evaluateQBF());
    }
}
