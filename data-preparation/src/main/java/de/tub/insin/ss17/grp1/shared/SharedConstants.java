package de.tub.insin.ss17.grp1.shared;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SharedConstants {

    /**
     * Constant for the BOTNET in arff
     */
    public final static String BOTNET = "A";

    /**
     * Constant for the NORMAL in arff
     */
    public final static String NORMAL = "N";

    /**
     * Constant for the BACKGROUND in arff
     */
    public final static String BACKGROUND = "B";

    public static final List<String> CLASS_LABELS_CORRECT_ORDER;

    public static final int CLASS_COUNT;

    static {
        CLASS_LABELS_CORRECT_ORDER =
            Collections.unmodifiableList(Arrays.asList(NORMAL, BOTNET, BACKGROUND));
        CLASS_COUNT = CLASS_LABELS_CORRECT_ORDER.size();
    }

    public static final String ENCODING = "UTF-8";

    public static final String HELP_TXT = "Use -h to get help.";
}
