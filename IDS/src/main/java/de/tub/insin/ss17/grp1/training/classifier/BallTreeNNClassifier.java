package de.tub.insin.ss17.grp1.training.classifier;

import java.util.List;

import weka.core.neighboursearch.BallTree;


/**
 * Class for the Ball Tree nearest neighbor classifier
 *
 * @author Joris Clement
 *
 */
public class BallTreeNNClassifier extends NNClassifier {

    public BallTreeNNClassifier(List<String> params) {
        super(params);
        this.setSearchAlgo();
    }

    private void setSearchAlgo() {
        BallTree ballTree = new BallTree();
        this.nnClassifier.setNearestNeighbourSearchAlgorithm(ballTree);
    }
}
