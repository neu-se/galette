import argparse
import os

import performance
from functional import run_functional
from report import create_report


def replicate(output_dir, resources_dir, settings_file, skip_build, dacapo_archive, timeout):
    # Run functional trials
    for tool in ['galette', 'phosphor']:
        for version in ['8', '11', '17', '21']:
            trial_dir = os.path.join(output_dir, f'functional-{tool}-{version}')
            run_functional(trial_dir, resources_dir, settings_file, skip_build, version, tool)
    # Run performance trials
    for tool in ['none', 'galette', 'phosphor']:
        for benchmark in performance.BENCHMARKS:
            # Skip tradebeans and tradesoap for phosphor because
            # of the problematic logging in an infinite loop
            if tool == 'phosphor':
                if benchmark == 'tradebeans' or benchmark == 'tradesoap':
                    continue
            for trial in range(0, 20):
                trial_dir = os.path.join(output_dir, f'functional-{tool}-{benchmark}-{trial}')
                performance.run_performance(trial_dir, resources_dir, settings_file, skip_build, benchmark,
                                            tool, dacapo_archive, timeout)
    # Create the report
    create_report(output_dir, output_dir)


def main():
    parser = argparse.ArgumentParser(description='Runs all of the trials needed for a full evaluation.')
    parser.add_argument(
        '-o',
        '--output-dir',
        type=str,
        help='Path of the directory into which output should be written.',
        required=True
    )
    parser.add_argument(
        '-r',
        '--resources-dir',
        type=str,
        help='Path of the directory into which downloaded resources should be stored and cached.',
        required=True
    )
    parser.add_argument(
        '-d',
        '--dacapo-archive',
        type=str,
        help='Path of the DaCapo archive.',
        required=True
    )
    parser.add_argument(
        '-x',
        '--timeout',
        help='Maximum amount of time to run DaCapo processes for in minutes (defaults to 1440).',
        type=float,
        default=60 * 24
    )
    parser.add_argument(
        '-s',
        '--settings-file',
        help='Path to a setting file for Maven.',
        type=str
    )
    parser.add_argument(
        '-k',
        '--skip-build',
        help='Skip building Maven projects (defaults to False)',
        default=False,
        action='store_true'
    )
    args = parser.parse_args()
    print(f'Running replication trials: {args.__dict__}')
    replicate(**args.__dict__)


if __name__ == '__main__':
    main()
