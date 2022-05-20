package SolutionCost;

import objectiveFunction.ObjectiveFunction;
import solutions.Solution;

public interface SolutionCost<E, V>  extends ObjectiveFunction<E, V>, Solution<E> {

    V getCost();
    SolutionCost<E, V> clone();

}
