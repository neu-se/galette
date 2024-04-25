import json
import os
from enum import auto

from strenum import StrEnum

from report_util import *

DATA_FILE_NAME = 'data.csv'
STATUS_FILE_NAME = 'status.json'


class Status(StrEnum):
    SUCCESS = auto()
    TIMEOUT = auto()
    BUILD_FAILURE = auto()
    RUN_FAILURE = auto()
    MISSING = auto()


class RunData:
    """Represents the results of a taint tracking tool on a benchmark or benchmark suite."""

    def __init__(self, input_dir):
        self.data_file = os.path.join(input_dir, DATA_FILE_NAME)
        status_file = os.path.join(input_dir, STATUS_FILE_NAME)
        if os.path.isfile(status_file):
            with open(status_file, 'r') as f:
                self.info = json.load(f)
                self.status = Status(self.info['status'])
        else:
            self.info = {'status': 'MISSING'}
            self.status = Status.MISSING
        self.id = os.path.basename(input_dir)

    def get_data_frame(self):
        data = pd.read_csv(self.data_file) \
            .rename(columns=lambda x: x.strip())
        return set_columns(data, run_id=self.id)


def find(input_dir):
    print(f'Searching for runs in {input_dir}.')
    files = [os.path.join(input_dir, f) for f in os.listdir(input_dir)]
    runs = list(map(RunData, filter(os.path.isdir, files)))
    print(f"Found {len(runs)} runs.")
    return runs


def check(runs):
    print(f'Checking runs.')
    result = []
    for c in runs:
        if c.status != Status.SUCCESS:
            print(f'\tFailed run {c.id} --- {c.info}')
        else:
            result.append(c)
    print(f'{len(result)} runs were successful.')
    return result


def combine(runs, file):
    print('Creating combined run dataset CSV.')
    data = pd.concat([t.get_data_frame() for t in runs]) \
        .reset_index(drop=True)
    data.to_csv(file, index=False)
    print(f'Wrote combined run dataset CSV to {file}.')
    return data


def extract(input_dir, output_file):
    return combine(check(find(input_dir)), output_file)


def write_status(status_file, status, **kwargs):
    print(f'Writing status to {status_file}.')
    with open(status_file, 'w') as f:
        data = kwargs.copy()
        data['status'] = status
        json.dump(data, f)
    print(f'Wrote status.')
