package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;

public class CliManager {

	private String filename = "netflow.csv";

	private String datasetDir = "./data/sets/CTU-13/";

	@Parameter
	private List<String> parameters = new LinkedList<String>();

	@Parameter(names = "-integers")
	private List<Integer> integers;

	@Parameter(names = { "--percentageTrain", "-per" }, description = "Percentage of the data for the training set")
	private Integer percentageTrain = 80;

	@Parameter(names = { "--numOfFolds", "-folds" }, description = "number of folds for cross-validation")
	private Integer numOfFolds = 5;

	@Parameter(names = { "--seperateTestScenario", "-sep" }, description = "Use the last number from the options -integers as the test scenario")
	private boolean seperateTestScenario = false;

	public void run() {
		CTUManager ctuManager = new CTUManager(datasetDir, filename);

		try {
			List<File> files = ctuManager.find(integers);
			CSV2ArffConverter.parse(files, generateDestFilepath());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private File generateDestFilepath() {
		// TODO implement
		return null;
	}
}
