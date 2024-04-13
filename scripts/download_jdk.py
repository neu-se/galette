import argparse
import os
import subprocess
import requests
from tqdm import tqdm

JDK_URL_TEMPLATES = {
    'corretto': 'https://corretto.aws/downloads/resources/{2}/amazon-corretto-{2}-linux-x64.tar.gz',
    'temurin': 'https://github.com/adoptium/temurin{1}-binaries/releases/download/{2}/' +
               'OpenJDK{1}U-jdk_x64_linux_hotspot_{3}.tar.gz',
    'oracle': 'https://download.oracle.com/java/{1}/archive/jdk-{2}_linux-x64_bin.tar.gz'
}

JDKS = {
    ('temurin', '8'): ['jdk8u402-b06', '8u402b06'],
    ('temurin', '11'): ['jdk-11.0.22%2B7', '11.0.22_7'],
    ('temurin', '17'): ['jdk-17.0.10%2B7', '17.0.10_7'],
    ('temurin', '21'): ['jdk-21.0.2%2B13', '21.0.2_13'],
    ('corretto', '8'): ['8.402.08.1'],
    ('corretto', '11'): ['11.0.22.7.1'],
    ('corretto', '17'): ['17.0.10.8.1'],
    ('corretto', '21'): ['21.0.2.14.1'],
    ('oracle', '17'): ['17.0.10'],
    ('oracle', '21'): ['21.0.2']
}


class Downloader(tqdm):
    def __init__(self, url):
        tqdm.__init__(self, unit='B', unit_scale=True, miniters=1, desc=url.rsplit('/', 1)[-1])
        self.url = url

    def record_progress(self, block_number, read_size, total_file_size):
        self.total = total_file_size
        self.update(block_number * read_size - self.n)

    def download(self, output_path):
        response = requests.get(self.url, stream=True)
        response.raise_for_status()
        self.total = int(response.headers.get("content-length", 0))
        with open(output_path, 'wb') as file:
            for chunk in response.iter_content(1024):
                self.update(len(chunk))
                file.write(chunk)


def download(output_dir, force, version, vendor='temurin'):
    if not force and os.path.isdir(output_dir):
        print(f'Found existing JDK directory: {output_dir}')
    else:
        print(f'Downloading JDK (vendor={vendor}, version={version}): {output_dir}')
        info = JDKS[(vendor, version)]
        url = JDK_URL_TEMPLATES[vendor].format(vendor, version, *info)
        os.makedirs(output_dir, exist_ok=True)
        file_name = url.rsplit('/', 1)[-1]
        # Download the archive
        archive = os.path.join(output_dir, file_name)
        with Downloader(url) as d:
            d.download(archive)
        # Unpack the archive and strip the top level directory
        subprocess.check_output(['tar', '-xvzf', archive, '--strip-components', '1', '-C', output_dir])
        # Remove the archive
        os.remove(archive)
        print('Downloaded JDK.')


def main():
    parser = argparse.ArgumentParser(description='Downloads a Java Development Kit (JDK).')
    parser.add_argument(
        '-o',
        '--output-dir',
        type=str,
        help='Path of the directory into which the resource to be downloaded should be stored.',
        required=True
    )
    parser.add_argument(
        '-n',
        '--vendor',
        help='Software vendor for the JDK.',
        choices=['temurin', 'corretto', 'oracle'],
        required=True
    )
    parser.add_argument(
        '-v',
        '--version',
        help='Major version for the JDK.',
        type=str,
        choices=['8', '11', '17', '21'],
        required=True
    )
    parser.add_argument(
        '-f',
        '--force',
        help='Indicates that an existing directory should be overwritten',
        action=argparse.BooleanOptionalAction
    )
    args = parser.parse_args()
    download(**args.__dict__)


if __name__ == '__main__':
    main()
