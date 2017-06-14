package de.tub.insin.ss17.grp1.training;

import java.util.List;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.Instances;


// TODO add interface for ml algos to implement
public class NNClassifier {

    private double dist;

    private int k;

    public NNClassifier(List<Param> params) {
        this.setParams(params);
    }

    private void setParams(List<Param> params) {
    }

    private void setK(String k) {
        this.k = Integer.valueOf(k);
        assert this.k > 0;
    }

    private void setDist(String dist) {
        this.dist = Double.valueOf(dist);
        assert this.dist > 0;
    }

    public void train(Instances trainingData) {
        // TODO Auto-generated method stub
    }

}
