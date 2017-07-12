# Project InSiN, Group 1

This project is developed an intrusion detection system(IDS)
in the course _Intelligente Sicherheit in Netzwerken_
at the _Technische Universitaet Berlin_ in the summer term 2017.
We are _group 1_. The members(contributors) are listed below.<br/>
More precisely, the goal is to develop an IDS on a given dataset, in our case
[CTU-13](http://mcfp.weebly.com/the-ctu-13-dataset-a-labeled-dataset-with-botnet-normal-and-background-traffic.html),
with a number of self chosen machine learning algorithms.
We focus on the K-Nearest-Neighbor algorithm, but will also implement and test
some others.

## Basic description
This section focuses on hot to use our project. We divided the project into two
parts.
The first part _data-preparation_ has the purpose to convert the CTU dataset to
[arff](https://weka.wikispaces.com/ARFF) files,
which can then be used by [weka](http://www.cs.waikato.ac.nz/ml/weka/)
for the IDS system.
The second part _IDS_ is the IDS system.
In that part the training and testing of our algorithm on the data to detect
anomalies is done.

## How-To

You can use both parts using a jar via the command line.
Therefore you have to clone this project.
Run ```mvn package``` and use the existing jars. They should be in the
```targets``` folder.
They are described in the following.

### Convert data from CTU-13 to Arff

To run the data-preparation 
1. enter java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar
2. add the scenario you want to prepare (multiple are possible): 
    -s | --scenarios 
3. add the CTU13 dataset-directory:
    -c | --ctu ../src/main/resources/TestCTU13/

Example:
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -s 4,5,6,7,11,12 -c ../src/main/resources/TestCTU13

If the message "ARff files moved to: " is shown, the data-preparation was successful. There should be a folder named "scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false" in the directory data-preparation/IDS/src/main/resources .

Optional Parameters:
1. add the destination folder, in which the sults are saved: -d | --destFolder
    Default value: ../IDS/src/main/resources/
2. set percentage of the data for the training set: -p | --percentageTrain
    Default value: 80
3. set the last number from option -s as the test scenario: -t | --seperateTestScenario
    Default value: false
4. set -r true to remove all Background Instances: -r | --removeBackground
    Default value: false

### Perform intrusion detection
To Start the intrusion detection 

1. Move to the folder IDS/target 
2. enter: java -jar IDS-1.0-SNAPSHOT-jar -with-dependencies.jar 
3. add the arffFolder you created before: 
-f|--arffFolder 
4. add the parameters you want to use: 
-p|--parameters
5. add the classifier you want to use: 
-c|--classifierName

Parameter Description: 
-f|--arffFolder: Describes the folder that contains the prepared data.
-p|--parameters: Describes the parameters the classifier requires. 
The parameters are k and distweight. k describes the amount of the nearest neighbours while distweight describes how the distance between the neighbours is weighed. The options for distweight are none, inverse and similarity. k should be a positive integer
-c|--classifierName: Name of the classifier you want to use. Implemented classifiers and their names used in the commandline are the following:
	LinearNearestNeighbourClassifier: lnns (Default)
	BallTreeNearestNeighbourClassifier: ballTreeNN

Example:
Using the IDS on the Data prepared in the example of the Data Preparation:

java -jar IDS-1.0-SNAPSHOT-jar-with-dependencies.jar -f ..IDS/src/main/resource/scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false -p k=2,distweight=inverse –c lnns

##TODO (default ggf. Anpassen)
You have to use the absolute path name of your destination folder of the Data Preparation. The destination folder is per default the folder you are currently using. If you saved your arff folder there, you need to use the absolute path. 

Using this command you start the IDS using the prepared data “scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false”, the classifier LinearNearestNeighbour with the parameters k=2 and distweight=inverse.

If the message " INFO IDSCliManager: 116 - --- finished test ---" is shown, it means the intrusion detection was successful.

In the destination folder (in this example "scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false") there now should be a folder "model" which contains the training model and a folder "results" which contains a report and a plotted overview.

Optional:
-h|--help: can be used to show possible options.
-o|--only: can be used to specify the usage of the IDS. The options of usage are train and test.  






## Contributors
* Joris Clement
* Josephine Alice Krause
* Philipp Christian Nickel
* Philip Daniel Wilson
