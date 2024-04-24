import os

from report_util import *

DATA_FILE_NAME = 'report.csv'
INFO_FILE_NAME = 'info.json'


class Trial:
    """Represents the results of a taint tracking tool on a benchmark or benchmark suite."""

    def __init__(self, trial_dir):
        self.data_file = os.path.join(trial_dir, DATA_FILE_NAME)
        self.valid = all(os.path.isfile(f) for f in [self.data_file])
        self.id = os.path.basename(trial_dir)

    def get_data_frame(self):
        data = pd.read_csv(self.data_file) \
            .rename(columns=lambda x: x.strip())
        return select(data, trial_id=id)


def find_trials(input_dir):
    print(f'Searching for trials in {input_dir}.')
    files = [os.path.join(input_dir, f) for f in os.listdir(input_dir)]
    trials = list(map(Trial, filter(os.path.isdir, files)))
    print(f"\tFound {len(trials)} trials.")
    return trials


def check_trials(trials):
    print(f'Checking trials.')
    result = []
    for c in trials:
        if not c.valid:
            print(f"\tMissing required files for {c.id}.")
        else:
            result.append(c)
    print(f'\t{len(result)} trials were valid.')
    return result


def combine_trials(trials, file):
    print('Creating combined experiment CSV.')
    data = pd.concat([t.get_data_frame() for t in trials]) \
        .reset_index(drop=True)
    data.to_csv(file, index=False)
    print(f'\tWrote combined experiment CSV to {file}.')
    return data


def extract(input_dir, output_file):
    return combine_trials(check_trials(find_trials(input_dir)), output_file)


def assert_record_equality(frames):
    if len(frames) <= 1:
        return
    record_sets = [set(list(map(tuple, frame.to_dict(orient='records')))) for frame in frames]
    assert all(record_sets[0] == record_set for record_set in record_sets[1:])


def assert_vendor_matches(counts):
    vendors = list(counts['vendor'].unique())
    for version in list(counts['version'].unique()):
        assert_record_equality([select(counts, version=version, vendor=vendor) for vendor in vendors])
