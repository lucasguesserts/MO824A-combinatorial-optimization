import argparse
import numpy as np
from MUPKP import FileManipulation
from MUPKP import RandomizedInstanceGenerator

parser = argparse.ArgumentParser(
    description='Generate a MUPKP problem instance'
)
parser.add_argument(
    "--nn",
    type=int,
    default=10,
    help="number_of_nodes",
    required=False,
)
parser.add_argument(
    "--ep",
    type=float,
    default=0.3,
    help="edge_probability",
    required=False,
)
parser.add_argument(
    "--ws",
    type=int,
    default=3,
    help="weight_size",
    required=False,
)
parser.add_argument(
    "--pn",
    type=float,
    default=0.5,
    help="percentage_of_nodes_to_fit",
    required=False,
)
parser.add_argument(
    "--min",
    type=int,
    default=500,
    help="weight_minimum_value",
    required=False,
)
parser.add_argument(
    "--max",
    type=int,
    default=1000,
    help="weight_maximum_value",
    required=False,
)
parser.add_argument(
    "--file",
    type=str,
    default="foo",
    help="name of the file to save, without extension",
    required=False,
)

args = parser.parse_args()
print(f"\nargs: {args}\n")

generator = RandomizedInstanceGenerator(
    number_of_nodes=args.nn,
    edge_probability=args.ep,
    weight_size=args.ws,
    percentage_of_nodes_to_fit=args.pn,
    weight_minimum_value=args.min,
    weight_maximum_value=args.max,
)

instance = generator.generate()

json_file_name = f"{args.file}.json"
figure_file_name = f"{args.file}.png"
FileManipulation.dict_to_json(instance.to_dict(), json_file_name)
instance.plot(figure_file_name)

print(f"weights:\n{instance.weights}")
print()
print(f"weights shape:\n{instance.weights.shape}")
print()
print(f"sum of all weights: {np.sum(instance.weights, axis=1)}")
print(f"weight capacity: {instance.capacity}")
print()
print(f"instance file: {json_file_name}")
print(f"instance figure: {figure_file_name}")
