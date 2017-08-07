package de.tub.insin.ss17.grp1.shared;

/**
 * Simple own weka runtime exception class to prevent
 * explicit exception specification in functions,
 * because Weka just throws exceptions of the type `Exception`.
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
