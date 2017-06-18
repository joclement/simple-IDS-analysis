package de.tub.insin.ss17.grp1.training;

import java.util.List;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.neighboursearch.LinearNNSearch;

public class LinearNNClassifier extends NNClassifier {

    public LinearNNClassifier(List<Param> params) {
        super(params);
        this.setSearchAlgo();
    }

    private void setSearchAlgo() {
        LinearNNSearch linearSearch = new LinearNNSearch();
        this.nnClassifier.setNearestNeighbourSearchAlgorithm(linearSearch);
    }

}
