# Extract the repository archive
FROM busybox AS extract
WORKDIR /extract
COPY galette-main.zip .
RUN unzip galette-main.zip
# Use Maven base image
FROM maven:3.9.6-eclipse-temurin-17-focal
WORKDIR /home
# Copy extracted repository
COPY --from=extract /extract/galette-main/. .
# Copy dacapo archive
COPY dacapo-23.11-chopin-small.tar /home/resources/
# Download python and pip
RUN apt-get update && \
  apt-get install -y python3.9 python3-pip
# Set the default python version to 3.9
RUN update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.9 1 && \
  update-alternatives --config python3
# Upgrade pip
RUN python3 -m pip install --upgrade --force-reinstall pip
# Install python requirements
RUN python3 -m pip install -r ./scripts/requirements.txt
# Download JDKs
RUN for version in 8 11 17 21; \
    do python3 ./scripts/download_jdk.py \
    --vendor temurin \
    --version "$version" \
    --output-dir "/home/resources/jdk/temurin/$version"; \
    done
# Export JAVA_HOME
RUN export JAVA_HOME=/home/resources/jdk/temurin/17/
# Build the project
RUN mvn -DskipTests install
# Build the evaluation classes
RUN mvn -DskipTests install -f galette-evaluation
# Expose port 80
EXPOSE 80/tcp