package de.tub.insin.ss17.grp1.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.tub.insin.ss17.grp1.shared.SharedConstants;
import weka.core.Attribute;
import weka.core.Instances;

public class ClassIndexs implements Serializable {

    private static final long serialVersionUID = 1L;

    private final static Logger log = LoggerFactory.getLogger(ClassIndexs.class);

    public final int NORMAL;

    public final int BACKGROUND;

    public final int BOTNET;

    public ClassIndexs(Instances data) {
        Attribute classAttr = data.classAttribute();

        if (classAttr.numValues() != SharedConstants.CLASS_COUNT) {
            // TODO this can happen with arff files from other groups or our arff file without Background,
            // so a rework might be needed here.
            throw new IllegalArgumentException("Arff file format invalid.");
        };
        this.BACKGROUND = classAttr.indexOfValue(SharedConstants.BACKGROUND);
        this.BOTNET = classAttr.indexOfValue(SharedConstants.BOTNET);
        this.NORMAL = classAttr.indexOfValue(SharedConstants.NORMAL);

        log.debug("Background value, -1 means not used in set: {}", this.BACKGROUND);

        if (this.BOTNET < 0 || this.NORMAL < 0) {
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
