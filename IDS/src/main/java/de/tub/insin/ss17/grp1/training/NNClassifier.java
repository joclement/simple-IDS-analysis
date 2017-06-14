package de.tub.insin.ss17.grp1.training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.tub.insin.ss17.grp1.util.Param;
import weka.core.Instances;


// TODO add interface for ml algos to implement
public class NNClassifier {

    private double dist;

    private int k;

    private Map<String, Consumer<String>> paramDict;

    public NNClassifier(List<Param> params) {
        this.paramDict = new HashMap<>();
        this.setParams(params);
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

    public void train(Instances trainingData) {
        // TODO Auto-generated method stub
    }

}
