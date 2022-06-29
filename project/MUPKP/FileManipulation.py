import json


class FileManipulation:
    @staticmethod
    def json_to_dict(file_name: str) -> dict:
        with open(file_name, "r") as file:
            data = json.load(file)
        return data

    @staticmethod
    def dict_to_json(
        data: dict,
        file_name: str,
    ) -> None:
        with open(file_name, "w") as file:
            file.write(json.dumps(data, indent=2, ensure_ascii=True))
        return
