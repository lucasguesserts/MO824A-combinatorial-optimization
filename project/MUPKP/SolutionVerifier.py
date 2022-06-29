import numpy as np
import networkx as nx
from .Solution import Solution
from .ProblemInstance import ProblemInstance


class SolutionVerifier:
    def __init__(self, problem: ProblemInstance):
        self.problem = problem

    def verify(self, solution: Solution) -> bool:
        weight = self._verify_weight_constraint(solution)
        precedence = self._verify_precedence_constraint(solution)
        return weight and precedence

    def _verify_weight_constraint(self, solution: Solution) -> bool:
        number_of_nodes = self.problem.graph.number_of_nodes()
        if solution.size != number_of_nodes:
            raise ValueError(
                f"solution.size {solution.size} and number_of_nodes {number_of_nodes} differ"
            )
        solution_vector = solution.to_array()
        weight_matrix = self.problem.weights
        total_weight = np.matmul(weight_matrix, solution_vector)
        is_weight_limit_satisfied = np.all(total_weight <= self.problem.capacity)
        return is_weight_limit_satisfied

    def _verify_precedence_constraint(self, solution: Solution) -> bool:
        graph = self.problem.graph
        reverse_graph = graph.reverse()
        for node in graph.nodes():
            if node in solution:
                predecessors = set(nx.dfs_postorder_nodes(reverse_graph, node))
                if not predecessors.issubset(solution):
                    return False
        return True
