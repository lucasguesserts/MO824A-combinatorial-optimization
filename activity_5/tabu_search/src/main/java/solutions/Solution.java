package solutions;

import java.util.List;

public interface Solution<E, V> {

    public List<E> getElements();
    public V getCost();
    public void add(final E element, final V costIncrement);
    public void remove(final E element, final V costIncrement);
    public Solution<E, V> clone();
    public String toString();

}

