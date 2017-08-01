package de.tub.insin.ss17.grp1.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.shared.SharedConstants;
import weka.core.Attribute;
import weka.core.Instances;


/**
 * Class to validate, identify and correctly access information based on the class labels
 * and their order(indexing).
 *
 * @author Joris Clement
 *
 */
public class ClassIndexs implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger log = LoggerFactory.getLogger(ClassIndexs.class);

    /**
     * the index of class label NORMAL.
     */
    public final int NORMAL;

    /**
     * the index of the class label BACKGROUND.
     */
    public final int BACKGROUND;

    /**
     * the index of the class label BOTNET.
     */
    public final int BOTNET;

    /**
     * Set up the class indexes from the given arff data.
     *
     * @param data
     */
    public ClassIndexs(Instances data) {
        Attribute classAttr = data.classAttribute();

        if (classAttr.numValues() != SharedConstants.CLASS_COUNT) {
            /*
             * TODO this can happen with arff files from other groups
             * or our arff file without Background,
             * so a rework might be needed here.
             */
            throw new IllegalArgumentException("Arff file format invalid.");
        };
        this.BACKGROUND = classAttr.indexOfValue(SharedConstants.BACKGROUND);
        this.BOTNET = classAttr.indexOfValue(SharedConstants.BOTNET);
        this.NORMAL = classAttr.indexOfValue(SharedConstants.NORMAL);

        log.debug("Background value, -1 means not used in set: {}", this.BACKGROUND);

        if (this.BOTNET < 0 || this.NORMAL < 0) {
            log.error("BOTNET label is: {}", this.BOTNET);
            log.error("NORMAL label is: {}", this.NORMAL);
            throw new IllegalArgumentException("Arff file: class label specification invalid.");
        }
    }

    public boolean hasBackground() {
        return this.BACKGROUND >= 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj.getClass().equals(this.getClass())) {
            ClassIndexs other = (ClassIndexs) obj;

            if (this.NORMAL     == other.NORMAL     &&
                this.BACKGROUND == other.BACKGROUND &&
                this.BOTNET     == other.BOTNET       ) {

                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("Hashcode for this class not implemented");
    }
}
