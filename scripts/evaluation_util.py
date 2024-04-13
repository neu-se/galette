import os
import pathlib
import subprocess
import tempfile
from collections import defaultdict

import download_jdk

TOOLS = ['none', 'galette', 'mirror-taint', 'phosphor']
GALETTE_VERSION = '1.0.0-SNAPSHOT'
GALETTE_ROOT = pathlib.Path(__file__).parent.parent
GALETTE_EVALUATION_ROOT = os.path.join(pathlib.Path(__file__).parent.parent, 'galette-evaluation')
GALETTE_EVALUATION_CORE_ROOT = os.path.join(GALETTE_EVALUATION_ROOT, 'galette-evaluation-core')
GALETTE_EVALUATION_CLASSES = os.path.join(GALETTE_EVALUATION_CORE_ROOT, 'target', 'classes')
MIRROR_TAINT_AGENT_JAR = os.path.join(GALETTE_ROOT, 'galette-evaluation', 'lib', 'taint-agent-core-1.0.0.jar')
GALETTE_AGENT_JAR = os.path.join(GALETTE_ROOT, 'galette-agent', 'target', f'galette-agent-{GALETTE_VERSION}.jar')
GALETTE_INSTRUMENT_JAR = os.path.join(GALETTE_ROOT, 'galette-instrument', 'target',
                                      f'galette-instrument-{GALETTE_VERSION}.jar')
PHOSPHOR_VERSION = '0.1.0-SNAPSHOT'
PHOSPHOR_AGENT_COORDINATE = f'edu.gmu.swe.phosphor:Phosphor:{PHOSPHOR_VERSION}:jar'
PHOSPHOR_DRIVER_COORDINATE = f'edu.gmu.swe.phosphor:phosphor-driver:{PHOSPHOR_VERSION}:jar'
MAX_HEAP = '-Xmx6g'
JAVA_AGENT_OPTIONS = defaultdict(lambda: '', {'phosphor': '=enum,acmpeq'})


def java_home_to_executable(java_home):
    return os.path.join(java_home, 'bin', 'java')


def ensure_java_home(output_dir, jdk_version):
    # Download a JDK if necessary
    jdk = os.path.join(output_dir, 'jdk', 'temurin', jdk_version)
    download_jdk.download(jdk, False, jdk_version)
    # Set JAVA_HOME
    os.environ["JAVA_HOME"] = os.path.abspath(jdk)
    return jdk


def build_maven_project(output_dir, project_root, settings_file, jdk_version='17'):
    ensure_java_home(output_dir, jdk_version)
    print(f'Building project {project_root}')
    # Build and install the project
    command = ['mvn', '-B', '-q', '-ntp', '-e', '-f', os.path.abspath(project_root), '-DskipTests', 'clean', 'install']
    if settings_file is not None:
        command += ['-s', os.path.abspath(settings_file)]
    print('\t' + ' '.join(command))
    subprocess.run(command, shell=False, check=True)
    print('Built project')


def instrument_jdk_galette(java_home, target_dir):
    if os.path.isdir(target_dir):
        print(f'Using existing Galette-instrumented JDK: {target_dir}')
        return
    print(f'Creating Galette-instrumented JDK: {target_dir}')
    # Assumes that Galette has been built
    java_executable = java_home_to_executable(java_home)
    command = [
        os.path.abspath(java_executable),
        '-jar',
        os.path.abspath(GALETTE_INSTRUMENT_JAR),
        os.path.abspath(java_home),
        os.path.join(target_dir)
    ]
    subprocess.run(command, shell=False, check=True)
    print(f'Created Galette-instrumented JDK: {target_dir}')


def copy_dependency(project_root, output_dir, coordinate, settings_file):
    ensure_java_home(output_dir, '17')
    print(f'Retrieving JAR for {coordinate}')
    command = [
        'mvn', '-q', '-e',
        '-f', os.path.abspath(project_root),
        'dependency:copy',
        f'-Dartifact={coordinate}',
        f'-DoutputDirectory={os.path.abspath(output_dir)}',
        '-Dmdep.useBaseVersion'
    ]
    if settings_file is not None:
        command += ['-s', os.path.abspath(settings_file)]
    subprocess.run(command, shell=False, check=True)
    _, artifact_id, version, _ = coordinate.split(':', 4)
    print(f'Retrieved JAR for {coordinate}')
    return os.path.join(output_dir, f'{artifact_id}-{version}.jar')


def get_agent_jar(output_dir, tool, settings_file):
    if tool == 'phosphor':
        return copy_dependency(GALETTE_EVALUATION_ROOT, output_dir, PHOSPHOR_AGENT_COORDINATE, settings_file)
    elif tool == 'galette':
        return GALETTE_AGENT_JAR
    elif tool == 'mirror-taint':
        return MIRROR_TAINT_AGENT_JAR
    else:
        return None


def instrument_jdk_phosphor(java_home, target_dir, version, output_dir, settings_file):
    if os.path.isdir(target_dir):
        print(f'Using existing Phosphor-instrumented JDK: {target_dir}')
        return
    print(f'Creating Phosphor-instrumented JDK: {target_dir}')
    java_executable = java_home_to_executable(java_home)
    driver_jar = copy_dependency(GALETTE_EVALUATION_ROOT, output_dir, PHOSPHOR_DRIVER_COORDINATE, settings_file)
    phosphor_options = ['-q', '-forceUnboxAcmpEq', '-withEnumsByValue', '-serialization']
    if version == 8:
        phosphor_options.append("-java8")
    command = [os.path.abspath(java_executable), MAX_HEAP, '-jar', os.path.abspath(driver_jar)]
    command += phosphor_options
    command += [os.path.abspath(java_home), os.path.join(target_dir)]
    subprocess.run(command, shell=False, check=True)
    print(f'Created Phosphor-instrumented JDK')


def create_tool_jdk(output_dir, tool, version, settings_file):
    # Get the original JDK
    jdk = os.path.join(output_dir, 'jdk', version)
    download_jdk.download(jdk, False, version)
    instrumented_jdk = os.path.join(output_dir, tool, 'jdk', version)
    # Instrument if necessary
    if tool == 'galette':
        instrument_jdk_galette(jdk, instrumented_jdk)
        return instrumented_jdk
    elif tool == 'phosphor':
        instrument_jdk_phosphor(jdk, instrumented_jdk, version, output_dir, settings_file)
        return instrumented_jdk
    else:
        instrumented_jdk = jdk
    print('Created JDK')
    return instrumented_jdk


def get_classpath(output_dir, project_root, scope='test'):
    ensure_java_home(output_dir, '17')
    with tempfile.NamedTemporaryFile() as temp:
        command = [
            'mvn', '-q', '-e',
            '-f', os.path.abspath(project_root),
            'dependency:build-classpath',
            f'-DincludeScope={scope}',
            f'-Dmdep.outputFile={os.path.abspath(temp.name)}'
        ]
        subprocess.run(command, shell=False, check=True)
        with open(temp.name) as f:
            return f.read()
