package de.tub.insin.ss17.grp1;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import de.tub.insin.ss17.grp1.training.LinearNNClassifier;
import de.tub.insin.ss17.grp1.util.ArffLoader;
import de.tub.insin.ss17.grp1.util.Param;
import weka.classifiers.Evaluation;
import weka.core.Instances;

public class NNClassifierTest {

    @Test
    public void testBasic() throws Exception {

        List<Param> params = CliManager.prepare(TestHelper.splitParams(AppTest.BASIC_NN_PARAMS));
        LinearNNClassifier classifier = new LinearNNClassifier(params);

        Instances traininData = TestHelper.loadTraining();
        Instances testData = TestHelper.loadTest();

        classifier.train(traininData);

        Evaluation eval = new Evaluation(traininData);
        eval.evaluateModel(classifier.getClassifier(), testData);
        assertTrue(eval.correct() >= 25);
    }
}
