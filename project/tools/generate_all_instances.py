import itertools
import numpy as np
from MUPKP import FileManipulation
from MUPKP import RandomizedInstanceGenerator


output_dir = "instances"

number_of_nodes_list            = [500]
edge_probability_list           = [0.001, 0.005, 0.01]
weight_size_list                = [6]
percentage_of_nodes_to_fit_list = [0.30, 0.50, 0.70]
weight_minimum_value            = 0
weight_maximum_value            = 1000

all_combinations = itertools.product(
    number_of_nodes_list,
    edge_probability_list,
    weight_size_list,
    percentage_of_nodes_to_fit_list,
)

for (nn, ep, ws, pn) in all_combinations:

    generator = RandomizedInstanceGenerator(
        number_of_nodes=nn,
        edge_probability=ep,
        weight_size=ws,
        percentage_of_nodes_to_fit=pn,
        weight_minimum_value=weight_minimum_value,
        weight_maximum_value=weight_maximum_value,
    )

    instance = generator.generate()
    file_path = f"{output_dir}/{instance.name}.json"
    FileManipulation.dict_to_json(instance.to_dict(), file_path)

    print(f"==========")
    print(f"instance:")
    print(f"\tfile = {file_path}")
    print(f"\tnumber_of_nodes = {nn}")
    print(f"\tedge_probability = {ep}")
    print(f"\tweight_size = {ws}")
    print(f"\tpercentage_of_nodes_to_fit = {pn}")
    print(f"\tweight_minimum_value = {weight_minimum_value}")
    print(f"\tweight_maximum_value = {weight_maximum_value}")
    print()
