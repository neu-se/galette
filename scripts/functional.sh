#!/bin/bash
readonly RESULTS_DIRECTORY=$1
readonly VERSION=$2
readonly TOOL=$3
readonly PROJECT_ROOT=$(pwd)
readonly EXPERIMENT_DIRECTORY="/experiment/katie/galette/"
readonly RESOURCES_DIRECTORY="$(pwd)/resources/"
readonly OUTPUT_DIRECTORY="$(pwd)/out/"
readonly INFO_FILE="$RESULTS_DIRECTORY/info.json"
readonly SETTINGS_FILE="$PROJECT_ROOT/settings.xml"

# Write a trace for each command to standard error
set -x
# Exit immediately if any simple command fails
set -e

# Print the arguments
echo "Running: results_directory=$RESULTS_DIRECTORY, version=$VERSION, tool=$TOOL"

# Create the results directory
mkdir -p "$RESULTS_DIRECTORY"

# Create the output directory
mkdir -p "$OUTPUT_DIRECTORY"

# Create the resources directory
mkdir -p "$RESOURCES_DIRECTORY"

# Record configuration information
echo "{
  \"version\": \"$VERSION\",
  \"tool\": \"$TOOL\",
  \"commit_sha\": \"$(git --git-dir "$PROJECT_ROOT/.git" rev-parse HEAD)\",
  \"branch_name\": \"$(git --git-dir "$PROJECT_ROOT/.git" rev-parse --abbrev-ref HEAD)\",
  \"remote_origin_url\": \"$(git --git-dir "$PROJECT_ROOT/.git" config --get remote.origin.url)\"
}" >"$INFO_FILE"

# Copy Maven settings file
cp "$EXPERIMENT_DIRECTORY/settings.xml" "$SETTINGS_FILE"

# Create and activate a virtual environment
python3 -m venv venv
. venv/bin/activate

# Install required Python libraries
python3 -m pip install -r "$PROJECT_ROOT/scripts/simple_requirements.txt"

# Run the script
python3 "$PROJECT_ROOT/scripts/functional.py" \
  --output-dir "$OUTPUT_DIRECTORY" \
  --version "$VERSION" \
  --tool "$TOOL" \
  --resources-dir "$RESOURCES_DIRECTORY" \
  --settings-file "$SETTINGS_FILE"


# Copy all normal files in the output directory to the results directory
find "$OUTPUT_DIRECTORY" -maxdepth 1 -type f -exec cp -t "$RESULTS_DIRECTORY" {} +