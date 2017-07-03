package de.tub.insin.ss17.grp1.util;

public class IDSSharedConstants {

    public static final String BACKGROUND = "Background";

    public static final String NORMAL = "Normal";

    public static final String BOTNET = "Botnet";

    public static final String[] CLASS_LABELS_CORRECT_ORDER = {NORMAL, BOTNET, BACKGROUND};

    public static final int CLASS_COUNT;

    static {
        CLASS_COUNT = CLASS_LABELS_CORRECT_ORDER.length;
    }
}
