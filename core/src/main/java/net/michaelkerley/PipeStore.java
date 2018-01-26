package net.michaelkerley;

import org.jetbrains.annotations.NotNull;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import java.util.logging.Logger;

public class PipeStore {
    private static final String STORE_NAME = "PipesStore";
    private static final int STORE_PIPES_RECORD = 2;
    private static final int STORE_SIZE_RECORD = 1;
    private final Logger logger = Logger.getLogger(PipeStore.class.getName());
    private final PipesGame canvas;

    public PipeStore(PipesGame game) {
        this.canvas = game;
    }

    public void save(@NotNull PipesInfo info) {
        int cols = info.getCols();
        int rows = info.getRows();
        if (info.noPipes()) {
            return;
        }
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(STORE_NAME, true);
            byte[] size = new byte[2];
            size[0] = (byte) cols;
            size[1] = (byte) rows;
            try {
                rs.setRecord(STORE_SIZE_RECORD, size, 0, size.length);
            } catch (InvalidRecordIDException e) {
                rs.addRecord(size, 0, size.length);
            }
            byte[] pipesBytes = new byte[rows * cols];
            int i = 0;
            for (int y = 0; y < rows; ++y) {
                for (int x = 0; x < cols; ++x) {
                    pipesBytes[i] = info.getPipe(x, y).getConnections();
                    ++i;
                }
            }
            try {
                rs.setRecord(STORE_PIPES_RECORD, pipesBytes, 0, pipesBytes.length);
            } catch (InvalidRecordIDException e) {
                rs.addRecord(pipesBytes, 0, pipesBytes.length);
            }
            rs.closeRecordStore();
        } catch (RecordStoreException t) {
            try {
                if (rs != null) {
                    rs.closeRecordStore();
                }
            } catch (RecordStoreException t2) {
                logger.severe(t2.getLocalizedMessage());
            }
            try {
                RecordStore.deleteRecordStore(STORE_NAME);
            } catch (RecordStoreException t2) {
                logger.severe(t2.getLocalizedMessage());
            }
        }
    }

    public boolean load(@NotNull PipesInfo info, int defCols, int defRows) {
        boolean success = true;
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(STORE_NAME, false);
        } catch (RecordStoreException e) {
            logger.severe(e.getLocalizedMessage());
            success = false;
        }
        if (success) {
            try {
                byte[] size = rs.getRecord(STORE_SIZE_RECORD);
                int cols = info.getCols();
                int rows = info.getRows();
                info.init(cols, rows);
                success = cols > 0 && rows > 0;
                if (success) {
                    canvas.initPipeSize();
                    try {
                        byte[] pipesBytes = rs.getRecord(STORE_PIPES_RECORD);
                        canvas.initPipes();
                        success = canvas.setConnections(pipesBytes, cols, rows);
                        if (success) {
                            canvas.checkConnections();
                        }
                    } catch (InvalidRecordIDException e) {
                        success = false;
                    }
                }
            } catch (RecordStoreException e) {
                try {
                    rs.closeRecordStore();
                } catch (RecordStoreException t) {
                    logger.severe(t.getLocalizedMessage());
                }
                try {
                    RecordStore.deleteRecordStore(STORE_NAME);
                } catch (RecordStoreException t) {
                    logger.severe(t.getLocalizedMessage());
                }
            } finally {
                try {
                    rs.closeRecordStore();
                } catch (RecordStoreException t) {
                    logger.severe(t.getLocalizedMessage());
                }
            }
        }
        if (!success) {
            info.init(defCols, defRows);
            canvas.init();
        }
        return success;
    }
}
