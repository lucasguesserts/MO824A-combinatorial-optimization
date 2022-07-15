import numpy as np
import numpy.typing as npt
import networkx as nx
import matplotlib.pyplot as plt
from .InstanceGeneratorParameters import InstanceGeneratorParameters


class Problem:
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
        self.name = f"N{self.graph.number_of_nodes()}_E{self.graph.number_of_edges()}_W{np.round(np.average(self.capacity)).astype(np.int_)}"
        return

    def plot(self, file_name: str = "foo.png") -> None:
        """Plot into a figure the input graph"""
        plt.subplot(111)
        nx.draw_shell(self.graph, with_labels=True, node_color="white")
        plt.savefig(file_name)
        plt.close()

    def to_dict(self) -> dict:
        data = {}
        data["number_of_nodes"] = len(self.graph.nodes)
        data["capacity"] = self.capacity.tolist()
        data["weights"] = np.transpose(self.weights).tolist()
        data["edges"] = list(self.graph.edges)
        data["parameters"] = self.parameters.to_dict()
        return data

    @staticmethod
    def from_dict(data: dict):
        graph = nx.DiGraph()
        graph.add_nodes_from(range(data["number_of_nodes"]))
        graph.add_edges_from(data["edges"])
        capacity = np.array(data["capacity"], dtype=np.int_)
        weights = np.transpose(np.array(data["weights"], dtype=np.int_))
        parameters = InstanceGeneratorParameters.from_dict(data["parameters"])
        return Problem(graph, capacity, weights, parameters)
