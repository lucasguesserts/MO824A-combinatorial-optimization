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
                "instances": Extractor.__find_all(lines, '^instance = ../instances/kqbf/kqbf(\d+)$'),
                "local search": Extractor.__transform_local_search(Extractor.__find_all(lines, '^localSearch = (\w+)$')),
                "tenure ratio": Extractor.__find_all(lines, '^tenureRatio = (\d+.?\d*)$'),
                "method variation": Extractor.__transform_method_variation(Extractor.__find_all(lines, '^methodVariation = (\w+)$')),
                "running time": Extractor.__find_all(lines, '^runningTime = (\d+.?\d*) seconds$'),
                "best cost": Extractor.__find_all(lines, '^bestProblemSolution = SolutionCost {cost: -(\d+)'),
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
    def __transform_local_search(found_cases):
        transformation = list(map(
            lambda c: c.replace("_IMPROVING", "").lower(),
            found_cases
        ))
        return transformation

    @staticmethod
    def __transform_method_variation(found_cases):
        transformation = list(map(
            lambda c: "intensification" if (c=="INTENSIFICATION_BY_RESTART") else ("diversification" if (c=="INTENSIFICATION_BY_RESTART_AND_DIVERSIFICATION_BY_RESTART") else "default"),
            found_cases
        ))
        return transformation

    @staticmethod
    def print_table(df, label_number):
        print(r"\begin{table}")
        print(r"\centering")
        print(df.to_latex(), end="")
        print(r"\caption{Solução obtida para cada configuração e instância do problema - parte " + str(label_number) + ".}")
        print(r"\label{table:all-data-" + str(label_number) + "}")
        print(r"\end{table}")
        print("")

    @staticmethod
    def get_parts(length, parts_size):
        return list(zip(
            range(0, length, parts_size),
            range(parts_size-1, length+1, parts_size),
        ))



if __name__ == "__main__":
    filePath = "../experiments/2022-05-23.experiment_result.txt"
    df = Extractor.getDataFrame(filePath)
    parts_argument = 12
    print(r"\begin{landscape}", "\n")
    enumerated_parts = list(enumerate(Extractor.get_parts(len(df), parts_argument)))
    for i, [begin, end] in enumerated_parts:
        Extractor.print_table(df.loc[begin:end], i)
    print(r"\end{landscape}")

