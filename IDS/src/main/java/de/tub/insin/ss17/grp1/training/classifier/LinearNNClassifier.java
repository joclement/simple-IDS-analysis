package de.tub.insin.ss17.grp1.training.classifier;

import java.util.List;

import weka.core.neighboursearch.LinearNNSearch;


/**
 * Wrapper class for the weka linear nearest neighbor classifiier.
 *
 * @author Joris Clement
 *
 */
public class LinearNNClassifier extends NNClassifier {

    public LinearNNClassifier(List<String> params) {
        super(params);
        this.setSearchAlgo();
    }

    private void setSearchAlgo() {
        LinearNNSearch linearSearch = new LinearNNSearch();
        this.nnClassifier.setNearestNeighbourSearchAlgorithm(linearSearch);
    }

}
