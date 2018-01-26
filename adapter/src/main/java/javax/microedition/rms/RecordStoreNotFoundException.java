package javax.microedition.rms;

public class RecordStoreNotFoundException extends RecordStoreException {
    private static final long serialVersionUID = -2170818986632514948L;

    public RecordStoreNotFoundException(String message, Exception e) {
        super(message, e);
    }

    public RecordStoreNotFoundException(String message) {
        super(message);
    }
}
