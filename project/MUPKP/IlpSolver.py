import gurobipy as gp
from gurobipy import GRB
import numpy as np

from .Problem import Problem
from .Solution import Solution
from .Solver import Solver


class IlpSolver(Solver):
    def __init__(self, problem: Problem, log: bool = False):
        self.problem = problem
        self._create_model(log)
        self._solve_model()

    def get_solution(self) -> Solution:
        return self.solution

    def _solve_model(self) -> None:
        self.model.optimize()
        self._set_solution()
        self.optimalValue = np.int_(self.model.ObjVal)
        return

    def _create_model(self, log: bool) -> None:
        env = gp.Env(empty=True)
        env.setParam("OutputFlag", log)
        env.start()
        self.model = gp.Model(self.problem.name, env)
        self._set_variables()
        self._set_objective_function()
        self._set_precedence_constraint()
        self._set_capacity_constraint()
        return

    def _set_variables(self) -> None:
        self.variables = self.model.addMVar(
            self.problem.graph.number_of_nodes(), vtype=GRB.BINARY
        )
        return

    def _set_objective_function(self) -> None:
        self.model.setObjective(expr=gp.quicksum(self.variables), sense=GRB.MAXIMIZE)
        return

    def _set_precedence_constraint(self) -> None:
        for node in self.problem.graph.nodes():
            predecessorList = self.problem.graph.predecessors(node)
            for predecessor in predecessorList:
                self.model.addLConstr(
                    self.variables[node] <= self.variables[predecessor]
                )
        return

    def _set_capacity_constraint(self) -> None:
        self.model.addMConstr(
            self.problem.weights, self.variables, "<=", self.problem.capacity
        )
        return

    def _set_solution(self) -> None:
        self.solution = Solution(self.model.NumVars)
        for index, value in enumerate(self.model.X):
            if value == 1:
                self.solution.add(index)
        return
