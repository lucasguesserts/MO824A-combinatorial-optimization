import numpy as np
import networkx as nx
from directed_acyclic_graph_generator import *

generator = InstanceGenerator(
    number_of_nodes = 10,
    edge_probability = 0.5,
    weight_size = 3,
    percentage_of_nodes_to_fit = 0.5,
    number_of_digits_to_round = 3,
)

instance = generator.generate()
FileManipulation.dict_to_json(instance.to_dict())
other_instance = ProblemInstance.from_dict(FileManipulation.json_to_dict())

instance.plot()
other_instance.plot("goo.png")

print(f"weights:\n{instance.weights}")
print()
print(f"weights shape:\n{instance.weights.shape}")
print()
print(f"sum of all weights: {np.sum(instance.weights, axis=0)}")
print(f"weight capacity: {instance.capacity}")

