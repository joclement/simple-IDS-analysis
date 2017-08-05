package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.training.classifier.BallTreeNNClassifier;
import de.tub.insin.ss17.grp1.training.classifier.LinearNNClassifier;
import de.tub.insin.ss17.grp1.training.classifier.NNClassifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class NNClassifierTest {

    NNClassifier classifier;
    Instances trainingData;
    Instances testData;

    public static List<String> emptyParamsList() {
        List<String> params = new LinkedList<String>();
        params.add("");
        return params;
    }

    @Before
    public void beforeEachTest() {
        this.trainingData = TestHelper.loadTraining();
        this.testData = TestHelper.loadTest();
        this.classifier = null;
    }

    @Test
    public void testLinearNNClassifier() throws Exception {
        classifier = new LinearNNClassifier(emptyParamsList());
        classifier.train(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier.getClassifier(), testData);
        assertTrue(eval.correct() >= 25);
    }

    @Test
    public void testLinearNNClassifierOptions() throws Exception {
        List<String> params = TestHelper.prepareParams("-K=5,-I");
        classifier = new LinearNNClassifier(params);
        classifier.train(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier.getClassifier(), testData);
        assertTrue(eval.correct() >= 25);
    }

    @Test
    public void testBallTreeNNClassifier() throws Exception {
        classifier = new BallTreeNNClassifier(emptyParamsList());
        classifier.train(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier.getClassifier(), testData);
        assertTrue(eval.correct() >= 25);
    }
}
