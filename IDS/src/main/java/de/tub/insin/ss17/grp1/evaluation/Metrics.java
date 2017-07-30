package de.tub.insin.ss17.grp1.evaluation;

import de.tub.insin.ss17.grp1.util.ClassIndexs;


public class Metrics {

    private final double[][] confMat;

    private final ClassIndexs classIndexs;

    protected Metrics(double[][] confusionMatrix, ClassIndexs classIndexs) {
        this.confMat = confusionMatrix;
        this.classIndexs = classIndexs;
    }

    public int tps() {
        double tps = this.confMat[classIndexs.BOTNET][classIndexs.BOTNET];
        return this.checkedToInt(tps);
    }

    public int fps() {
        double fps = this.confMat[classIndexs.NORMAL][classIndexs.BOTNET];
        return this.checkedToInt(fps);
    }

    public int tns() {
        double tns = this.confMat[classIndexs.NORMAL][classIndexs.NORMAL];
        return this.checkedToInt(tns);
    }

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
