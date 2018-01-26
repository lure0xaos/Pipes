package net.michaelkerley;

@SuppressWarnings("LawOfDemeter")
public class Pipe {
    private final int x;
    private final int y;
    private byte connections;
    private boolean inConnectedSet;

    public Pipe(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public byte getConnections() {
        return connections;
    }

    public void setConnections(byte connections) {
        this.connections = connections;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isInUnconnectedSet() {
        return !inConnectedSet;
    }

    public boolean isInConnectedSet() {
        return inConnectedSet;
    }

    public void setInConnectedSet(boolean inConnectedSet) {
        this.inConnectedSet = inConnectedSet;
    }

    public boolean isConnected(int dir) {
        return (connections & dir) == dir;
    }

    public void rotate(boolean clockwise) {
        if (clockwise) {
            connections *= 2;
            if (isConnected(PipeConn.CONN_OVERFLOW)) {
                setConnected(PipeConn.CONN_OVERFLOW, false);
                setConnected(PipeConn.CONN_UP, true);
            }
        } else {
            if (isConnected(PipeConn.CONN_UP)) {
                setConnected(PipeConn.CONN_OVERFLOW, true);
            }
            connections /= 2;
        }
    }

    public void setConnected(int dir, boolean connected) {
        if (connected) {
            connections |= dir;
        } else {
            connections &= (~dir);
        }
    }

}
