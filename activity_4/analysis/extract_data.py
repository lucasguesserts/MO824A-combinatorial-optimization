import math
import re
import distutils.util

import pandas as pd


class Extractor ():
    @staticmethod
    def getDataFrame(filePath):
        with open(filePath) as file:
            lines = list(map(lambda x: str(x), file.readlines()))
            df = pd.DataFrame({
                "instances": Extractor.__find_all(lines, '^instance: ../instances/kqbf/kqbf(\d+)$'),
                "construction": Extractor.__transform_construction(Extractor.__find_all(lines, '^construction mechanist: (\w+)$')),
                "alpha": Extractor.__find_all(lines, '^alpha: (\d+.?\d*|-)$'),
                "local search": Extractor.__transform_local_search(Extractor.__find_all(lines, '^firstImproving: (true|false)$')),
                "iterations": Extractor.__find_all(lines, '^iterations: (\d+)$'),
                "best cost": Extractor.__find_all(lines, '^Best Solution Found: \{cost: (\d+)'),
                "weight": Extractor.__find_all(lines, '^Knapsack Weight of Best Solution: (\d+)'),
                "duration": Extractor.__find_all(lines, '^RunningTime: (\d+.?\d*) seconds$'),
            })
        return df

    @staticmethod
    def __find_all(l, regex):
        out = []
        for i in l:
            found = re.search(regex, i)
            if (found):
                out.append(found)
        return list(map(lambda s: s.group(1), out))

    @staticmethod
    def __transform_construction(found_cases):
        transformation = list(map(
            lambda c: c.lower().replace("_", " "),
            found_cases
        ))
        return transformation

    @staticmethod
    def __transform_local_search(found_cases):
        cases = list(map(
            distutils.util.strtobool,
            found_cases
        ))
        transformation = list(map(
            lambda b: "first improving" if b else "best improving",
            cases
        ))
        return transformation

    @staticmethod
    def print_table(df, label_number):
        print(r"\begin{table}")
        print(r"\centering")
        print(df.to_latex(), end="")
        print(r"\caption{Número de possíveis soluções para cada tamanho de problema - parte " + str(label_number) + ".}")
        print(r"\label{table:all-data-" + str(label_number) + "}")
        print(r"\end{table}")
        print("")

    # @staticmethod
    # def get_parts(length, number_of_parts):
    #     previous_part = 0
    #     for part in range(number_of_parts-1):
    #         next_part = math.floor(length * ((part+1) / number_of_parts)) - 1
    #         yield [previous_part, next_part]
    #         previous_part = next_part + 1
    #     next_part = length-1
    #     yield [previous_part, next_part]

    @staticmethod
    def get_parts(length, parts_size):
        return list(zip(
            range(0, length, parts_size),
            range(parts_size-1, length+1, parts_size),
        ))



if __name__ == "__main__":
    filePath = "../experiments/01.experiment_result.txt"
    df = Extractor.getDataFrame(filePath)
    parts_argument = 8
    print(r"\begin{landscape}", "\n")
    enumerated_parts = list(enumerate(Extractor.get_parts(len(df), parts_argument)))
    for i, [begin, end] in enumerated_parts:
        Extractor.print_table(df.loc[begin:end], i)
    print(r"\end{landscape}")

