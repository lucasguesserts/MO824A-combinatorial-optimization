from typing import Tuple
import numpy as np
import numpy.typing as npt
import networkx as nx

from .InstanceGeneratorParameters import InstanceGeneratorParameters
from .Problem import Problem


class WeightInstanceGenerator(InstanceGeneratorParameters):
    def _generate_randomized_weights_and_capacity(
        self,
    ) -> Tuple[npt.NDArray[np.int_], npt.NDArray[np.int_]]:
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
            `(weight_size, number_of_nodes)` which is the
            weight matrix. Each column represents the multidimensional
            weight of each node.
        """
        weights = self._generate_randomized_weights()
        capacity = self._generate_capacity(weights)
        return (capacity, weights)

    def _generate_randomized_weights(self) -> npt.NDArray[np.int_]:
        weights_shape = [self.weight_size, self.number_of_nodes]
        randomized_float_array = np.random.rand(*weights_shape)
        weight_range = self.weight_maximum_value - self.weight_minimum_value
        randomized_float_weights = self.weight_minimum_value + weight_range * randomized_float_array
        randomized_weights = np.round(randomized_float_weights).astype(np.int_)
        return randomized_weights

    def _generate_capacity(self, weights: npt.NDArray[np.int_]):
        capacity = np.round(
            self.percentage_of_nodes_to_fit * np.sum(weights, axis=1)
        ).astype(np.int_)
        return capacity

class RandomizedInstanceGenerator(WeightInstanceGenerator):
    def generate(self) -> Problem:
        graph = self._generate_randomized_transitive_reduced_DAG()
        capacity, weights = self._generate_randomized_weights_and_capacity()
        instance = Problem(graph, capacity, weights, self)
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
        random_binary_triangular_matrix = np.triu(m=random_binary_matrix, k=+1)
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
        assert nx.is_directed_acyclic_graph(
            dag
        ), f"something is odd, {self._generate_randomized_DAG.__name__} did not generate a directed acyclic graph"
        reduced_dag = nx.transitive_reduction(dag)
        return reduced_dag
