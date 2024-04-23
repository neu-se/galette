import argparse

import pandas as pd

from evaluation_util import *
from report_util import set_columns

BENCHMARKS = ['avrora', 'batik', 'biojava', 'cassandra', 'eclipse', 'fop', 'graphchi', 'h2', 'h2o', 'jme',
              'jython', 'kafka', 'luindex', 'lusearch', 'pmd', 'spring', 'sunflow', 'tomcat', 'tradebeans',
              'tradesoap', 'xalan', 'zxing']
MEASUREMENT_ITERATIONS = 5
WARMUP_ITERATIONS = 5
CALLBACK_CLASS = 'edu.neu.ccs.prl.galette.eval.RecordingCallback'
DACAPO_MAIN_CLASS = 'Harness'


def run_dacapo(resources_dir, report_file, tool, dacapo_dir, benchmark, settings_file):
    # Get a JDK for the DaCapo process
    tool_jdk = create_tool_jdk(resources_dir, tool, '11', settings_file)
    java_executable = java_home_to_executable(tool_jdk)
    java_options = ['-ea', MAX_HEAP, f'-Dgalette.dacapo.report={os.path.abspath(report_file)}']
    # Get the JAR file for the Java agent
    agent_jar = get_agent_jar(resources_dir, tool, settings_file)
    if agent_jar is not None:
        java_options += [
            f'-Xbootclasspath/a:{os.path.abspath(agent_jar)}',
            f'-javaagent:{os.path.abspath(agent_jar)}{JAVA_AGENT_OPTIONS[tool]}',
        ]
    dacapo_jar = os.path.join(dacapo_dir, 'dacapo-23.11-chopin.jar')
    classpath = ':'.join(os.path.abspath(f) for f in [GALETTE_EVALUATION_CLASSES, dacapo_jar])
    java_options += ['-cp', classpath, DACAPO_MAIN_CLASS]
    dacapo_options = [
        '--size', 'small',
        '-f', '1000',
        '-n', str(MEASUREMENT_ITERATIONS + WARMUP_ITERATIONS),
        '--callback', CALLBACK_CLASS,
        benchmark
    ]
    command = [os.path.abspath(java_executable)] + java_options + dacapo_options
    print(f'Starting DaCapo benchmark: {benchmark}')
    print('\t' + ' '.join(command))
    subprocess.run(command, shell=False, check=True)
    print(f'Finished DaCapo benchmark')


def extract_dacapo(archive, resources_dir):
    if os.path.isdir(resources_dir):
        print(f"Using existing DaCapo directory: {resources_dir}")
    print(f"Extracting DaCapo archive to {resources_dir}")
    os.makedirs(resources_dir, exist_ok=True)
    subprocess.check_output(['tar', '-xf', archive, '--strip-components', '1', '-C', resources_dir])
    print(f"Extracted DaCapo archive.")


def update_report(report_file, benchmark, tool):
    # Read the unprocessed report
    data = pd.read_csv(report_file) \
        .rename(columns=lambda x: x.strip())
    # Add columns for the benchmark and tool to the report
    data = set_columns(data, benchmark=benchmark, tool=tool)
    # Drop warm-up iterations
    data = data[data['iteration'] >= WARMUP_ITERATIONS]
    # Write the updated report
    data.to_csv(report_file, index=False)


def run(report_file, benchmark, tool, resources_dir, dacapo_archive, settings_file):
    # Build Galette
    build_maven_project(resources_dir, GALETTE_ROOT, settings_file, '17')
    # Build evaluation classes
    build_maven_project(resources_dir, GALETTE_EVALUATION_ROOT, settings_file, '17')
    # Ensure the parent directory of the report file exists
    os.makedirs(pathlib.Path(report_file).parent, exist_ok=True)
    # Ensure the DaCapo archive has been extracted
    dacapo_dir = os.path.join(resources_dir, 'dacapo')
    extract_dacapo(dacapo_archive, dacapo_dir)
    # Run DaCapo
    run_dacapo(resources_dir, report_file, tool, dacapo_dir, benchmark, settings_file)
    # Fix the report
    update_report(report_file, benchmark, tool)


def main():
    parser = argparse.ArgumentParser(description='Collects performance data for a tool on a DaCapo Benchmark.')
    parser.add_argument(
        '-f',
        '--report-file',
        type=str,
        help='Path of file into which the results should be written.',
        required=True
    )
    parser.add_argument(
        '-b',
        '--benchmark',
        help='Name of the DaCapo benchmark.',
        choices=BENCHMARKS,
        required=True
    )
    parser.add_argument(
        '-t',
        '--tool',
        help='Name of the tool to be evaluated.',
        choices=TOOLS,
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
        '-s',
        '--settings-file',
        help='Path to a setting file for Maven.',
        type=str
    )
    args = parser.parse_args()
    print(f'Collecting performance experiment data: {args.__dict__}')
    run(**args.__dict__)


if __name__ == '__main__':
    main()
