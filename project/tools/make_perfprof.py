from math import inf
from MUPKP import ExperimentResult
import numpy as np
import matplotlib.pyplot as plt
import perfprof

optimality_tolerance = 0.05
experiments = "medium"
output_dir = "./report/images/performance_profile/"

data_dir = f"./experiments/{experiments}/"
data_file_name_list = [
    "ilp_solve.json",
    "greedy_solve.json",
    "grasp_solve.json",
    "tabu_solve.json",
]
data_file_name_list = map(lambda s: data_dir + s, data_file_name_list)

results = list(map(ExperimentResult, data_file_name_list))
table = results[0].df
for x in results[1:]:
    table = table.join(x.df)

cost_entries = list(map(lambda x: (x, "cost"), ExperimentResult.possible_metaheuristics))
time_entries = list(map(lambda x: (x, "time[s]"), ExperimentResult.possible_metaheuristics))
cost_reference = (1 - optimality_tolerance) * table.loc[:, cost_entries].max(axis=1)
# print(reference)

for c, t in zip(cost_entries, time_entries):
    table[c] = table[c] - cost_reference
    table[c] = table[c] >= 0
    z = table.loc[:,[c, t]].apply(lambda y: y[1] if y[0] else inf, axis=1)
    table[t] = z

# for i in range(0, len(table.columns), 2):
#     c = table.columns[i]
#     t = table.columns[i+1]
#     x = table[c] - cost_reference
#     x = x >= 0
#     for j in range(len(x)):
#         z = table[t].iloc[j] if x[j] else inf
#         table[t].iloc[j] = z

# print(table)
# table.to_csv('foo.csv')

time_data = table[time_entries]
# print(time_data)

for thmax in [5.0, 15.0, None]:

    fig = plt.figure(
        figsize=(7, 6)
    )

    header = list(map(lambda x: x[0], time_data.columns))

    measurements = time_data[time_entries].to_numpy()

    palette = [
        "o-k", # ILP
        "o-r", # GREEDY
        "o-b", # GRASP
        "o-g", # TABU
    ]

    perfprof.perfprof(
        data=measurements,
        linestyle=palette,
        thmax=thmax,
        markersize=4,
        markevery=[0]
    )

    ax = fig.get_axes()[0]

    box = ax.get_position()
    ax.set_position([
        box.x0,
        box.y0 + box.height * 0.1,
        box.width,
        box.height * 0.9
    ])

    # Put a legend below current axis
    ax.legend(
        header,
        loc="upper center",
        bbox_to_anchor=(0.5, -0.12),
        fancybox=True,
        shadow=True,
        ncol=3,
    )

    plt.xlim([1, thmax])
    plt.ylim([0, 1.01])
    plt.xlabel(r'performance ratio $\tau$')
    plt.ylabel(r'cummulative distribution $\rho_s(\tau)$')

    plt.grid(True)

    # plt.tight_layout()

    plt.savefig(output_dir + f"{experiments}_thmax_{thmax}.png")
    plt.close()
