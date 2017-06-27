package de.tub.insin.ss17.grp1.util;

import java.io.Serializable;

import weka.core.Attribute;
import weka.core.Instances;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassIndexs implements Serializable {

    private static final long serialVersionUID = 1L;

    public final int NORMAL;

    public final int BACKGROUND;

    public final int BOTNET;
    
    private final static Logger log = LoggerFactory.getLogger(ClassIndexs.class);

    public ClassIndexs(Instances data) {
        log.debug("start: ClassIndexs");
        Attribute classAttr = data.classAttribute();
        assert classAttr.isNominal();
        assert classAttr.numValues() == IDSSharedConstants.CLASS_COUNT;
        this.BACKGROUND = classAttr.indexOfValue(IDSSharedConstants.BACKGROUND);
        this.BOTNET = classAttr.indexOfValue(IDSSharedConstants.BOTNET);
        this.NORMAL = classAttr.indexOfValue(IDSSharedConstants.NORMAL);

        // TODO move or copy to unit test
        assert this.BACKGROUND >= 0;
        assert this.NORMAL >= 0;
        assert this.BOTNET >= 0;
        log.debug("finished: ClassIndexs");
    }

    @Override
    public boolean equals(Object obj) {
        log.debug("start: equals");
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
        log.debug("finished: equals");
        return false;
    }
}
