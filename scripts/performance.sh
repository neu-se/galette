#!/bin/bash
readonly RESULTS_DIRECTORY=$1
readonly TOOL=$2
readonly BENCHMARK=$3
readonly TIMEOUT=$4
readonly PROJECT_ROOT=$(pwd)
readonly EXPERIMENT_DIRECTORY="/experiment/katie/galette/"
readonly RESOURCES_DIRECTORY="$(pwd)/resources/"
readonly OUTPUT_DIRECTORY="$(pwd)/out/"
readonly INFO_FILE="$RESULTS_DIRECTORY/info.json"
readonly SETTINGS_FILE="$PROJECT_ROOT/settings.xml"
readonly DACAPO_ARCHIVE="$PROJECT_ROOT/dacapo-23.11-chopin-small.tar"

# Write a trace for each command to standard error
set -x
# Exit immediately if any simple command fails
set -e

# Print the arguments
echo "Running: results_directory=$RESULTS_DIRECTORY, benchmark=$BENCHMARK, tool=$TOOL"

# Create the results directory
mkdir -p "$RESULTS_DIRECTORY"

# Create the output directory
mkdir -p "$OUTPUT_DIRECTORY"

# Create the resource directory
mkdir -p "$RESOURCES_DIRECTORY"

# Record configuration information
echo "{
  \"benchmark\": \"$BENCHMARK\",
  \"tool\": \"$TOOL\",
  \"commit_sha\": \"$(git --git-dir "$PROJECT_ROOT/.git" rev-parse HEAD)\",
  \"branch_name\": \"$(git --git-dir "$PROJECT_ROOT/.git" rev-parse --abbrev-ref HEAD)\",
  \"remote_origin_url\": \"$(git --git-dir "$PROJECT_ROOT/.git" config --get remote.origin.url)\"
}" >"$INFO_FILE"

# Copy Maven settings file
cp "$EXPERIMENT_DIRECTORY/settings.xml" "$SETTINGS_FILE"

# Copy DaCapo archive
cp "$EXPERIMENT_DIRECTORY/dacapo-23.11-chopin-small.tar" "$DACAPO_ARCHIVE"

# Create and activate a virtual environment
python3 -m venv venv
. venv/bin/activate

# Install required Python libraries
python3 -m pip install -r "$PROJECT_ROOT/scripts/requirements.txt"

# Run the script
python3 "$PROJECT_ROOT/scripts/performance.py" \
  --output-dir "$OUTPUT_DIRECTORY" \
  --benchmark "$BENCHMARK" \
  --tool "$TOOL" \
  --resources-dir "$RESOURCES_DIRECTORY" \
  --dacapo-archive "$DACAPO_ARCHIVE" \
  --timeout "$TIMEOUT" \
  --settings-file "$SETTINGS_FILE"

# Copy all normal files in the output directory to the results directory
find "$OUTPUT_DIRECTORY" -maxdepth 1 -type f -exec cp -t "$RESULTS_DIRECTORY" {} +