import pandas as pd
import json

data_dir = "./experiments/medium/"
output_file = "./report/results/medium.tex"
data_file_name_list = [
    "ilp_solve.json",
    "greedy_solve.json",
    "grasp_solve.json",
    "tabu_solve.json",
]

def load_dataframe(file_name):
    with open(file_name, "r") as file:
        data = json.load(file)
    metaheuristic_name = get_metaheuristic_name(file_name)
    data = {
        "metaheuristic": metaheuristic_name,
        "problem": map(lambda x: x["problem"], data),
        "cost": map(lambda x: x["cost"], data),
        "time[s]": map(lambda x: x["runningTime"], data),
    }
    df = pd.DataFrame(data)
    df = df.set_index(["problem","metaheuristic"]).unstack().swaplevel(0,1,1).sort_index(1)
    return df

def add_metadata(df):
    problem_data_df = pd.DataFrame()
    problem_data_df["problem"] = df.index
    problem_data_df["name"] = len(df.index) * ["problem_info"]
    for column_name, metadata_name in zip(["capacity", "edges", "nodes"], ["average_capacity", "number_of_edges", "number_of_nodes"]):
        problem_data_df[column_name] = list(map(lambda x: get_metadata(x)[metadata_name], df.index))
    problem_data_df = problem_data_df.set_index(["problem","name"]).unstack().swaplevel(0,1,1).sort_index(1)
    df = pd.concat([problem_data_df, df], axis=1)
    return df

def init_file(file):
    print(r"\begin{landscape}", file=file)
    print("", file=file)

def finish_file(file):
    print(r"\end{landscape}", file=file)

def save_table(df, file):
    number_of_nodes = str(df[("problem_info", "nodes")][0])
    print(r"\begin{table}", file=file)
    print(r"\centering", file=file)
    print(df.to_latex(), end="", file=file)
    print(r"\caption{Cost and running time of all metaheuristics for problem instances with " + number_of_nodes + r" nodes.}", file=file)
    print(r"\label{table:" + number_of_nodes + r"-results}", file=file)
    print(r"\end{table}", file=file)
    print("", file=file)

def get_metadata(problem_name: str):
    # N1000_E2405_W373757
    parts = problem_name.split("_")
    number_of_nodes = int(parts[0].replace("N", ""))
    number_of_edges = int(parts[1].replace("E", ""))
    average_capacity = int(parts[2].replace("W", ""))
    return {
        "number_of_nodes": number_of_nodes,
        "number_of_edges": number_of_edges,
        "average_capacity": average_capacity,
    }

def get_metaheuristic_name(file_name):
    return file_name.split("/")[-1].split(".")[0].split("_")[0]

def split_by_number_of_nodes(df):
    dfs = [x for _, x in df.groupby(df["problem_info", "nodes"])]
    for x in dfs:
        x.sort_values(by=[('problem_info', 'edges')], inplace=True)
    return dfs


if __name__ == "__main__":
    df = None
    for data_file_name in data_file_name_list:
        input_file_name = data_dir + data_file_name
        if df is None:
            df = load_dataframe(input_file_name)
        else:
            other = load_dataframe(input_file_name)
            # other.drop(["nodes", "edges", "capacity"], axis=1, inplace=True)
            df = df.join(other)
    df = add_metadata(df)
    dfs = split_by_number_of_nodes(df)
    with open(output_file, "w") as file:
        init_file(file)
        for x in dfs:
            save_table(x, file)
        finish_file(file)
