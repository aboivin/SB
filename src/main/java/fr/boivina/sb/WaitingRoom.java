package fr.boivina.sb;

import java.util.ArrayDeque;
import java.util.Queue;

public class WaitingRoom {

    private final int waitingRoomSize;

    private final Queue<Client> waitingQueue = new ArrayDeque<>();

    public WaitingRoom(int waitingRoomSize) {
        this.waitingRoomSize = waitingRoomSize;
    }

    public boolean isFull() {
        return waitingRoomSize == waitingQueue.size();
    }

    public boolean isEmpty() {
        return waitingQueue.isEmpty();
    }

    public void accept(Client client) {
        waitingQueue.add(client);
    }

    public Client nextClient() {
        return waitingQueue.poll();
    }
}
