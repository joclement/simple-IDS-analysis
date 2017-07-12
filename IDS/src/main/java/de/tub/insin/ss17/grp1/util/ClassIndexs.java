package de.tub.insin.ss17.grp1.util;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        assert classAttr.isNominal();
        assert classAttr.numValues() == IDSSharedConstants.CLASS_COUNT;
        this.BACKGROUND = classAttr.indexOfValue(IDSSharedConstants.BACKGROUND);
        this.BOTNET = classAttr.indexOfValue(IDSSharedConstants.BOTNET);
        this.NORMAL = classAttr.indexOfValue(IDSSharedConstants.NORMAL);

        log.debug("Background value, -1 means not used in set: {}", this.BACKGROUND);
        // TODO move or copy to unit test
        assert this.BACKGROUND >= 0;
        assert this.NORMAL >= 0;
        assert this.BOTNET >= 0;
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
