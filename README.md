# Project InSiN, Group 1
This project is about the development of an intrusion detection system(IDS)
in the course _Intelligente Sicherheit in Netzwerken_
at the _Technische Universitaet Berlin_ in the summer term 2017.
We are _group 1_. The members(contributors) are listed below.<br/>
More precisely, the goal is to develop an IDS on a given dataset, in our case
[CTU-13](http://mcfp.weebly.com/the-ctu-13-dataset-a-labeled-dataset-with-botnet-normal-and-background-traffic.html),
with a number of self chosen machine learning algorithms.
We focus on the K-Nearest-Neighbor algorithm, using 2 ways for searching through
the trained instances, linear search and ball tree.
For comparison there is the j48 decision tree available.

## Basic description
This section focuses on how to use our project. We divided the project into two
sections.
The first section, _data-preparation_ ,has the purpose of converting the CTU dataset to
[arff](https://weka.wikispaces.com/ARFF) files,
which can then be used by [weka](http://www.cs.waikato.ac.nz/ml/weka/)
for the IDS.
The second one, _IDS_ ,is the IDS.
In this part, the training and testing of our algorithm on the data to detect
anomalies is done.


## How-To
You can use both of these sections using a jar via the command line.
Therefore, you have to clone this project.
Run ```mvn package``` and use the existing jars. They should be in the
```targets``` folder.
They are described as follows.


### Convert data from CTU-13 to Arff

#### To execute the data-preparation:
1. run the jar via the command line f.x. like this:
   ```
   java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
2. This program(jar) takes mandatory and optional parameters.
  * Use `-h | --help` to get a help message and view all parameters,
    how to use them and their default values.
  * Required Parameters:
     * Add the scenario you desire to convert (multiple are possible):
       `-s | --scenarios SCENARIO1, SCENARIO2, ...`
  * Optional Parameters:
      * Add the CTU13 dataset-directory:
        `-c | --ctu DIR`
      * Add the destination folder, in which the results are saved: `-d | --destFolder`
      * Set percentage of the data for the training set: `-p | --percentageTrain`
      * Set the last number from option `-s` as the test scenario: `-t | --separateTestScenario`
      * Use `-r` to remove all Background Instances: `-r | --removeBackground`


#### Examples:
```
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -h
```
```
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -s 4,5,6,7,11,12 -c ../src/main/resources/TestCTU13
```

If the message "Arff files moved to: /exampleDirectory" is shown, the data preparation was successful.
There should be a folder named "scenarios=4,5,6,7,11,12_percentageTrain=80_separateTestScenario=false" in the current directory.



### Perform intrusion detection

#### To execute the intrusion detection:
1. run the jar via the command line f.x. like this
   ```
   java -jar IDS-1.0-SNAPSHOT-jar-with-dependencies.jar
   ```
2. This program(jar) takes mandatory and optional parameters.
  * Use `-h | --help` to get a help message and view the parameters,
    how to use them and their default values.
  * Required Parameters:
      * add the arffFolder you created before: `-f | --arffFolder`
  * Optional Parameters:
      * add the classifier you want to use: `-c | --classifier`
      * add the parameters for the classifier: `-p | --parameters`
        check WEKA for the parameters you can use. Separate them by comma. Links:
        for [linear search and ball tree search nearest neighbor classifier](http://weka.sourceforge.net/doc.dev/weka/classifiers/lazy/IBk.html),
        for [j48 decision tree](http://weka.sourceforge.net/doc.dev/weka/classifiers/trees/J48.html)
      * `-o | --only`: can be used to specify the usage of the IDS. The options of usage are train and test.
      * `-n | --nominal`: can be used to specify a list of columns in the arff folder,
                          which should be converted from numeric to nominal.


#### Examples:
Print help message:
```
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -h
```

Using the following command you start the IDS using the prepared data `scenarios=4,5,6,7,11,12\_percentageTrain=80\_separateTestScenario=false`, the classifier LinearNearestNeighbour with the parameters k=2 and inverse dist weighting.
The columns 3 and 6 in the training and test arff file will be converted from
numeric to nominal. If the arff files are prepared using our tool, then column 3
and 6 should be the ip address of the source and the destination.

```
java -jar IDS-1.0-SNAPSHOT-jar-with-dependencies.jar -f ./scenarios=4,5,6,7,11,12_percentageTrain=80_separateTestScenario=false -p -k=2,-I -c lnns -n 3,6
```
If a message similar to this " INFO IDSCliManager: 116 - --- finished test ---" is shown,
it means the intrusion detection was successful.
In the destination folder (in this example "scenarios=4,5,6,7,11,12_percentageTrain=80_separateTestScenario=false") there now should be a folder "model" which contains the training model and a folder "results" which contains a report and a plotted overview.

The following uses the same data, but just trains the default classifier with no
parameters. So there will a model for the linear search nearest neighbor
classifier in the folder model.
```
java -jar IDS-1.0-SNAPSHOT-jar-with-dependencies.jar -f ./scenarios=4,5,6,7,11,12_percentageTrain=80_separateTestScenario=false --only train
```



## JavaDoc
If you want to generate the javadoc for this project,
you can do that executing the command `mvn javadoc:javadoc`.
The resulting files will be in the 2 subprojects(data-preparation, IDS) in their
folder `target/site/apidocs`.



## Contributors
* Joris Clement
* Josephine Alice Krause
* Philipp Christian Nickel
* Philip Daniel Wilson
