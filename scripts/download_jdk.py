import argparse
import os
import subprocess

from downloader import Downloader

JDK_URL_TEMPLATES = {
    'corretto': 'https://corretto.aws/downloads/resources/{2}/amazon-corretto-{2}-linux-x64.tar.gz',
    'temurin': 'https://github.com/adoptium/temurin{1}-binaries/releases/download/{2}/' +
               'OpenJDK{1}U-jdk_x64_linux_hotspot_{3}.tar.gz',
    'oracle': 'https://download.oracle.com/java/{1}/archive/jdk-{2}_linux-x64_bin.tar.gz'
}

JDKS = {
    ('temurin', '8'): ('jdk8u402-b06', '8u402b06'),
    ('temurin', '11'): ('jdk-11.0.22%2B7', '11.0.22_7'),
    ('temurin', '17'): ('jdk-17.0.10%2B7', '17.0.10_7'),
    ('temurin', '21'): ('jdk-21.0.2%2B13', '21.0.2_13'),
    ('corretto', '8'): '8.402.08.1',
    ('corretto', '11'): '11.0.22.7.1',
    ('corretto', '17'): '17.0.10.8.1',
    ('corretto', '21'): '21.0.2.14.1',
    ('oracle', '17'): '17.0.10',
    ('oracle', '21'): '21.0.2'
}


def download(output_dir, vendor, version):
    info = tuple(JDKS[(vendor, version)])
    url = JDK_URL_TEMPLATES[vendor].format(vendor, version, *info)
    jdk_dir = os.path.join(output_dir, vendor, version)
    os.makedirs(jdk_dir, exist_ok=True)
    file_name = url.rsplit('/', 1)[-1]
    # Download the archive
    archive = os.path.join(jdk_dir, file_name)
    with Downloader(url) as d:
        d.download(archive)
    # Unpack the archive and strip the top level directory
    subprocess.check_output(['tar', '-xvzf', archive, '--strip-components', '1', '-C', jdk_dir])
    # Remove the archive
    os.remove(archive)
    return jdk_dir


def download_all(output_dir):
    for key in JDKS.keys():
        download(output_dir, *key)


def main():
    parser = argparse.ArgumentParser(description='Downloads a Java Development Kit (JDK).')
    parser.add_argument('--output-dir', type=str,
                        help='Path of the directory into which the resource to be downloaded should be stored.',
                        required=True)
    parser.add_argument("--vendor", help='Software vendor for the JDK.',
                        choices=['temurin', 'corretto', 'oracle'], required=True)
    parser.add_argument('--version', help='Major version for the JDK.',
                        type=str,
                        choices=['8', '11', '17', '21'], required=True)
    args = parser.parse_args()
    download(**args.__dict__)


if __name__ == '__main__':
    main()
