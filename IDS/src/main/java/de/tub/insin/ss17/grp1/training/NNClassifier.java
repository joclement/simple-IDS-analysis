package de.tub.insin.ss17.grp1.training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;


abstract public class NNClassifier implements MlAlgo {

    private double dist;

    private int k;

    private Map<String, Consumer<String>> paramDict;

    IBk nnClassifier;

    public NNClassifier(List<Param> params) {
        this.paramDict = new HashMap<>();
        this.setParams(params);
        this.nnClassifier = new IBk(this.k);
    }

    private void setParams(List<Param> params) {
        this.paramDict.put("k", this::setK);
        this.paramDict.put("dist", this::setDist);

        for (Param param : params) {
            Consumer<String> entry = this.paramDict.get(param.name);
            assert entry != null;
            entry.accept(param.value);
        }
    }

    private void setK(String k) {
        this.k = Integer.valueOf(k);
        assert this.k > 0;
    }

    private void setDist(String dist) {
        this.dist = Double.valueOf(dist);
        assert this.dist > 0;
    }

    @Override
    public void train(Instances trainingData) throws Exception {
        this.nnClassifier.buildClassifier(trainingData);
    }

    @Override
    public Classifier getClassifier() {
        return this.nnClassifier;
    }

    @Override
    public String getFilename() {
        StringBuilder filename = new StringBuilder("nearestNeighbour");

        filename.append("_k=");
        filename.append(this.nnClassifier.getKNN());

        filename.append("_n=");
        filename.append(this.nnClassifier.getNumTraining());

        return filename.toString();
    }

}
