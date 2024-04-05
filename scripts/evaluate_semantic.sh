#!/bin/bash
readonly RESULTS_DIRECTORY=$1
readonly VENDOR=$2
readonly VERSION=$3
readonly TOOL=$4
readonly PROJECT_ROOT=$(pwd)
readonly EXPERIMENT_DIRECTORY="/experiment/katie/galette/"
readonly INFO_FILE_NAME="trial_info.json"
readonly JDK_DIRECTORY="$(pwd)/jdk/"
readonly OUTPUT_DIRECTORY="$(pwd)/out/"
readonly REPORT_FILE="$OUTPUT_DIRECTORY/report.csv"

# Write a trace for each command to standard error
set -x
# Exit immediately if any simple command fails
set -e

# Print the arguments
echo "Running: results_directory=$RESULTS_DIRECTORY, vendor=$VENDOR, version=$VERSION, tool=$TOOL"

# Copy Maven settings file
cp "$EXPERIMENT_DIRECTORY/settings.xml" "$PROJECT_ROOT"
readonly SETTINGS_FILE="$PROJECT_ROOT/settings.xml"

# Create the output directory
mkdir -p "$OUTPUT_DIRECTORY"

# Create and activate a virtual environment
python3 -m venv venv
. venv/bin/activate

# Install required Python libraries
python3 -m pip install -r "$PROJECT_ROOT/scripts/requirements.txt"

# Download Java 17
python3 "$PROJECT_ROOT/scripts/download_jdk.py" \
  --output-dir "$JDK_DIRECTORY" \
  --vendor "temurin" \
  --version "17"

# Export Java home
export JAVA_HOME="$JDK_DIRECTORY/temurin/17/"

# Build and install the project
mvn -B -q -ntp -e \
  -f "$PROJECT_ROOT" \
  -s "$SETTINGS_FILE" \
  -DskipTests install

# Download JDK for trial
python3 "$PROJECT_ROOT/scripts/download_jdk.py" \
  --output-dir "$JDK_DIRECTORY" \
  --vendor "$VENDOR" \
  --version "$VERSION"

# Export Java home
export JAVA_HOME="$JDK_DIRECTORY/$VENDOR/$VERSION/"

# Run the trial
mvn -ntp -B -e \
  -f "$PROJECT_ROOT/galette-evaluation/galette-evaluation-tools/" \
  -s "$SETTINGS_FILE" \
  -P"$TOOL" \
  -Dflow.report="$REPORT_FILE"\
  install

# Record configuration information
echo "{
  \"vendor\": \"$VENDOR\",
  \"version\": \"$VERSION\",
  \"tool\": \"$TOOL\",
  \"commit_sha\": \"$(git --git-dir "$PROJECT_ROOT/.git" rev-parse HEAD)\",
  \"branch_name\": \"$(git --git-dir "$PROJECT_ROOT/.git" rev-parse --abbrev-ref HEAD)\",
  \"remote_origin_url\": \"$(git --git-dir "$PROJECT_ROOT/.git" config --get remote.origin.url)\"
}" >"$OUTPUT_DIRECTORY/$INFO_FILE_NAME"

# Create a TAR archive of the output directory and move it to the results directory
mkdir -p "$RESULTS_DIRECTORY"

# Copy all normal files in the output directory to the results directory
find "$OUTPUT_DIRECTORY" -maxdepth 1 -type f -exec cp -t "$RESULTS_DIRECTORY" {} +