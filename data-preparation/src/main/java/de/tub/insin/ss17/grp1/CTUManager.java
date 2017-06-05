package de.tub.insin.ss17.grp1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

public class CTUManager {

	private final File datasetDir;

	private final String filename;

	public CTUManager(String datasetDir, String filename){
		this.datasetDir = new File(datasetDir);
		this.filename = filename;
	}

	public List<File> find(List<Integer> scenarios) throws FileNotFoundException {
		List<File> files = new LinkedList<File>();
		for(Integer scenario : scenarios){
			files.add(find(scenario));
		}

		return files;
	}

	private File find(Integer scenario) throws FileNotFoundException {
		String pathInDataset = scenario.toString() + File.separator + this.filename;
		File file = new File(datasetDir, pathInDataset);
		if(!file.exists()){
			throw new FileNotFoundException();
		}
		return file;
	}

}
