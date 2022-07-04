import numpy as np
import matplotlib.pyplot as plt
import perfprof

for thmax in [5.0, 15.0, None]:

    fig = plt.figure(
        figsize=(7, 6)
    )

    header = np.loadtxt(
        "data.csv",
        skiprows=0,
        delimiter=",",
        max_rows=1,
        dtype=str
    )

    measurements = np.loadtxt(
        "data.csv",
        skiprows=1,
        delimiter=","
    )

    palette = [
        "o-g", "o--g", # GRASP
        "o-b", "o--b", # TABU
        "o-r", "o--r", # GENETIC
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

    plt.savefig(f"performance_profile_thmax_{thmax}.png")
    plt.close()
