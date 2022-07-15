import os
from MUPKP import IlpSolver, Problem, FileManipulation

instances_dir = "./instances"

instance_list = os.listdir(instances_dir)
instance_list = list(filter(lambda f: ".json" in f, instance_list))
instance_list = list(map(lambda f: f"{instances_dir}/{f}", instance_list))

for instance in instance_list:
    try:
        print("\n\n======================")
        problem = Problem.from_dict(FileManipulation.json_to_dict(instance))
        print(f"Integer Linear Programming - solution of the problem {problem.name}:")
        ilpSolver = IlpSolver(problem, log=True)
        print(f"running time: {ilpSolver.get_running_time_seconds()}")
        print(ilpSolver.get_solution())
    except Exception as e:
        print(f"\n\n!!!!!!!!!\nerror processing instance: {instance}\n\n")
