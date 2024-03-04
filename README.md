Inventor Disambiguation (patent applicants)
========================

This project disambiguates individual inventors across their patent applicantions (both granted and ungranted applications). This project is branched from the coreference model for patent grantees (https://github.com/iesl/inventor-disambiguation). 

Build instructions are below:


## Overview ##

Build:

```bash
cd inventor-disambiguation
mvn clean package
```

Run:

```bash
# Load the data
sh scripts/db/populate_mongo_db.sh
# Preprocess, create records to disambiguate
sh scripts/db/generate_inventor_mentions.sh
# Compute blocking/canopy assignments
sh scripts/coref/generate_coref_tasks.sh
# Run disambiguation
time ./scripts/coref/run_disambiguation.sh config/coref/RunConfig.config config/coref/WeightsCommonCharacteristics.config
# Post-process clean up inventor names
./scripts/process/post-process-remove-stopwords.sh data/multi-canopy-output/all-results.txt data/multi-canopy-output/all-results.txt.post-processed
```

## Usage ##

There are several stages of running the disambiguation algorithm: 

1. Preprocess the data
2. Load data into Mongo
3. Create “InventorMentions” (the records to disambiguate)
4. Create “CorefTasks” (a way to parallelize the processing)
5. Run disambiguation.
6. Post process inventor names

The following section provides a step-by-step guide to running the disambiguation system. It provides steps for running the entire processing pipeline from starting with the raw csv and tsv files and ending with the submitted results.

### Loading and Preprocessing the Patent Data ###

#### Downloading the raw data and preprocessing####

Download the following .dta files from the 2014 data export from USPTO’s Patent Examination Research Dataset (PatEx) (https://www.uspto.gov/ip-policy/economic-research/research-datasets/patent-examination-research-dataset-public-pair) and place them in the ```data``` directory:
* application_data.dta
* all_inventors.dta
* correspondence_address.dta

Download the following .dta files from the 2014 data export of USPTO’s Patent Assignment Dataset (https://www.uspto.gov/ip-policy/economic-research/research-datasets/patent-assignment-dataset) and place them in the ```data``` directory:
* assignee.dta
* documentid_admin.dta

Then, run the ```./scripts/stata/disambiguation_full_gen.do``` file in Stata to generate the input files for this disambiguation. That code generates the following files:
* patent_full.tsv
* rawassignee_full.tsv
* rawinventor_full.tsv
* rawlocation_full.tsv
* rawlawyer_full.tsv
* uspc_current_full.tsv

Place these files in the ```data``` directory.


#### Starting the MongoDB Server ####

The following script is used to start the Mongo server:

```bash
sh scripts/db/start_mongo_server.sh
```

The port used is 27972, if this port is not available, please choose a different port and update all of the config files in the config directory with your new port number.

#### Loading the Data ####

The data is loaded into MongoDB using parallelization. Run the following script to load all of the data into mongo:

```bash
sh scripts/db/populate_mongo_db.sh
```

#### Creating Inventor Mentions ####

The input to our algorithm is a combination of data related to each row in the raw inventor table. We refer to this combination of related data an inventor mention. The inventor mentions in our database collect the patent, patent classification, co-inventor, assignee, lawyer, etc needed for disambiguation. This step also uses multiple threads. To generate the inventor mentions:

```bash
sh scripts/db/generate_inventor_mentions.sh
```


#### Creating the Coreference Task File ####

The coreference algorithm also takes a partitioning of the data into blocks or canopies. This allows us to easily parallelize the algorithm. To generate this input file run the following script:

```bash
sh scripts/coref/generate_coref_tasks.sh
```
The output of this script is a file, coref-tasks.tsv in the data directory. 
The output file is a two column tab separated file sorted by the number of mentions in the canopy:

```
LAST_jahns_FIRST_ekk 5889111-1,5596051-0,8399579-2,8304075-1,7316994-11,6683126-2
LAST_fleischmann_FIRST_tho      6780466-2,7880655-1,6499606-2,8108167-0
LAST_xue_FIRST_erz      6515146-6,5989457-3
LAST_nagone_FIRST_yui   6977872-0
```

#### Running Disambiguation ####

The command to run the disambiguation with the parameter setting is: 

```
time ./scripts/coref/run_disambiguation.sh config/coref/RunConfig.config config/coref/WeightsCommonCharacteristics.config
```

The results of this script will be stored in 

```
data/multi-canopy-output/all-results.txt
```

#### Post-processing of Inventor Names ####

There is also a post-processing script that cleans up inventor names removing words such as “deceased”. To run this script:

```bash
./scripts/process/post-process-remove-stopwords.sh <input-file> <output-file>
# For example
./scripts/process/post-process-remove-stopwords.sh \
    data/multi-canopy-output/all-results.txt \
    data/multi-canopy-output/all-results.txt.post-processed
```

## Contact ##

Please email Gauri Subramani (gsubramani@lehigh.edu)
