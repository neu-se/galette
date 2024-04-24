import argparse
from subprocess import TimeoutExpired

import pandas as pd

import run_data
from evaluation_util import *
from report_util import set_columns
from run_data import Status, write_status

BENCHMARKS = ['avrora', 'batik', 'biojava', 'eclipse', 'fop', 'graphchi', 'h2', 'h2o', 'jme',
              'jython', 'kafka', 'luindex', 'lusearch', 'pmd', 'spring', 'sunflow', 'tomcat', 'tradebeans',
              'tradesoap', 'xalan', 'zxing']
MEASUREMENT_ITERATIONS = 5
WARMUP_ITERATIONS = 5
CALLBACK_CLASS = 'edu.neu.ccs.prl.galette.eval.RecordingCallback'
DACAPO_MAIN_CLASS = 'Harness'


def run_dacapo(resources_dir, report_file, tool, dacapo_dir, benchmark, timeout, settings_file):
    # Get a JDK for the DaCapo process
    tool_jdk = create_tool_jdk(resources_dir, tool, '11', settings_file)
    java_executable = java_home_to_executable(tool_jdk)
    java_options = ['-ea', MAX_HEAP, f'-Dgalette.dacapo.report={os.path.abspath(report_file)}']
    if tool == 'mirror-taint':
        java_options += ['-Dtaint.quiet=true']
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
    try:
        process = subprocess.run(command, shell=False, timeout=timeout)
        status = Status.SUCCESS if process.returncode == 0 else Status.DACAPO_FAILURE
        print(f'Finished DaCapo benchmark')
    except TimeoutExpired:
        status = Status.TIMEOUT
        print(f'Timeout expired for DaCapo benchmark')
    return status


def extract_dacapo(archive, resources_dir):
    if os.path.isdir(resources_dir):
        print(f"Using existing DaCapo directory: {resources_dir}")
    else:
        print(f"Extracting DaCapo archive to {resources_dir}")
        os.makedirs(resources_dir, exist_ok=True)
        subprocess.check_output(['tar', '-xf', archive, '--strip-components', '1', '-C', resources_dir])
        print(f"Extracted DaCapo archive")


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


def run(output_dir, benchmark, tool, resources_dir, dacapo_archive, timeout, settings_file, skip_build):
    # Ensure the results directory exists
    os.makedirs(output_dir, exist_ok=True)
    data_file = os.path.join(output_dir, run_data.DATA_FILE_NAME)
    status_file = os.path.join(output_dir, run_data.STATUS_FILE_NAME)
    try:
        # Build Galette
        build_maven_project(resources_dir, GALETTE_ROOT, settings_file, skip_build, '17')
        # Build evaluation classes
        build_maven_project(resources_dir, GALETTE_EVALUATION_ROOT, settings_file, skip_build, '17')
        # Ensure the DaCapo archive has been extracted
        dacapo_dir = os.path.join(resources_dir, 'dacapo')
        extract_dacapo(dacapo_archive, dacapo_dir)
        # Run DaCapo
        status = run_dacapo(resources_dir, data_file, tool, dacapo_dir, benchmark, timeout * 60, settings_file)
        status = run_dacapo(resources_dir, data_file, tool, dacapo_dir, benchmark, timeout * 60, settings_file)
        # Fix the report
        update_report(data_file, benchmark, tool)
    except Exception as e:
        write_status(status_file, Status.BUILD_FAILURE, benchmark=benchmark, tool=tool)
        raise e
    write_status(status_file, status, benchmark=benchmark, tool=tool)


def main():
    parser = argparse.ArgumentParser(description='Collects performance data for a tool on a DaCapo Benchmark.')
    parser.add_argument(
        '-o',
        '--output-dir',
        type=str,
        help='Path of the directory into which output should be written.',
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
        '-x',
        '--timeout',
        help='Maximum amount of time to run the DaCapo process for in minutes (defaults to 1440).',
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
        help='Skip building of associated Maven projects (defaults to False)',
        default=False,
        action='store_true'
    )
    args = parser.parse_args()
    print(f'Collecting performance experiment data: {args.__dict__}')
    run(**args.__dict__)


if __name__ == '__main__':
    main()
