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

}