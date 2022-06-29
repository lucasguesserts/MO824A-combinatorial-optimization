from typing import Iterable


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
