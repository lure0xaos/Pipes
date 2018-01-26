package javax.microedition.rms;

public class InvalidRecordIDException extends RecordStoreException {
    private static final long serialVersionUID = -7286953022487766095L;

    public InvalidRecordIDException(String message, Exception e) {
        super(message, e);
    }
}
