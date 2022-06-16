import sys
import json
import matplotlib.pyplot as plt
import numpy as np

def get_data():
    file_path = sys.argv[1]
    with open(file_path) as file:
        data = json.load(file)
    return data

def plot_experiment_results(data):
    for obj in data:
        x = np.array(list(map(lambda it: int(it["iteration"]), obj["iterations"])))
        x = np.append(x, obj["Setup"]["numberOfGenerations"])
        y = np.array(list(map(lambda it: int(it["cost"]), obj["iterations"])))
        y = np.append(y,  obj["Solution"]["cost"])
        y = y / np.max(y)
        label = f"{obj['Setup']['domainSize']}"
        plt.semilogx(x, y, linestyle="-", marker=".", label=label)

def get_max_iterations(data):
    return data[0]["Setup"]["numberOfGenerations"]

def plot_reference_line(data):
    x = np.array([1, get_max_iterations(data)])
    y = np.array([0, 1])
    plt.semilogx(x, y, linestyle="-", color="black", label="ref")

def make_title(data):
    return f'Setup: pop. size: {data[0]["Setup"]["populationSize"]}, m. rate: {data[0]["Setup"]["mutationRate"]}'

def add_descriptive_info(data):
    plt.title(make_title(data))
    plt.legend(loc='best')
    plt.grid(True)

def save_figure():
    figName = f"plot_all.png"
    plt.savefig(figName)

if __name__ == "__main__":
    data = get_data()
    plot_experiment_results(data)
    plot_reference_line(data)
    add_descriptive_info(data)
    save_figure()
