package br.com.softctrl.contact.lib;

public class CMRuntimeException extends RuntimeException {

    /**
     *
     * @param tag
     * @param throwable
     */
    public CMRuntimeException(String tag, Throwable throwable) {
        super(tag, throwable);
    }

    public CMRuntimeException(String message) {
        super(message);
    }

    /**
     * @param tag
     * @param throwable
     */
    public static final void throwNew(String tag, Throwable throwable) {
        throw new CMRuntimeException(tag, throwable);
    }

    /**
     *
     * @param message
     */
    public static final void throwNew(String message) {
        throw new CMRuntimeException(message);
    }


}
