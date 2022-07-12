from abc import ABC, abstractmethod
from .Solution import Solution


class Solver(ABC):
    @abstractmethod
    def get_solution(self) -> Solution:
        pass

    @abstractmethod
    def get_solution_list(self) -> list[Solution]:
        pass
