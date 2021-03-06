import sys
from MUPKP import IlpSolver, BruteForceSolver, Problem, Solution, SolutionVerifier, FileManipulation

print(sys.argv)

default_problem_file_name = "./instances/small_examples/instance_1.json"
problem_file_name = sys.argv[1] if len(sys.argv) == 2 else default_problem_file_name

problem = Problem.from_dict(FileManipulation.json_to_dict(problem_file_name))
print(f"\nproblem: {problem.name}")
print(f"from file: {problem_file_name}")

bruteForceSolver = BruteForceSolver(problem)
print(f"\nBrute Force - solution to the problem {problem.name}:")
print(f"running time: {bruteForceSolver.get_running_time_seconds()}")
print(bruteForceSolver.get_solution())
print(bruteForceSolver.get_solution_ordered_list())

ilpSolver = IlpSolver(problem)
print(f"\nInteger Linear Programming - solution to the problem {problem.name}:")
print(f"running time: {ilpSolver.get_running_time_seconds()}")
print(ilpSolver.get_solution())

print()
