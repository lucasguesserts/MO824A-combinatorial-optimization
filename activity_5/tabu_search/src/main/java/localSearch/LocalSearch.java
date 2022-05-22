package localSearch;

import java.util.Collection;

import problem.Problem;

public interface LocalSearch<E, V> {

    Problem<E, V> getLocalOptimal();
    Collection<E> getElementsManipulated();

}
