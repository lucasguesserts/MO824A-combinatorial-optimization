import numpy as np
import numpy.typing as npt
import networkx as nx
import matplotlib.pyplot as plt

class InstanceGenerator:

    def __init__(
        self,
        number_of_nodes: int = 10,
        edge_probability: float = 0.5,
        weight_size: int = 2,
        percentage_of_nodes_to_fit: float = 0.5,
        number_of_digits_to_round: int = 3,
    ):
        """
        Parameters
        ----------
        number_of_nodes : int
            Number of nodes of the induced graph
        edge_probability: float
            For each edge, the probability that it is generated.
            - value 1.0: all edges are generated
            - value 0.0: no edge is generated
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
            If `number_of_nodes` is not greater than 1 (one)
            If `edge_probability` is not in the interval [0, 1]
                (between 0 (zero) (inclusive) and 1 (one) (inclusive))
            If `weight_size` is smaller or equal to 0 (zero)
            If `percentage_of_nodes_to_fit` is not in the interval ]0, 1[
                (between 0 (zero) (exclusive) and 1 (one) (exclusive))
            If `number_of_digits_to_round`  is smaller or equal to 0 (zero)
        """
        self.number_of_nodes = number_of_nodes
        self.edge_probability = edge_probability
        self.weight_size = weight_size
        self.percentage_of_nodes_to_fit = percentage_of_nodes_to_fit
        self.number_of_digits_to_round = number_of_digits_to_round
        self._validate_parameters()

    def generate(self) -> tuple[nx.DiGraph, npt.NDArray[np.int_], npt.NDArray[np.int_]]:
        graph = self._generate_randomized_DAG()
        capacity, weights = self._generate_randomized_weights_and_capacity()
        return (graph, capacity, weights)

    @staticmethod
    def display_graph(graph: nx.DiGraph, file_name: str="foo.png") -> None:
        """Plot into a figure the input graph"""
        plt.subplot(111)
        nx.draw_shell(
            graph,
            with_labels=True,
            node_color = "white"
        )
        plt.savefig(file_name)
        plt.close()


    def _validate_parameters(self) -> None:
        if self.number_of_nodes <= 1:
            raise ValueError("number_of_nodes must be greater than 1 (one)")
        if not 0 <= self.edge_probability <= 1:
            raise ValueError("edge_probability must be a value between 0 (inclusive) and 1 (inclusive)")
        if self.weight_size <= 0:
            raise ValueError("weight_size must must be greater than 0 (zero)")
        if not 0 < self.percentage_of_nodes_to_fit < 1:
            raise ValueError("percentage_of_nodes_to_fit must be a value between 0 (zero) (exclusive) and 1 (one) (exclusive)")
        if self.number_of_digits_to_round <= 0:
            raise ValueError("number_of_digits_to_round must must be greater than 0 (zero)")

    def _generate_randomized_adjacent_matrix(self) -> npt.NDArray[np.bool_]:
        """
        Generate a random adjacent matrix of a DAG.

        Generate a random binary lower triangular
        matrix with all entries of the main diagonal null (zero),
        which defines a Directed Acyclic Graph (DAG).

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
        random_matrix = np.random.rand(self.number_of_nodes, self.number_of_nodes)
        random_binary_matrix = (random_matrix <= self.edge_probability).astype(np.int8)
        random_binary_triangular_matrix = np.triu(
            m = random_binary_matrix,
            k = +1
        )
        return random_binary_triangular_matrix

    def _generate_randomized_DAG(self) -> nx.DiGraph:
        """
        Generate a random Directed Acyclic Graph (DAG).

        Returns
        -------
        networkx.DiGraph
            A Directed Acyclic Graph with `number_of_nodes`
            nodes and edges chosen randomly.
        """
        adjacent_matrix = self._generate_randomized_adjacent_matrix()
        dag = nx.from_numpy_array(adjacent_matrix, create_using=nx.DiGraph)
        assert nx.is_directed_acyclic_graph(dag), f"something is odd, {self._generate_randomized_DAG.__name__} did not generate a directed acyclic graph"
        return dag

    def _generate_randomized_weights_and_capacity(self) -> tuple[npt.NDArray[np.int_], npt.NDArray[np.int_]]:
        """
        Generate weights for the nodes and capacities for the knapsack.

        Generate a vector of integers of size `weight_size`
        for each node of a graph.

        Generate a vector of integers of size `weight_size`
        which is the knapsack capacity.

        Returns
        -------
        tuple[npt.NDArray[np.int_], npt.NDArray[np.int_]]
            The first entry is a vector of size `weight_size`
            which is the capacity of the knapsack.
            The second entry is a matrix of shape
            `(number_of_nodes, weight_size)` which is the
            weight of each node.
        """
        randomized_float_weights = np.random.rand(self.number_of_nodes, self.weight_size) # row: node, column: weight_size
        randomized_weights = np.round(10**self.weight_size * randomized_float_weights).astype(np.int_)
        capacity = np.round(self.percentage_of_nodes_to_fit * np.sum(randomized_weights, axis=0)).astype(np.int_)
        return (capacity, randomized_weights)
