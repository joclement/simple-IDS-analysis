package de.tub.insin.ss17.grp1.evaluation;

import de.tub.insin.ss17.grp1.util.ClassIndexs;


/**
 * Class to compute metrics especially for the CTU data-set. That data-set has 3 class
 * labels(NORMAL, BOTNET, BACKGROUND). But the metrics can just be computed for the labels
 * NORMAL and BOTNET. So the calculations of the True Positives, True Negatives, False Positives
 * and False Negatives need to be adapted.
 *
 * @author Joris Clement
 *
 */
public class Metrics {

    private final double[][] confMat;

    private final ClassIndexs classIndexs;

    /**
     * Constructs an object to get the metrics based on a given confusion matrix
     * and information about the class labels on that confusion matrix.
     *
     * @param confusionMatrix the confusion matrix
     * @param classIndexs information about class labels in confusion matrix
     */
    protected Metrics(double[][] confusionMatrix, ClassIndexs classIndexs) {
        this.confMat = confusionMatrix;
        this.classIndexs = classIndexs;
    }

    /**
     *
     * @return number of true positives
     */
    public int tps() {
        double tps = this.confMat[classIndexs.BOTNET][classIndexs.BOTNET];
        return this.checkedToInt(tps);
    }

    /**
     *
     * @return number of false positives
     */
    public int fps() {
        double fps = this.confMat[classIndexs.NORMAL][classIndexs.BOTNET];
        return this.checkedToInt(fps);
    }

    /**
     *
     * @return number of true negatives
     */
    public int tns() {
        double tns = this.confMat[classIndexs.NORMAL][classIndexs.NORMAL];
        return this.checkedToInt(tns);
    }

    /**
     *
     * @return number of false negatives
     */
    public int fns() {
        double fns = this.confMat[classIndexs.BOTNET][classIndexs.NORMAL];
        return this.checkedToInt(fns);
    }

    public double falsePositiveRate() {
        double fpRate = this.fps() / (double) (this.fps() + this.tns());
        return fpRate;
    }

    public double falseNegativeRate() {
        double fnRate = this.fns() / (double) (this.fns() + this.tps());
        return fnRate;
    }

    public double recall() {
        double recall = this.tps() / (double) (this.fns() + this.tps());
        return recall;
    }

    public double specificity() {
        double specificity = this.tns() / (double) (this.fps() + this.tns());
        return specificity;
    }

    public double truePositiveRate() {
        return recall();
    }

    public double trueNegativeRate() {
        return specificity();
    }

    public double accuracy() {
        double accuracy = (this.tps() + this.tns()) /
                (double) (this.tps() + this.tns() + this.fps() + this.fns());
        return accuracy;
    }

    public double precision() {
        double precision = this.tps() / (double) (this.fps() + this.tps());
        return precision;
    }

    private int checkedToInt(double num) {
        long roundedNum = Math.round(num);
        if (roundedNum != num) {
            throw new RuntimeException("Confusion Matrix entry is not an int");
        }
        return Math.toIntExact(roundedNum);
    }
}
