from typing import Iterable
import numpy as np
import numpy.typing as npt


class Solution(set):
    def __init__(self, size: int):
        if (size < 1) and (type(size) == int):
            raise ValueError(
                f"Solution size must be and integer greater or equals to 1, but you provided '{size}' of type '{type(size)}'"
            )
        self.size = size

    def add(self, element: int) -> None:
        if (type(element) != int) or (element < 0):
            raise ValueError(
                f"a solution only accepts non-negative integers but got '{element}' of type {type(element).__name__}"
            )
        if element >= self.size:
            raise ValueError(
                f"Element {element} provided is greater or equals to the solution size '{self.size}'"
            )
        set.add(self, element)

    def add_all(self, elements: Iterable[int]) -> None:
        for element in elements:
            self.add(element)

    def to_array(self) -> npt.NDArray[np.int_]:
        array = np.zeros(self.size).astype(np.int_)
        for element in self:
            array[element] = np.int_(1)
        return array

    def to_dict(self) -> dict:
        data = {}
        data["values"] = list(self)
        data["size"] = self.size
        return data

    @staticmethod
    def from_dict(data: dict):
        solution = Solution(int(data["size"]))
        solution.add_all(data["values"])
        return solution
