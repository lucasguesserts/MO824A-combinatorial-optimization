import numpy as np
import numpy.typing as npt
import networkx as nx
import matplotlib.pyplot as plt
import json

class FileManipulation:

    @staticmethod
    def json_to_dict(
        file_name: str
    ) -> dict:
        with open(file_name, "r") as file:
            data = json.load(file)
        return data

    @staticmethod
    def dict_to_json(
        data: dict,
        file_name: str,
    ) -> None:
        with open(file_name, "w") as file:
            file.write(json.dumps(data, indent=2, ensure_ascii=True))
        return

class InstanceGeneratorParameters:

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
            If `number_of_nodes` is smaller than 2 (two)
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
        return

    def _validate_parameters(self) -> None:
        if self.number_of_nodes < 2:
            raise ValueError("number_of_nodes must be greater than 1 (one)")
        if not 0 <= self.edge_probability <= 1:
            raise ValueError("edge_probability must be a value between 0 (inclusive) and 1 (inclusive)")
        if self.weight_size <= 0:
            raise ValueError("weight_size must must be greater than 0 (zero)")
        if not 0 < self.percentage_of_nodes_to_fit < 1:
            raise ValueError("percentage_of_nodes_to_fit must be a value between 0 (zero) (exclusive) and 1 (one) (exclusive)")
        if self.number_of_digits_to_round <= 0:
            raise ValueError("number_of_digits_to_round must must be greater than 0 (zero)")
        return

    def to_dict(self) -> dict:
        data = {}
        data["number_of_nodes"] = self.number_of_nodes
        data["edge_probability"] = self.edge_probability
        data["weight_size"] = self.weight_size
        data["percentage_of_nodes_to_fit"] = self.percentage_of_nodes_to_fit
        data["number_of_digits_to_round"] = self.number_of_digits_to_round
        return data

    @staticmethod
    def from_dict(data: dict):
        return InstanceGeneratorParameters(
            number_of_nodes = data["number_of_nodes"],
            edge_probability = data["edge_probability"],
            weight_size = data["weight_size"],
            percentage_of_nodes_to_fit = data["percentage_of_nodes_to_fit"],
            number_of_digits_to_round = data["number_of_digits_to_round"],
        )


class ProblemInstance:
    def __init__(
        self,
        graph: nx.DiGraph,
        capacity: npt.NDArray[np.int_],
        weights: npt.NDArray[np.int_],
        parameters: InstanceGeneratorParameters,
    ):
        self.graph = graph
        self.capacity = capacity
        self.weights = weights
        self.parameters = parameters
        return

    def plot(self, file_name: str="foo.png") -> None:
        """Plot into a figure the input graph"""
        plt.subplot(111)
        nx.draw_shell(
            self.graph,
            with_labels=True,
            node_color = "white"
        )
        plt.savefig(file_name)
        plt.close()

    def to_dict(self) -> dict:
        data = {}
        data["capacity"] = self.capacity.tolist()
        data["weights"] = self.weights.tolist()
        data["edges"] = list(self.graph.edges)
        data["parameters"] = self.parameters.to_dict()
        return data

    @staticmethod
    def from_dict(data: dict):
        graph = nx.DiGraph()
        graph.add_nodes_from(range(data["parameters"]["number_of_nodes"]))
        graph.add_edges_from(data["edges"])
        capacity = np.array(data["capacity"], dtype=np.int_)
        weights = np.array(data["weights"], dtype=np.int_)
        parameters = InstanceGeneratorParameters.from_dict(data["parameters"])
        return ProblemInstance(graph, capacity, weights, parameters)


class InstanceGenerator (InstanceGeneratorParameters):

    def generate(self) -> ProblemInstance:
        graph = self._generate_randomized_transitive_reduced_DAG()
        capacity, weights = self._generate_randomized_weights_and_capacity()
        instance = ProblemInstance(graph, capacity, weights, self)
        return instance

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

    def _generate_randomized_transitive_reduced_DAG(self) -> nx.DiGraph:
        """
        Generate a Randomized Transitive Reduced Directed Acyclic Graph (DAG).

        Returns
        -------
        networkx.DiGraph
            A Directed Acyclic Graph with `number_of_nodes`
            nodes and edges chosen randomly.
        """
        adjacent_matrix = self._generate_randomized_adjacent_matrix()
        dag = nx.from_numpy_array(adjacent_matrix, create_using=nx.DiGraph)
        assert nx.is_directed_acyclic_graph(dag), f"something is odd, {self._generate_randomized_DAG.__name__} did not generate a directed acyclic graph"
        reduced_dag = nx.transitive_reduction(dag)
        return reduced_dag

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
