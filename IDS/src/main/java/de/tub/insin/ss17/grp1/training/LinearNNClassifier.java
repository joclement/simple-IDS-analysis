package de.tub.insin.ss17.grp1.training;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.neighboursearch.LinearNNSearch;


public class LinearNNClassifier extends NNClassifier {

    private final static Logger log = LoggerFactory.getLogger(LinearNNClassifier.class);

    public LinearNNClassifier(List<Param> params) {
        super(params);
        log.debug("start: LinearNNClassifier");
        this.setSearchAlgo();
        log.debug("finished: LinearNNClassifier");
    }

    private void setSearchAlgo() {
        log.debug("start: setSearchAlgo");
        LinearNNSearch linearSearch = new LinearNNSearch();
        this.nnClassifier.setNearestNeighbourSearchAlgorithm(linearSearch);
        log.debug("finished: setSearchAlgo");
    }

}
