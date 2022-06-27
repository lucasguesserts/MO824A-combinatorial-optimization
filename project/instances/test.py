import numpy as np
from directed_acyclic_graph_generator import *

dag = generate_randomized_DAG(
    number_of_nodes = 6,
    edge_probability = 0.5)
display_graph(dag)

W, w = generate_randomized_weights_and_capacity(
    number_of_nodes = 10,
    weight_size = 3,
    percentage_of_nodes_to_fit = 0.5,
    number_of_digits_to_round = 3,
)

print(f"weights:\n{w}")
print()
print(f"weights shape:\n{w.shape}")
print()
print(f"sum of all weights: {np.sum(w, axis=0)}")
print(f"weight capacity: {W}")

