import numpy as np
import networkx as nx
from directed_acyclic_graph_generator import InstanceGenerator

generator = InstanceGenerator(
    number_of_nodes = 10,
    edge_probability = 0.5,
    weight_size = 3,
    percentage_of_nodes_to_fit = 0.5,
    number_of_digits_to_round = 3,
)

dag, W, w = generator.generate()

InstanceGenerator.display_graph(dag)

print(f"weights:\n{w}")
print()
print(f"weights shape:\n{w.shape}")
print()
print(f"sum of all weights: {np.sum(w, axis=0)}")
print(f"weight capacity: {W}")

