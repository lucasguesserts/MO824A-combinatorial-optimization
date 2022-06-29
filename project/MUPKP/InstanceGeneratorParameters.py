class InstanceGeneratorParameters:
    def __init__(
        self,
        number_of_nodes: int = 10,
        edge_probability: float = 0.5,
        weight_size: int = 2,
        percentage_of_nodes_to_fit: float = 0.5,
        number_of_digits_to_round: int = 3,
    ):
        """
        Parameters
        ----------
        number_of_nodes : int
            Number of nodes of the induced graph
        edge_probability: float
            For each edge, the probability that it is generated.
            - value 1.0: all edges are generated
            - value 0.0: no edge is generated
        number_of_nodes : int
            Number of nodes of the graph
        weight_size : int
            size of the vector of weight
        percentage_of_nodes_to_fit : int
            average number of nodes that we want to
            fit into the knapsack
        number_of_digits_to_round : int
            the number of digits to use in the round function
            of the random number generation

        Raises
        ------
        ValueError
            If `number_of_nodes` is smaller than 2 (two)
            If `edge_probability` is not in the interval [0, 1]
                (between 0 (zero) (inclusive) and 1 (one) (inclusive))
            If `weight_size` is smaller or equal to 0 (zero)
            If `percentage_of_nodes_to_fit` is not in the interval ]0, 1[
                (between 0 (zero) (exclusive) and 1 (one) (exclusive))
            If `number_of_digits_to_round`  is smaller or equal to 0 (zero)
        """
        self.number_of_nodes = number_of_nodes
        self.edge_probability = edge_probability
        self.weight_size = weight_size
        self.percentage_of_nodes_to_fit = percentage_of_nodes_to_fit
        self.number_of_digits_to_round = number_of_digits_to_round
        self._validate_parameters()
        return

    def _validate_parameters(self) -> None:
        if self.number_of_nodes < 2:
            raise ValueError("number_of_nodes must be greater than 1 (one)")
        if not 0 <= self.edge_probability <= 1:
            raise ValueError(
                "edge_probability must be a value between 0 (inclusive) and 1 (inclusive)"
            )
        if self.weight_size <= 0:
            raise ValueError("weight_size must must be greater than 0 (zero)")
        if not 0 < self.percentage_of_nodes_to_fit < 1:
            raise ValueError(
                "percentage_of_nodes_to_fit must be a value between 0 (zero) (exclusive) and 1 (one) (exclusive)"
            )
        if self.number_of_digits_to_round <= 0:
            raise ValueError(
                "number_of_digits_to_round must must be greater than 0 (zero)"
            )
        return

    def to_dict(self) -> dict:
        data = {}
        data["number_of_nodes"] = self.number_of_nodes
        data["edge_probability"] = self.edge_probability
        data["weight_size"] = self.weight_size
        data["percentage_of_nodes_to_fit"] = self.percentage_of_nodes_to_fit
        data["number_of_digits_to_round"] = self.number_of_digits_to_round
        return data

    @staticmethod
    def from_dict(data: dict):
        return InstanceGeneratorParameters(
            number_of_nodes=data["number_of_nodes"],
            edge_probability=data["edge_probability"],
            weight_size=data["weight_size"],
            percentage_of_nodes_to_fit=data["percentage_of_nodes_to_fit"],
            number_of_digits_to_round=data["number_of_digits_to_round"],
        )
