# Artifact for "Dynamic Taint Tracking for Modern Java Virtual Machines"

## Purpose

This repository contains the artifact for the paper "Dynamic Taint Tracking for Modern Java Virtual
Machines".
This artifact includes the scripts needed to replicate the evaluation described in the paper,
the source code for the Galette dynamic taint tracking system, and the raw experiment data analyzed in the paper.
Due to the licensing for MirrorTaint, we were not able to include the materials necessary to
reproduce the results collected for MirrorTaint.
The purpose of these materials is to facilitate future research into program analysis in the Java Virtual Machine
and to allow researchers to independently obtain the empirical results reported in the paper
"Dynamic Taint Tracking for Modern Java Virtual Machines".

## Data

This repository includes the raw experiment data for the results presented in
"Dynamic Taint Tracking for Modern Java Virtual Machines"
The included data files are described below.

### functional.csv

The file "functional.csv" contains the results of each taint tracking system on the functional benchmark suite
using each JDK.
Each row reports the results of a tainting system on a single benchmark test on a specific JDK.
Each row contains the taint tracking system used (tool), the JDK version used (version),
the identifier of a benchmark test (test), the group of that test (group),
the number of true positives reported by the specified tool for the specified test on the specified JDK (tp),
the number of false positives reported (fp), the number of false negatives reported (fn),
the status of the test run (status), the result of the test (result),
and the identifier of the trial dataset in which this result was reported (trial_id).

The number of true positives, false positives, and false negatives are computed based on the manually determined
ground truth expected label sets and the actual set of labels propagated by the taint tracking system.
Labels in both the propagated and expected sets are counted as true positives.
Labels in the expected set, but not the propagated set are counted as false negatives.
Labels in the propagated set, but not the expected set are counted as false positives.

The status of a test run can be either: "success", "timeout", "crash", "abort", "fail", or "vm-crash".
The status "success" means that the test ran without error and all assertions passed.
The status "timeout" means that the test did not complete within 10 minutes.
The status "crash" means that an error occurred trying to run the test.
The status "abort" means that the test was aborted by the test runner due to a failed assumption.
The status "fail" means that a failure occurred during the execution of the test either due to a failed assertion
or an unhandled, unexpected exception being thrown.
The status "vm-crash" means that the JVM could not even be started.

The result of a test can either be "semantic", "tag", or "success".
The result of a test is "semantic", indicating a deviation from the original program semantics,
if its status is not "success".
The result of a test is "tag", indicating incorrect taint tag propagation, if its status is "success" and at least
one false positive or negative was reported.
Otherwise, the result of a test is "success".

Consider the following row:

```
tool,version,test,group,tp,fp,fn,status,result,trial_id
...
galette,8,"[engine:junit-jupiter]/[class:edu.neu.ccs.prl.galette.bench.ArrayAccessITCase]/[test-template:getSetElement(java.lang.Class, boolean, boolean, boolean)]/[test-template-invocation:#2]",ArrayAccess,2,0,0,success,success,0
```

This row indicates that for the test
"[engine:junit-jupiter]/[class:edu.neu.ccs.prl.galette.bench.ArrayAccessITCase]/[test-template:getSetElement(java.lang.Class, boolean, boolean, boolean)]/[test-template-invocation:#2]"
the tool "galette" reported three true positives, zero false positives, and zero false negatives on JDK version "8".
It also indicates the test ran without error producing in an overall result of "success".

### performance.csv

Each row in the file "performance.csv" reports the values recorded for a measurement iteration
for a tainting system on a single performance benchmark.
Each row contains the iteration number for the measurement (iteration),
the maximum RSS sampled during the iteration in kilobytes (rss),
the elapsed real time taken to complete the iteration in milliseconds (elapsed_time),
the name of the benchmark that was run (benchmark),
the taint tracking system used (tool),
and the identifier of the trial dataset in which this result was reported (trial_id).

