import math

def get_number_of_possibilities(size_of_problem_instaces):
    return [
        math.pow(2, problem_size)
        for problem_size in size_of_problem_instaces
    ]

def print_row(problem_size, number_of_possibilities):
    print(
        "\t{:3} & {:8.1e} \\\\\\hline"
        .format(
            problem_size,
            number_of_possibilities
        ))

if __name__ == "__main__":
    size_of_problem_instaces = [
        20,
        40,
        60,
        80,
        100,
        200,
        400
    ]
    number_of_possibilities = get_number_of_possibilities(size_of_problem_instaces)
    for instance in zip(size_of_problem_instaces, number_of_possibilities):
        print_row(*instance)
