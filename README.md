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


#### To execute the data-preparation:
1. run the jar via the command line f.x. like this
   ```
   java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
2. Use `-h | --help` to get a help message to know how to use the parameters.
3. add the scenario you want to prepare (multiple are possible):
   ` -s | --scenarios SCENARIO1, SCENARIO2, ...`
4. add the CTU13 dataset-directory:
    `-c | --ctu DIR`



#### Examples:
```
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -h
```
```
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -s 4,5,6,7,11,12 -c ../src/main/resources/TestCTU13
```

If the message "ARff files moved to: ..." is shown, the data preparation was successful.
There should be a folder named "scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false" in the current directory.



#### Optional Parameters:
1. add the destination folder, in which the results are saved: `-d | --destFolder`
    Default value: ./ (current folder)
2. set percentage of the data for the training set: `-p | --percentageTrain`
    Default value: 80
3. set the last number from option -s as the test scenario: `-t | --seperateTestScenario`
    Default value: false
4. set -r true to remove all Background Instances: `-r | --removeBackground`
    Default value: false



# TODO rework this part
### Perform intrusion detection


#### To execute the intrusion detection:
1. run the jar via the command line f.x. like this
   ```
   java -jar IDS-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
2. Use `-h | --help` to get a help message to know how to use the parameters.
3. add the arffFolder you created before:
   `-f | --arffFolder`
4. add the classifier you want to use:
   `-c | --classifier`
5. add the parameters you want to use:
   `-p | --parameters`


#### Parameter Description:
* `-h|--help`: can be used to show possible options.
* `-f | --arffFolder`: Describes the folder that contains the prepared data.
* `-c|--classifier`: Name of the classifier you want to use.
  Implemented classifiers and their names used in the commandline are the following:
  * LinearNearestNeighbourClassifier: _lnns_ (Default)
  * BallTreeNearestNeighbourClassifier: ballTreeNN
* `-p|--parameters`: Describes the parameters the classifier requires.
  * paremeters for lnns and balltree, check weka for reference
    * -k, k describes the amount of the nearest neighbours. k must be positive.
    * -I, to set inverse distweight
    * ..., for more options check weka
* `-o|--only`: can be used to specify the usage of the IDS. The options of usage are train and test.


#### Examples:
Print help message:
```
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -h
```

Using the following command you start the IDS using the prepared data `scenarios=4,5,6,7,11,12\_percentageTrain=80\_seperateTestScenario=false`, the classifier LinearNearestNeighbour with the parameters k=2 and inverse dist weighting.

```
java -jar IDS-1.0-SNAPSHOT-jar-with-dependencies.jar -f ..IDS/src/main/resource/scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false -p -k=2,-I -c lnns
```

If a message similar to this " INFO IDSCliManager: 116 - --- finished test ---" is shown,
it means the intrusion detection was successful.
In the destination folder (in this example "scenarios=4,5,6,7,11,12_percentageTrain=80_seperateTestScenario=false") there now should be a folder "model" which contains the training model and a folder "results" which contains a report and a plotted overview.



## Contributors
* Joris Clement
* Josephine Alice Krause
* Philipp Christian Nickel
* Philip Daniel Wilson
