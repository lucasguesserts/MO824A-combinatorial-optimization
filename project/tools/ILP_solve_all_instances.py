import json
import math
import os
import sys
from MUPKP import IlpSolver, Problem, FileManipulation

instances_dir = sys.argv[1]

instance_list = os.listdir(instances_dir)
instance_list = list(filter(lambda f: ".json" in f, instance_list))
instance_list = list(map(lambda f: f"{instances_dir}/{f}", instance_list))

logs = []

for instance in instance_list:
    try:
        problem = Problem.from_dict(FileManipulation.json_to_dict(instance))
        solver = IlpSolver(problem)
        logs.append({
            "problem": problem.name,
            "cost": len(solver.get_solution()),
            "runningTime": solver.get_running_time_seconds(),
        })
    except Exception as e:
        logs.append({
            "problem": problem.name,
            "cost": math.inf,
            "runningTime": math.inf,
            "error": e
        })

with open("ilp_solve.json", "w") as file:
    json.dump(logs, file, indent=4)
