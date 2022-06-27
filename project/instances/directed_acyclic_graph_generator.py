import numpy as np
import numpy.typing as npt
import networkx as nx
import matplotlib.pyplot as plt

def generate_randomized_adjacent_matrix(
    number_of_nodes: int = 10,
    edge_probability: float = 0.5
) -> npt.NDArray[np.bool_]:
    """
    Generate a random adjacent matrix of a DAG.

    Generate a random binary lower triangular
    matrix with all entries of the main diagonal null (zero),
    which defines a Directed Acyclic Graph (DAG).

    Parameters
    ----------
    number_of_nodes : int
        Number of nodes of the induced graph
    edge_probability: float
        For each edge, the probability that it is generated.
        - value 1.0: all edges are generated
        - value 0.0: no edge is generated

    Raises
    ------
    ValueError
        If the input `number_of_nodes` is not greater than 1 (one)

    Returns
    -------
    numpy.array of np.int8
        A `number_of_nodes` by `number_of_nodes`
        random binary lower triangular array
        with all entries of the main diagonal false
        If the entry `m[i][j]` of the array `m` is True,
        then there is an edge from the node `i` to the
        node `j`.
    """
    if number_of_nodes <= 1:
        raise ValueError("number_of_nodes must be greater than 1 (one)")
    if not 0 <= edge_probability <= 1:
        raise ValueError("edge_probability must be a value between 0 (inclusive) and 1 (inclusive)")
    random_matrix = np.random.rand(number_of_nodes, number_of_nodes)
    random_binary_matrix = (random_matrix <= edge_probability).astype(np.int8)
    random_binary_triangular_matrix = np.triu(
        m = random_binary_matrix,
        k = +1
    )
    return random_binary_triangular_matrix

def generate_randomized_weights_and_capacity(
    number_of_nodes: int,
    weight_size: int = 2,
    percentage_of_nodes_to_fit: float = 0.5,
    number_of_digits_to_round: int = 3,
) -> tuple[npt.NDArray[np.int_], npt.NDArray[np.int_]]:
    """
    Generate weights for the nodes and capacities for the knapsack.

    Generate a vector of integers of size `weight_size`
    for each node of a graph.

    Generate a vector of integers of size `weight_size`
    which is the knapsack capacity.

    Parameters
    ----------
    number_of_nodes : int
        Number of nodes of the graph
    weight_size : int
        size of the vector of weight
    percentage_of_nodes_to_fit : int
        average number of nodes that we want to
        fit into the knapsack
    number_of_digits_to_round : int
        the number of digits to use in the round function
        of the random number generation

    Raises
    ------
    ValueError
        If the input `weight_size` is smaller or equal to 0 (zero)
        If the input `number_of_nodes` is smaller or equal than 1 (one)
        If the `percentage_of_nodes_to_fit` is not in the interval ]0, 1[
            (between 0 (zero) (exclusive) and 1 (one) (exclusive))
        If the `number_of_digits_to_round`  is smaller or equal to 0 (zero)

    Returns
    -------
    tuple[npt.NDArray[np.int_], npt.NDArray[np.int_]]
        The first entry is a vector of size `weight_size`
        which is the capacity of the knapsack.
        The second entry is a matrix of shape
        `(number_of_nodes, weight_size)` which is the
        weight of each node.
    """
    if weight_size <= 0:
        raise ValueError("weight_size must must be greater than 0 (zero)")
    if number_of_nodes <= 1:
        raise ValueError("number_of_nodes must be greater than 1 (one)")
    if not 0 < percentage_of_nodes_to_fit < 1:
        raise ValueError("percentage_of_nodes_to_fit must be a value between 0 (zero) (exclusive) and 1 (one) (exclusive)")
    if number_of_digits_to_round <= 0:
        raise ValueError("number_of_digits_to_round must must be greater than 0 (zero)")
    randomized_float_weights = np.random.rand(number_of_nodes, weight_size) # row: node, column: weight_size
    randomized_weights = np.round(10**weight_size * randomized_float_weights).astype(np.int_)
    capacity = np.round(percentage_of_nodes_to_fit * np.sum(randomized_weights, axis=0)).astype(np.int_)
    return (capacity, randomized_weights)


def generate_randomized_DAG(
    number_of_nodes: int = 10,
    edge_probability: float = 0.5
) -> nx.DiGraph:
    """
    Generate a random Directed Acyclic Graph (DAG).

    Parameters
    ----------
    number_of_nodes : int
        Number of nodes of the induced graph
    edge_probability: float
        For each edge, the probability that it is generated.
        - value 1.0: all edges are generated
        - value 0.0: no edge is generated

    Raises
    ------
    ValueError
        If the input `number_of_nodes` is not greater than 1 (one)

    Returns
    -------
    networkx.DiGraph
        A Directed Acyclic Graph with `number_of_nodes`
        nodes and edges chosen randomly.
    """
    adjacent_matrix = generate_randomized_adjacent_matrix(number_of_nodes, edge_probability)
    dag = nx.from_numpy_array(adjacent_matrix, create_using=nx.DiGraph)
    assert nx.is_directed_acyclic_graph(dag), f"something is odd, {generate_randomized_DAG.__name__} did not generate a directed acyclic graph"
    return dag

def display_graph(graph: nx.DiGraph, file_name: str="foo.png") -> None:
    """Plot into a figure the input graph"""
    plt.subplot(111)
    nx.draw(
        graph,
        with_labels=True,
        node_color = "white"
    )
    plt.savefig(file_name)
