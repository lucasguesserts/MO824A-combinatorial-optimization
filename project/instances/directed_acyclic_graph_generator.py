import numpy as np
import numpy.typing as npt
import networkx as nx
import matplotlib.pyplot as plt

def generate_randomized_adjacent_matrix(
    number_of_vertices: int = 10,
    edge_probability: float = 0.5
) -> npt.NDArray[np.bool_]:
    """
    Generate a random adjacent matrix of a DAG.

    Generate a random binary lower triangular
    matrix with all entries of the main diagonal null (zero),
    which defines a Directed Acyclic Graph (DAG).

    Parameters
    ----------
    number_of_vertices : int
        Number of vertices of the induced graph
    edge_probability: float
        For each edge, the probability that it is generated.
        - value 1.0: all edges are generated
        - value 0.0: no edge is generated

    Raises
    ------
    ValueError
        If the input `number_of_vertices` is not greater than 1 (one)

    Returns
    -------
    numpy.array of np.int8
        A `number_of_vertices` by `number_of_vertices`
        random binary lower triangular array
        with all entries of the main diagonal false
        If the entry `m[i][j]` of the array `m` is True,
        then there is an edge from the vertex `i` to the
        vertex `j`.
    """
    if number_of_vertices <= 1:
        raise ValueError("number_of_vertices must be greater than 1 (one)")
    if not 0 <= edge_probability <= 1:
        raise ValueError("edge_probability must be a value between 0 (inclusive) and 1 (inclusive)")
    random_matrix = np.random.rand(number_of_vertices, number_of_vertices)
    random_binary_matrix = (random_matrix <= edge_probability).astype(np.int8)
    random_binary_triangular_matrix = np.triu(
        m = random_binary_matrix,
        k = +1
    )
    print(random_binary_triangular_matrix)
    return random_binary_triangular_matrix

def generate_randomized_DAG(
    number_of_vertices: int = 10,
    edge_probability: float = 0.5
) -> nx.DiGraph:
    """
    Generate a random Directed Acyclic Graph (DAG).

    Parameters
    ----------
    number_of_vertices : int
        Number of vertices of the induced graph
    edge_probability: float
        For each edge, the probability that it is generated.
        - value 1.0: all edges are generated
        - value 0.0: no edge is generated

    Raises
    ------
    ValueError
        If the input `number_of_vertices` is not greater than 1 (one)

    Returns
    -------
    networkx.DiGraph
        A Directed Acyclic Graph with `number_of_vertices`
        vertices and edges chosen randomly.
    """
    adjacent_matrix = generate_randomized_adjacent_matrix(number_of_vertices, edge_probability)
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
