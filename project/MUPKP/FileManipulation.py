import json
from typing import Dict


class FileManipulation:
    @staticmethod
    def json_to_dict(file_name: str) -> Dict:
        with open(file_name, "r") as file:
            data = json.load(file)
        return data

    @staticmethod
    def dict_to_json(
        data: Dict,
        file_name: str,
    ) -> None:
        with open(file_name, "w") as file:
            file.write(json.dumps(data, indent=4, ensure_ascii=True))
        return
