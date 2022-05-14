import re
import pandas as pd

class Extractor ():
    @staticmethod
    def getDataFrame(filePath):
        with open(filePath) as file:
            lines = list(map(lambda x: str(x), file.readlines()))
            df = pd.DataFrame({
                "instances": Extractor.__find_all(lines, '^instance: ../instances/kqbf/kqbf(\d+)$'),
                "construction_mechanism": Extractor.__find_all(lines, '^construction mechanist: (\w+)$'),
                "alpha": Extractor.__find_all(lines, '^alpha: (\d+.?\d*)$'),
                "first_improving": Extractor.__find_all(lines, '^firstImproving: (true|false)$'),
                "iterations": Extractor.__find_all(lines, '^iterations: (\d+)$'),
                "best_solution_cost": Extractor.__find_all(lines, '^Best Solution Found: \{cost: (\d+)'),
                "knapsack_weight": Extractor.__find_all(lines, '^Knapsack Weight of Best Solution: (\d+)'),
                "processing_times": Extractor.__find_all(lines, '^RunningTime: (\d+.?\d*) seconds$'),
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

if __name__ == "__main__":
    filePath = "../experiments/2022-05-14.experiment_result"
    df = Extractor.getDataFrame(filePath)
    print(df.to_latex())

