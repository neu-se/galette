import argparse
from evaluation_util import *

BENCHMARKS = ['avrora', 'batik', 'biojava', 'cassandra', 'eclipse', 'fop', 'graphchi', 'h2', 'h2o', 'jme',
              'jython', 'kafka', 'luindex', 'lusearch', 'pmd', 'spring', 'sunflow', 'tomcat', 'tradebeans',
              'tradesoap', 'xalan', 'zxing']


def run_dacapo(output_dir, report_file, tool, dacapo_dir, iterations, benchmark, settings_file):
    # Get a JDK for the DaCapo process
    tool_jdk = create_tool_jdk(output_dir, tool, '11', settings_file)
    java_executable = java_home_to_executable(tool_jdk)
    java_options = ['-ea', MAX_HEAP, f'-Dgalette.dacapo.report={os.path.abspath(report_file)}']
    # Get the JAR file for the Java agent
    agent_jar = get_agent_jar(output_dir, tool, settings_file)
    if agent_jar is not None:
        java_options += [
            f'-Xbootclasspath/a:{os.path.abspath(agent_jar)}',
            f'-javaagent:{os.path.abspath(agent_jar)}{JAVA_AGENT_OPTIONS[tool]}',
        ]
    dacapo_jar = os.path.join(dacapo_dir, 'dacapo-23.11-chopin.jar')
    java_options += ['-jar', os.path.abspath(dacapo_jar)]
    # TODO --callback <CALLBACK_CLASS>
    dacapo_options = ['--size', 'small', '-f', '1000', '-n', str(iterations), benchmark]
    command = [os.path.abspath(java_executable)] + java_options + dacapo_options
    print(f'Starting DaCapo benchmark: {benchmark}')
    print('\t' + ' '.join(command))
    subprocess.run(command, shell=False, check=True)
    print(f'Finished DaCapo benchmark')


def extract_dacapo(archive, output_dir):
    if os.path.isdir(output_dir):
        print(f"Using existing DaCapo directory: {output_dir}")
    print(f"Extracting DaCapo archive to {output_dir}")
    os.makedirs(output_dir, exist_ok=True)
    subprocess.check_output(['tar', '-xf', archive, '--strip-components', '1', '-C', output_dir])
    print(f"Extracted DaCapo archive.")


def run(report_file, benchmark, tool, output_dir, dacapo_archive, settings_file):
    # Build Galette
    build_maven_project(output_dir, GALETTE_ROOT, settings_file, '17')
    # Build evaluation classes
    build_maven_project(output_dir, GALETTE_EVALUATION_ROOT, settings_file, '17')
    # Ensure the parent directory of the report file exists
    os.makedirs(pathlib.Path(report_file).parent, exist_ok=True)
    # Ensure the DaCapo archive has been extracted
    dacapo_dir = os.path.join(output_dir, 'dacapo')
    extract_dacapo(dacapo_archive, dacapo_dir)
    # Run DaCapo
    run_dacapo(output_dir, report_file, tool, dacapo_dir, 1, benchmark, settings_file)


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
        '-o',
        '--output-dir',
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
