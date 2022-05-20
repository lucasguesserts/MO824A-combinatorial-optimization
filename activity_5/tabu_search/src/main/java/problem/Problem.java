package problem;

import objectiveFunction.ObjectiveFunction;
import solutions.Solution;

public interface Problem<E, V>  extends ObjectiveFunction<E, V>, Solution<E> {

    V getCost();
    Problem<E, V> clone();

}
