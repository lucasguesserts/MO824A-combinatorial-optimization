from typing import Iterable
import itertools

from .Problem import Problem
from .Solution import Solution
from .SolutionVerifier import SolutionVerifier


class BruteForceSolver:
    def __init__(self, problem: Problem):
        self.problem = problem
        self._set_all_possible_solutions()
        self._filter_valid_solutions()
        self._find_best_solutions()
        return

    @staticmethod
    def _all_combinations(elements: Iterable) -> list[tuple[int]]:
        size = len(elements)
        acc = []
        for combination_size in range(size):
            acc.extend(itertools.combinations(elements, combination_size))
        return acc

    def _set_all_possible_solutions(self) -> list[Solution]:
        self._all_possible_solutions = []
        number_of_nodes = self.problem.graph.number_of_nodes()
        def make_solution(combination: tuple[int]):
            solution = Solution(number_of_nodes)
            solution.add_all(combination)
            return solution
        all_combinations = BruteForceSolver._all_combinations(range(number_of_nodes))
        self._all_possible_solutions = list(map(make_solution, all_combinations))
        return

    def _filter_valid_solutions(self):
        verifier = SolutionVerifier(self.problem)
        self._valid_solutions = filter(verifier.verify, self._all_possible_solutions)
        return

    def _find_best_solutions(self):
        self.solutions = sorted(self._valid_solutions, key=len, reverse=True)
        return
