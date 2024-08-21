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


class TrialData:
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
        return set_columns(data, trial_id=self.id)


def find(input_dir):
    print(f'Searching for trials in {input_dir}.')
    files = [os.path.join(input_dir, f) for f in os.listdir(input_dir)]
    trials = list(map(TrialData, filter(os.path.isdir, files)))
    print(f"Found {len(trials)} trials.")
    return trials


def check(trials):
    print(f'Checking trials.')
    result = []
    for c in trials:
        if c.status != Status.SUCCESS:
            print(f'\tUnsuccessful trial {c.id} --- {c.info}')
        else:
            result.append(c)
    print(f'{len(result)} trials were successful.')
    return result


def partition(elements, predicate):
    t, f = [], []
    for e in elements:
        (t if predicate(e) else f).append(e)
    return [t, f]


def aggregate(trials):
    print('Creating aggregated dataset.')
    data = pd.concat([t.get_data_frame() for t in trials]) \
        .reset_index(drop=True)
    return data


def aggregate_and_write(trials, output_file):
    if len(trials) == 0:
        return None
    data = aggregate(trials)
    data.to_csv(output_file, index=False)
    print(f'Wrote aggregated dataset CSV to {output_file}.')
    return data


def extract(input_dir, output_dir):
    functional_file = os.path.join(output_dir, 'functional.csv')
    performance_file = os.path.join(output_dir, 'performance.csv')
    if os.path.isfile(functional_file) and os.path.isfile(performance_file):
        print('Using existing aggregated datasets.')
        return pd.read_csv(functional_file), pd.read_csv(performance_file)
    else:
        trials = check(find(input_dir))
        performance_trials, functional_trials = partition(trials, lambda t: 'elapsed_time' in t.columns)
        return aggregate_and_write(functional_trials, functional_file), \
            aggregate_and_write(performance_trials, performance_file)
