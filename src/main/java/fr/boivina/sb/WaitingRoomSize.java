package fr.boivina.sb;

public class WaitingRoomSize {

    private final int waitingRoomSize;

    public WaitingRoomSize(int waitingRoomSize) {
        this.waitingRoomSize = waitingRoomSize;
    }

    protected boolean isFull(int clientCount) {
        return clientCount == waitingRoomSize;
    }
}