Measurement iterations are between five and nine, inclusive.
As discussed in the paper, iterations zero through four are warm-up iterations;
results for these iterations are discarded.

Consider the following row:

```
iteration,rss,elapsed_time,benchmark,tool,trial_id
7,728072,5057,jme,galette,533
```

This row indicates that the tool "galette" has an execution time of "5057" milliseconds and a maximum RSS of "728072"
kilobytes on iteration "7" of trial "533" on the benchmark "jme".

## Setup

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

## Usage

The following directions assume that you have started a docker container according to the directions provided above.
All commands are run from the "home" directory of that docker container.

### Running a Functional Trial

To collect results for the functional evaluation (RQ1 and R2 in the paper), for a particular taint tracking system
on a particular JDK run:

```shell
python3 scripts/functional.py \
  --output-dir OUTPUT_DIRECTORY \
  --version VERSION \
  --tool TOOL \
  --resources-dir /home/resources/ \
  --skip-build
```

Where:

* \<OUTPUT_DIRECTORY\> is the absolute path of the directory to which output files should be written.
* \<VERSION\> is the JDK version that should be used: 8, 11, 17, or 21.
* \<TOOL\> is the taint tracking system to be used: galette or phosphor.

This command will run each of the tests in the functional benchmark suite with the specified taint tracking system
on the specified JDK.
The results of each test will be printed to the console.
Once this command has completed, the output directory will contain the following files:

```
├── status.json
└── data.csv
```

The file "status.json" contains information about the tool and version that were used for this trial.
This file also contains the status of the trial.
If the trial process terminated successfully, this status will be "SUCCESS".
If an error occurred building a project, the status "BUILD_FAILURE" will be reported.
If an error occurred running the trial process, "RUN_FAILURE" will be reported.

The file "data.csv" contains the results of the trial.
The format of this file is similar to that of the data file "functional.csv" as described above without the
"trial_id" column.

#### Example

To test this command, run a trial for Galette on JDK 11:

```shell
python3 scripts/functional.py \
  --output-dir /home/results/functional-galette-8 \
  --version 11 \
  --tool galette \
  --resources-dir /home/resources/ \
  --skip-build
```

### Running a Performance Trial

To run a trial for the performance evaluation (RQ3 and R4 in the paper), for a particular taint tracking system
on a particular benchmark:

```shell
python3 scripts/performance.py \
  --output-dir OUTPUT_DIRECTORY \
  --benchmark BENCHMARK \
  --tool TOOL \
  --timeout TIMEOUT \
  --dacapo-archive /home/resources/dacapo-23.11-chopin-small.tar \
  --resources-dir /home/resources/ \
  --skip-build
```

Where:

* \<OUTPUT_DIRECTORY\> is the absolute path of the directory to which output files should be written.
* \<BENCHMARK\> is the DaCapo benchmark that should be used: avrora, batik, biojava, eclipse, fop, graphchi, h2, h2o,
  jme, jython, luindex, lusearch, pmd, spring, sunflow, tomcat, tradebeans, tradesoap, xalan, or zxing.
* \<TOOL\> is the taint tracking system to be used: galette, phosphor, or none.
* \<TIMEOUT\> is the maximum amount of time to run the DaCapo process for in minutes

This command will run the specified DaCapo benchmark for ten iterations with the specified taint tracking system until
the specified timeout has elapsed or the tenth iteration completes.
Once this command has completed, the output directory will contain the following files:

```
├── status.json
└── data.csv
```

The file "status.json" contains information about the tool and benchmark that were used for this trial.
This file also contains the status of the trial.
If the trial process terminated successfully, this status will be "SUCCESS".
If an error occurred building a project, the status "BUILD_FAILURE" will be reported.
If an error occurred running the trial process, "RUN_FAILURE" will be reported.
If the trial process was timed out before the benchmark completed, "TIMEOUT" will be reported.

The file "data.csv" contains the results of the trial.
The format of this file is similar to that of the data file "performance.csv" as described above without the
"trial_id" column.

