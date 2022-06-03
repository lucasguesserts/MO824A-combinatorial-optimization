package problem;

import java.util.Collection;

import objectiveFunction.ObjectiveFunction;
import solutions.Solution;

public interface Problem<E, V>  extends ObjectiveFunction<E, V>, Solution<E> {

    V getCost();
    Problem<E, V> clone();
    public Boolean isValid(final Collection<E> elementsToAdd, final Collection<E> elementsToRemove);

}
