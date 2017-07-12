package de.tub.insin.ss17.grp1.util;

import de.tub.insin.ss17.grp1.shared.DataSharedConstants;

public class IDSSharedConstants {

    public static final String BACKGROUND = DataSharedConstants.BACKGROUND;

    public static final String NORMAL = DataSharedConstants.NORMAL;

    public static final String BOTNET = DataSharedConstants.BOTNET;

    public static final String[] CLASS_LABELS_CORRECT_ORDER = {NORMAL, BOTNET, BACKGROUND};

    public static final int CLASS_COUNT;

    static {
        CLASS_COUNT = CLASS_LABELS_CORRECT_ORDER.length;
    }
}
