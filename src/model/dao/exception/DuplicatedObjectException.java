package model.dao.exception;

public class DuplicatedObjectException extends Throwable {

    /**
    * Creates a new instance of
   * <code>DuplicatedObjectException</code> without detail message.
   */
    public DuplicatedObjectException() {
    }

    /**
     * Constructs an instance of
     * <code>DuplicatedObjectException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public DuplicatedObjectException(String msg) {
        super(msg);
    }
    /* The super keyword refers to superclass (parent) objects.
    It is used to call superclass methods, and to access the superclass constructor.
    The most common use of the super keyword is to eliminate the confusion between superclasses and subclasses that have methods with the same name. */

}
