package de.tub.insin.ss17.grp1.shared;

/**
 * Simple own weka runtime exception class to prevent
 * explicit exception specification in functions.
 *
 * @author Joris Clement
 *
 */
public class RuntimeWekaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RuntimeWekaException(String string) {
        super(string);
    }
}
