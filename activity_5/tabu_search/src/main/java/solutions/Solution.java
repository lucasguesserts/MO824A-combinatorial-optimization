package solutions;

import java.util.List;

public interface Solution<E> {

    public List<E> getElements();
    public void add(final E element);
    public void remove(final E element);
    public void reset();
    public String toString();

}

