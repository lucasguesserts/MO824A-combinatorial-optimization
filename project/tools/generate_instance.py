import numpy as np
from MUPKP import FileManipulation
from MUPKP import RandomizedInstanceGenerator


default_json_file_name = "foo.json"
default_figure_file_name = "foo.png"

generator = RandomizedInstanceGenerator(
    number_of_nodes=7,
    edge_probability=0.3,
    weight_size=3,
    percentage_of_nodes_to_fit=0.5,
    weight_minimum_value=500,
    weight_maximum_value=1000,
)

instance = generator.generate()
FileManipulation.dict_to_json(instance.to_dict(), default_json_file_name)

instance.plot(default_figure_file_name)

print(f"weights:\n{instance.weights}")
print()
print(f"weights shape:\n{instance.weights.shape}")
print()
print(f"sum of all weights: {np.sum(instance.weights, axis=1)}")
print(f"weight capacity: {instance.capacity}")
