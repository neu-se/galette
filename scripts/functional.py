import argparse

import pandas as pd

from evaluation_util import *
from report_util import set_columns

DRIVER_MAIN_CLASS = 'edu.neu.ccs.prl.galette.bench.extension.BenchmarkDriver'
TAG_MANAGERS = {
    'galette': 'edu.neu.ccs.prl.galette.bench.extension.GaletteTagManager',
    'mirror-taint': 'edu.neu.ccs.prl.galette.eval.MirrorTaintTagManager',
    'phosphor': 'edu.neu.ccs.prl.galette.eval.PhosphorTagManager'
}


def run_driver(resources_dir, report_file, tool, version, settings_file):
    jdk = os.path.join(resources_dir, 'jdk', version)
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
    print(f'Starting functional experiment driver')
    print('\t' + ' '.join(command))
    subprocess.run(command, shell=False, check=True)
    print(f'Finished functional experiment driver')


def update_report(report_file, version, tool):
    # Read the unprocessed report
    data = pd.read_csv(report_file) \
        .rename(columns=lambda x: x.strip())
    # Add columns for the version and tool to the report
    data = set_columns(data, version=version, tool=tool)
    # Write the updated report
    data.to_csv(report_file, index=False)


def run(report_file, tool, resources_dir, version, settings_file, skip_build):
    # Build Galette
    build_maven_project(resources_dir, GALETTE_ROOT, settings_file, skip_build, '17')
    # Build evaluation classes
    build_maven_project(resources_dir, GALETTE_EVALUATION_ROOT, settings_file, skip_build, '17')
    # Ensure the parent directory of the report file exists
    os.makedirs(pathlib.Path(report_file).parent, exist_ok=True)
    # Run the driver
    run_driver(resources_dir, report_file, tool, version, settings_file)
    # Fix the report
    update_report(report_file, version, tool)


def main():
    parser = argparse.ArgumentParser(description='Collects functional experiment data for a tool on a JDK.')
    parser.add_argument(
        '-f',
        '--report-file',
        type=str,
        help='Path of file into which the results should be written.',
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
        help='Skip building of associated Maven projects (defaults to False)',
        default=False,
        action='store_true'
    )
    args = parser.parse_args()
    print(f'Collecting functional experiment data: {args.__dict__}')
    run(**args.__dict__)


if __name__ == '__main__':
    main()
