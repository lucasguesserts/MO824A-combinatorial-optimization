from typing import Iterable
import numpy as np
import numpy.typing as npt


class Solution(set):
    def add(self, element: int) -> None:
        if (type(element) != int) or (element < 0):
            raise ValueError(
                f"a solution only accepts non-negative integers but got '{element}' of type {type(element).__name__}"
            )
        set.add(self, element)

    def add_all(self, elements: Iterable[int]) -> None:
        for element in elements:
            self.add(element)

    def to_array(self, size: int) -> npt.NDArray[np.int_]:
        maxElement = max(self)
        if maxElement >= size:
            raise ValueError(f"size '{size}' must be larger than the maximum element '{maxElement}' the solution")
        array = np.zeros(size).astype(np.int_)
        for element in self:
            array[element] = np.int_(1)
        return array

    def to_dict(self) -> dict:
        data = {}
        data["solution"] = list(self)
        return data

    @staticmethod
    def from_dict(data: dict):
        solution = Solution()
        solution.add_all(data["solution"])
        return solution
