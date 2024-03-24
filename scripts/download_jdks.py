import os
import subprocess
import sys
import urllib.request
from tqdm import tqdm

URL_TEMPLATES = {
    'corretto': 'https://corretto.aws/downloads/resources/{2}/amazon-corretto-{2}-linux-x64.tar.gz',
    'temurin': 'https://github.com/adoptium/temurin{1}-binaries/releases/download/{2}/' +
               'OpenJDK{1}U-jdk_x64_linux_hotspot_{3}.tar.gz',
    'oracle': 'https://download.oracle.com/java/{1}/archive/jdk-{2}_linux-x64_bin.tar.gz'
}


class Downloader(tqdm):
    def __init__(self, url):
        tqdm.__init__(self, unit='B', unit_scale=True, miniters=1, desc=url.rsplit('/', 1)[-1])
        self.url = url

    def record_progress(self, block_number, read_size, total_file_size):
        self.total = total_file_size
        self.update(block_number * read_size - self.n)

    def download(self, output_path):
        urllib.request.urlretrieve(self.url, filename=output_path, reporthook=self.record_progress)


def download_jdk(output_dir, url, major_version, vendor):
    jdk_dir = os.path.join(output_dir, vendor, major_version)
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


def main():
    output_dir = sys.argv[1]
    jdks = [
        ('temurin', '8', 'jdk8u402-b06', '8u402b06'),
        ('temurin', '11', 'jdk-11.0.22%2B7', '11.0.22_7'),
        ('temurin', '17', 'jdk-17.0.10%2B7', '17.0.10_7'),
        ('temurin', '21', 'jdk-21.0.2%2B13', '21.0.2_13'),
        ('corretto', '8', '8.402.08.1'),
        ('corretto', '11', '11.0.22.7.1'),
        ('corretto', '17', '17.0.10.8.1'),
        ('corretto', '21', '21.0.2.14.1'),
        ('oracle', '17', '17.0.10'),
        ('oracle', '21', '21.0.2')
    ]
    for jdk in jdks:
        vendor = jdk[0]
        url = URL_TEMPLATES[vendor].format(*jdk)
        download_jdk(output_dir, url, jdk[1], vendor)


if __name__ == '__main__':
    main()
