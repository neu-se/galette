import argparse
import os
import shutil

import requests
from tqdm import tqdm

DACAPO_URL_TEMPLATE = 'https://download.dacapobench.org/chopin/data/{benchmark}-data.zip'
DACAPO_SUBJECTS = ['avrora', 'batik', 'biojava', 'cassandra', 'eclipse', 'fop', 'graphchi', 'h2', 'h2o', 'jme',
                   'jython', 'kafka', 'luindex', 'lusearch', 'pmd', 'spring', 'sunflow', 'tomcat', 'tradebeans',
                   'tradesoap', 'xalan', 'zxing']


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


def download_dacapo_data(output_dir, benchmark):
    url = DACAPO_URL_TEMPLATE.format(benchmark=benchmark)
    os.makedirs(output_dir, exist_ok=True)
    file_name = url.rsplit('/', 1)[-1]
    # Download the archive
    archive = os.path.join(output_dir, file_name)
    with Downloader(url) as d:
        d.download(archive)
    extract_dir = os.path.join(output_dir, benchmark)
    # Unpack the archive
    shutil.unpack_archive(archive, extract_dir)
    # Remove the archive
    os.remove(archive)


def main():
    parser = argparse.ArgumentParser(description='Downloads the dataset for a DaCapo Benchmark.')
    parser.add_argument('--output-dir', type=str,
                        help='Path of the directory into which the resource to be downloaded should be stored.',
                        required=True)
    parser.add_argument('--benchmark', help='Name of the DaCapo benchmark.',
                        choices=['batik', 'graphchi', 'h2o', 'luindex', 'lusearch', 'sunflow'],
                        required=True)
    args = parser.parse_args()
    download_dacapo_data(**args.__dict__)


if __name__ == '__main__':
    main()
