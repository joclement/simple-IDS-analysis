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
    -s | --scenarios 4,5,6,7,11,12
3. add the CTU13 dataset-directory:
    -c | --ctu ../src/main/resources/TestCTU13/

Example:
java -jar data-preparation-1.0-SNAPSHOT-jar-with-dependencies.jar -s 6,11,12 -c ../src/main/resources/TestCTU13

If the message "ARff files moved to: " is shown, the data-preparation was successful. There should be a folder named "scenarios=6,11,12_percentageTrain=80_seperateTestScenario=false" in the directory data-preparation/IDS/src/main/resources .

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

#### Example command
Just an example before we describe all the parameters.

```
java -jar de.tub.insin.grp1.
```

#### Parameter Description
There are a couple of parameters you can/must use. In the java source code they
are specified in the class ```IDSCliManager.java```.

#### More examples

# TODO Someone Write this part

## Contributors
* Joris Clement
* Josephine Alice Krause
* Philipp Christian Nickel
* Philip Daniel Wilson
