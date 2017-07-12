package de.tub.insin.ss17.grp1.training;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import de.tub.insin.ss17.grp1.util.ClassIndexs;
import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.SelectedTag;


abstract public class NNClassifier implements MlAlgo {

    private Map<String, Consumer<String>> paramDict;

    CTUIBk nnClassifier;

    public NNClassifier(List<Param> params) {
        this.paramDict = new HashMap<>();
        this.nnClassifier = new CTUIBk();
        this.setParams(params);
    }

    private void setParams(List<Param> params) {
        this.paramDict.put("k", this::setK);
        this.paramDict.put("distweight", this::setDistanceWeighting);

        for (Param param : params) {
            Consumer<String> entry = this.paramDict.get(param.name);
            assert entry != null;
            entry.accept(param.value);
        }
    }

    private void setK(String kParam) {
        int k = Integer.valueOf(kParam);
        assert k > 0;
        this.nnClassifier.setKNN(k);
    }

    private void setDistanceWeighting(String distanceWeighting) {
        int tagID = -1;
        switch (distanceWeighting) {
            case "none":
                tagID = IBk.WEIGHT_NONE;
                break;
            case "inverse":
                tagID = IBk.WEIGHT_INVERSE;
                break;
            case "similarity":
                tagID = IBk.WEIGHT_SIMILARITY;
                break;
            default:
                System.err.println("Incorrect distance Weighting parameter specified.");
                System.exit(-1);
                break;
        }
        SelectedTag tag = new SelectedTag(tagID, IBk.TAGS_WEIGHTING);
        this.nnClassifier.setDistanceWeighting(tag);
    }

    @Override
    public void train(Instances trainingData) throws Exception {
        this.nnClassifier.setClassIndexs(new ClassIndexs(trainingData));
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
