# Installation
To set up this artifact, perform the following steps:

1. Install Docker Engine version 23.0.0+.
   Directions for installing Docker Engine are available on
   the [Docker website](https://docs.docker.com/engine/install/).
2. Download the archived Docker image "galette-artifact-image.tgz" from the repository:
   https://doi.org/10.6084/m9.figshare.26880619.v1.
   On Linux, this image can be created using the "Dockerfile", "galette-main.zip",
   and "dacapo-23.11-chopin-small.tar" files included in the repository.
   In a directory containing these files, run the command:
   `docker build -t galette-artifact . && docker save galette-artifact | gzip > galette-artifact-image.tgz`.
3. Load the Docker image by running: `docker load -i galette-artifact-image.tgz`.
4. Start an interactive Docker container: `docker run -it -p 8080:80 galette-artifact bash`

See the provided "README.md" file for example commands that can be run to check the installation.