We advise against running the "tradebeans" or "tradesoap" benchmarks with Phosphor with a large timeout.
Deviations from these benchmarks' normal behavior introduced by Phosphor cause the benchmarks to loop
infinitely producing errors which are recorded to a log file.
The size of this log file grows to be rather large for larger timeouts.

#### Example

To test this command, run a trial for Galette on the "sunflow" benchmark:

```shell
python3 scripts/performance.py \
  --output-dir /home/results/performance-galette-sunflow \
  --benchmark sunflow \
  --tool galette \
  --dacapo-archive /home/resources/dacapo-23.11-chopin-small.tar \
  --resources-dir /home/resources/ \
  --skip-build
```

Now run a trial without a taint tracking system on the "sunflow" benchmark:

```shell
python3 scripts/performance.py \
  --output-dir /home/results/performance-none-sunflow \
  --benchmark sunflow \
  --tool none \
  --dacapo-archive /home/resources/dacapo-23.11-chopin-small.tar \
  --resources-dir /home/resources/ \
  --skip-build
```

### Creating a Report

After one or more trials have been completed, you can create a report
summarizing the results of those trials.
Note that you must have results for the baseline ("none") and Galette for any performance trials for this to work
properly.
First, create an input directory containing the results of the trials to be included in the report.
This input directory should contain one subdirectory for each trial to be included in the report.
Each of these subdirectories should contain the "data.csv" and "status.json" output files for a
trial (see ["Running a Functional Trial"](#Running-a-Functional-Trial) and
["Running a Performance Trial"](#Running-a-Performance-Trial) for information about these files).
For example, the following is a valid structure for the input directory:

```
├── trial-0
│   ├── data.csv
│   └── status.json
└── trial-1
    ├── data.csv
    └── status.json
```

To create the report run:

```shell
python3 scripts/report.py INPUT_DIRECTORY OUTPUT_FILE
```

Where:

* \<INPUT_DIRECTORY\> is the path of the input directory you created.
* \<OUTPUT_FILE\> is the path of the file to which the report should be written in HTML format.

#### Example

To test this command, create a report for the trials you ran above:

```shell
python3 scripts/report.py /home/results/ report.html

```

The easiest way to view the created report from within the container is to run:

```shell
python3 -m http.server 80

```

Then, open the page "http://localhost:8080/report.html" in a browser.

To create a report using the raw experimental data analyzed in the paper run:

```shell
python3 scripts/report.py /home/data/ report2.html

```

Next, run `python3 -m http.server 80` and open the page "http://localhost:8080/report2.html" in a browser.

### Replicating the Paper Results

To replicate the results reported in the paper, you would need to run one functional trial for each tool on each JDK
version.
These trials can be run using the directions given in ["Running a Functional Trial"](#Running-a-Functional-Trial).
You also need to run twenty performance trials for each tool on each DaCapo benchmark.
These trials can be run using the directions given in ["Running a Performance Trial"](#Running-a-Performance-Trial).
The output from these trials should be collected into a single directory.
Finally, a report can be created using the directions given in ["Creating a Report"](#creating-a-report).

To run all the required trials and produce the final report using a single command, run:

```shell
python3 scripts/replicate.py \
  --output-dir /home/replicate \
  --timeout 1440
  --dacapo-archive /home/resources/dacapo-23.11-chopin-small.tar \
  --resources-dir /home/resources/ \
  --skip-build
```

This command will take a significant amount of time to run on one machine.
Note that this command skips running the "tradebeans" or "tradesoap" benchmarks with Phosphor because of the issues
discussed above.
This command will produce a report file "/home/replicate/report.html"
The easiest way to view the created report from within the container is to run:

```shell
python3 -m http.server 80

```

Then, open the page "http://localhost:8080/replicate/report.html" in a browser.
As noted above, this report will not contain results for MirrorTaint due to license issues.