package graph;

final class NodeValuePair<T> {

    public final Integer node;
    public final T value;

    public NodeValuePair(final Integer node, final T value) {
        this.node = node;
        this.value = value;
        return;
    }

}
