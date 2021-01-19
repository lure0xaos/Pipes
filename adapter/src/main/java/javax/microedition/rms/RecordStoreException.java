package javax.microedition.rms;

public class RecordStoreException extends Exception {
    private static final long serialVersionUID = -6654380412531765098L;

    RecordStoreException(String message, Exception e) {
        super(message, e);
    }

    RecordStoreException(String message) {
        super(message);
    }
}
