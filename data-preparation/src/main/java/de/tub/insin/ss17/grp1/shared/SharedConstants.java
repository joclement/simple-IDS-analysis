package de.tub.insin.ss17.grp1.shared;

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

    public static final String[] CLASS_LABELS_CORRECT_ORDER = {NORMAL, BOTNET, BACKGROUND};

    public static final int CLASS_COUNT;

    static {
        CLASS_COUNT = CLASS_LABELS_CORRECT_ORDER.length;
    }
}
