import numpy as np
from MUPKP import IlpSolver, BruteForceSolver, Problem, Solution, SolutionVerifier, FileManipulation

problem_file_name = "./instances/instance_1.json"

problem = Problem.from_dict(FileManipulation.json_to_dict(problem_file_name))

ilpSolver = BruteForceSolver(problem)
print(f"\nBrute - solution to the problem {problem.name}:")
print(ilpSolver.get_solution_list())

ilpSolver = IlpSolver(problem)
print(f"\nInteger Linear Programming - solution to the problem {problem.name}:")
print(ilpSolver.get_solution_list())
