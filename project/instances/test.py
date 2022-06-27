from directed_acyclic_graph_generator import *

dag = generate_randomized_DAG(
    number_of_vertices = 6,
    edge_probability = 0.5)
display_graph(dag)

