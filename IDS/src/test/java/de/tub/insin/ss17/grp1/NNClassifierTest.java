package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.tub.insin.ss17.grp1.training.classifier.BallTreeNNClassifier;
import de.tub.insin.ss17.grp1.training.classifier.LinearNNClassifier;
import de.tub.insin.ss17.grp1.training.classifier.NNClassifier;
import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class NNClassifierTest {

    List<Param> params;
    NNClassifier classifier;
    Instances trainingData;
    Instances testData;

    @Before
    public void beforeEachTest() {
        this.trainingData = TestHelper.loadTraining();
        this.testData = TestHelper.loadTest();
        params = IDSCliManager.prepare(TestHelper.splitParams(AppTest.BASIC_NN_PARAMS));
    }

    @Test
    public void testLinearNNClassifier() throws Exception {
        classifier = new LinearNNClassifier(params);
        classifier.train(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier.getClassifier(), testData);
        assertTrue(eval.correct() >= 25);
    }

    @Test
    public void testBallTreeNNClassifier() throws Exception {
        classifier = new BallTreeNNClassifier(params);
        classifier.train(trainingData);

        Evaluation eval = new Evaluation(trainingData);
        eval.evaluateModel(classifier.getClassifier(), testData);
        assertTrue(eval.correct() >= 25);
    }
}
