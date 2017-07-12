package de.tub.insin.ss17.grp1.validation;

import de.tub.insin.ss17.grp1.util.ClassIndexs;

public class Metrics {

    private double[][] confMat;

    private ClassIndexs classIndexs;

    public Metrics(double[][] confusionMatrix, ClassIndexs classIndexs) {
        this.confMat = confusionMatrix;
        this.classIndexs = classIndexs;
    }

    public int tps() {
        double tps = this.confMat[classIndexs.BOTNET][classIndexs.BOTNET];
        assert(Math.round(tps) == tps);
        return Math.toIntExact(Math.round(tps));
    }

    public int fps() {
        double fps = this.confMat[classIndexs.NORMAL][classIndexs.BOTNET];
        assert(Math.round(fps) == fps);
        return Math.toIntExact(Math.round(fps));
    }

    public int tns() {
        double tns = this.confMat[classIndexs.NORMAL][classIndexs.NORMAL];
        assert(Math.round(tns) == tns);
        return Math.toIntExact(Math.round(tns));
    }

    public int fns() {
        double fns = this.confMat[classIndexs.BOTNET][classIndexs.NORMAL];
        assert(Math.round(fns) == fns);
        return Math.toIntExact(Math.round(fns));
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
}
