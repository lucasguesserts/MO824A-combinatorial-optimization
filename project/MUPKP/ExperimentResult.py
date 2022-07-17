from typing import Dict, List
import json
import pandas as pd
from .FileManipulation import FileManipulation


class ExperimentResult:

    possible_metaheuristics: List[str]= ["ilp", "greedy", "grasp", "tabu"]

    def __init__(self, file_name):
        self.metadata_column = "problem_info"
        self.metaheuristic = self._get_metaheuristic_name(file_name)
        self._set_data(file_name)
        self._set_data_frame()
        self._set_metadata()
        return

    def add_metadata(self, df: pd.DataFrame) -> pd.DataFrame:
        return pd.concat([self.metadata, df], axis=1)

    @staticmethod
    def split_by_number_of_nodes(df: pd.DataFrame) -> List[pd.DataFrame]:
        dfs = [x for _, x in df.groupby(df["problem_info", "nodes"])]
        for x in dfs:
            x.sort_values(by=[('problem_info', 'edges')], inplace=True)
        return dfs

    def _set_data(self, file_name):
        self.data = FileManipulation.json_to_dict(file_name)
        return

    def _set_data_frame(self):
        self.df = pd.DataFrame({
            "metaheuristic": self.metaheuristic,
            "problem": map(lambda x: x["problem"], self.data),
            "cost": map(lambda x: x["cost"], self.data),
            "time[s]": map(lambda x: x["runningTime"], self.data),
        })
        self.df: pd.DataFrame = self.df.set_index(["problem","metaheuristic"]).unstack().swaplevel(0,1,1).sort_index(1)
        return

    def _get_metaheuristic_name(self, string: str) -> str:
        # N1000_E2405_W373757

        for metaheuristic in ExperimentResult.possible_metaheuristics:
            if metaheuristic in string:
                return metaheuristic
        raise Exception(f"none of the possible metaheuristics {ExperimentResult.possible_metaheuristics} was found in {string}")

    def _set_metadata(self) -> pd.DataFrame:
        self.metadata = pd.DataFrame()
        self.metadata["problem"] = self.df.index
        self.metadata[""] = len(self.df.index) * [self.metadata_column]
        for column_name, metadata_name in zip(["capacity", "edges", "nodes"], ["average_capacity", "number_of_edges", "number_of_nodes"]):
            self.metadata[column_name] = list(map(lambda x: ExperimentResult._get_metadata(x)[metadata_name], self.df.index))
        self.metadata = self.metadata.set_index(["problem",""]).unstack().swaplevel(0,1,1).sort_index(1)
        return

    @staticmethod
    def _get_metadata(problem_name: str) -> Dict:
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


