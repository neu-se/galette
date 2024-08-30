import argparse
import re

from evaluation_util import *

DRIVER_MAIN_CLASS = 'edu.neu.ccs.prl.galette.bench.extension.BenchmarkDriver'
TAG_MANAGERS = {
    'galette': 'edu.neu.ccs.prl.galette.bench.extension.GaletteTagManager',
    'mirror-taint': 'edu.neu.ccs.prl.galette.eval.MirrorTaintTagManager',
    'phosphor': 'edu.neu.ccs.prl.galette.eval.PhosphorTagManager'
}

COLUMNS = ["tool", "version", "test", "group", "tp", "fp", "fn", "status", "result"]


def get_benchmark_group(test_identifier):
    m = re.search(r'\.([^.]*?)ITCase', test_identifier)
    return m.group(1)


def compute_result(fp, fn, status):
    if status != 'success':
        return 'semantic'
    else:
        return 'success' if (fp + fn == 0) else "tag"


def process_output(output_dir):
    report_file = os.path.join(output_dir, trial_data.DATA_FILE_NAME)
    data = pd.read_csv(report_file) \
        .rename(columns=lambda x: x.strip())
    # Remove disabled tests
    data = data[data['status'] != 'disabled']
    # Convert test class names into benchmark groups
    data['group'] = data['test'].apply(get_benchmark_group)
    # Convert versions to ints
    data['version'] = pd.to_numeric(data['version'])
    # Compute test results
    data['result'] = data[['fp', 'fn', 'status']] \
        .apply(lambda x: compute_result(*x), axis=1)
    # Reorder the columns
    data = data[COLUMNS]
    data.to_csv(report_file, index=False)


def create_driver_command(resources_dir, settings_file, report_file, tool, version):
    jdk = os.path.join(resources_dir, 'jdk', 'temurin', version)
    download_jdk.download(jdk, False, version)
    # Get a JDK for the driver process
    tool_jdk = create_tool_jdk(resources_dir, tool, version, settings_file)
    # Get the classpath for the evaluation module
    classpath = get_classpath(resources_dir, GALETTE_EVALUATION_CORE_ROOT, scope='runtime') + \
                ':' + GALETTE_EVALUATION_CLASSES
    # Get the JAR file for the Java agent
    agent_jar = get_agent_jar(resources_dir, tool, settings_file)
    java_executable = java_home_to_executable(jdk)
    command = [
        os.path.abspath(java_executable),
        '-cp',
        classpath,
        DRIVER_MAIN_CLASS,
        os.path.abspath(tool_jdk),
        os.path.abspath(report_file),
        '-cp',
        classpath,
        '-ea',
        MAX_HEAP,
        f'-Xbootclasspath/a:{os.path.abspath(agent_jar)}',
        f'-javaagent:{os.path.abspath(agent_jar)}{JAVA_AGENT_OPTIONS[tool]}',
        f'-Dflow.manager={TAG_MANAGERS[tool]}'
    ]
    if tool == 'mirror-taint':
        command += ["-Dtaint.quiet=true"]
    return command


def run_functional(output_dir, resources_dir, settings_file, skip_build, version, tool):
    run(output_dir, resources_dir, settings_file, skip_build, 'functional experiment driver', None, lambda x: x,
        create_driver_command, version=version, tool=tool)
    process_output(output_dir)


def main():
    parser = argparse.ArgumentParser(description='Collects functional experiment data for a tool on a JDK.')
    parser.add_argument(
        '-o',
        '--output-dir',
        type=str,
        help='Path of the directory into which output should be written.',
        required=True
    )
    parser.add_argument(
        '-t',
        '--tool',
        help='Name of the tool to be evaluated.',
        choices=TOOLS[1:],
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
        '-v',
        '--version',
        help='Major version for the JDK to be used.',
        type=str,
        choices=['8', '11', '17', '21'],
        required=True
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
    print(f'Collecting functional experiment data: {args.__dict__}')
    run_functional(**args.__dict__)


if __name__ == '__main__':
    main()
