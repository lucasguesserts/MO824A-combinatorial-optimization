package solutions;

import java.util.Collection;

public interface Solution<E> {

    public Collection<E> getElements();
    public Boolean isValidCandidate(final E element);
    public void add(final E element);
    public void remove(final E element);
    public void reset();
    public String toString();

}

