import json
import pandas as pd


class Extractor ():
    @staticmethod
    def getDataFrame(filePath):
        with open(filePath) as file:
            data = json.load(file)
            df = pd.DataFrame({
                "instances": map(lambda o: o["Setup"]["domainSize"], data),
                "n. gen": map(lambda o: o["Setup"]["numberOfGenerations"], data),
                "mut rate": map(lambda o: o["Setup"]["mutationRate"], data),
                "pop size": map(lambda o: o["Setup"]["populationSize"], data),
                "k. capac.": map(lambda o: o["Setup"]["knapsackCapacity"], data),
                "sol. w": map(lambda o: o["Solution"]["weight"], data),
                "exec t. [s]": map(lambda o: o["RunningTime"], data),
                "sol. cost": map(lambda o: o["Solution"]["cost"], data),
            })
        return df

    @staticmethod
    def print_table(df):
        print(r"\begin{table}")
        print(r"\centering")
        print(df.to_latex(), end="")
        print(r"\caption{Solução obtida para cada configuração e instância do problema.}")
        print(r"\label{table:all-data}")
        print(r"\end{table}")
        print("")


if __name__ == "__main__":
    filePath = "../experiments/2022-05-16.experiment_result.json"
    df = Extractor.getDataFrame(filePath)
    print(r"\documentclass{article}")
    print(r"\usepackage[utf8]{inputenc}")
    print(r"\usepackage{booktabs}")
    print("")
    print(r"\begin{document}")
    print("")
    Extractor.print_table(df)
    print("")
    print(r"\end{document}")

