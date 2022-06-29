import itertools
import numpy as np
from MUPKP import BruteForceSolver, Problem, Solution, SolutionVerifier, FileManipulation

instance_file_name = "./instances/instance_1.json"

instance = Problem.from_dict(FileManipulation.json_to_dict(instance_file_name))

s = Solution(instance.graph.number_of_nodes())

# s.add(1)
# print(s)

s.add_all(instance.graph.nodes)
print(s)

# # error
# s.addAll([4, "a"])
# print(s)

# x = s.to_array(3)
# x = s.to_array()
# print(x)

xx = np.array([0, 1, 1, 0, 0])
A = np.array(
    [[812, 395, 414, 869, 425], [830, 224, 384, 706, 607], [631, 59, 753, 896, 680]]
)
print(A)
print(instance.capacity)
print(A @ xx)

s.clear()
for i in range(5):
    if xx[i] == 1:
        s.add(i)

FileManipulation.dict_to_json(s.to_dict(), "foo.json")
other = Solution.from_dict(FileManipulation.json_to_dict("foo.json"))
print(other)

verifier = SolutionVerifier(instance)
print(verifier.verify(s))

solver = BruteForceSolver(instance)
print(solver.solutions)
