import pandas as pd
import numpy as np
import json

data_dir = "experiments/medium/"
data_file_name_list = [
    "greedy_solve.json",
    "grasp_solve.json",
    "tabu_solve.json",
]

def load_dataframe(file_name):
    with open(file_name, "r") as file:
        data = json.load(file)
    data = {
        "problems": map(lambda x: x["problem"], data),
        "cost": map(lambda x: x["cost"], data),
        "running time [s]": map(lambda x: x["runningTime"], data),
    }
    df = pd.DataFrame(data)
    return df

def save_table(df, output_file):
    with open(output_file, "w") as file:
        metaheuristic = get_metaheuristic(output_file)
        print(r"\begin{table}",file=file)
        print(r"\centering",file=file)
        print(df.to_latex(), end="",file=file)
        print(r"\caption{Cost and running time of the " + metaheuristic + r" metaheuristic for several problems.}",file=file)
        print(r"\label{table:" + metaheuristic + r"-results}",file=file)
        print(r"\end{table}",file=file)
        print("",file=file)

def get_metaheuristic(file_name):
    return file_name.split("/")[-1].split(".")[0].split("_")[0]

if __name__ == "__main__":
    for data_file_name in data_file_name_list:
        input_file_name = data_dir + data_file_name
        output_file_name = input_file_name.replace("json", "tex")
        df = load_dataframe(input_file_name)
        save_table(df, output_file_name)
