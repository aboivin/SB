package fr.boivina.sb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingDeque;

public class BarberShop {

    private static final Logger logger = LoggerFactory.getLogger(BarberShop.class);

    private final LinkedBlockingDeque<Client> waitingRoom;
    private Client clientInCuttingChair;

    public BarberShop(int waitingRoomCapacity) {
        this.waitingRoom =  new LinkedBlockingDeque<>(waitingRoomCapacity);
    }

    public synchronized void newClientVisit(Client client) {
        logger.debug("[{}] entering the shop", client.id);

        if (clientInCuttingChair != null && waitingRoom.remainingCapacity() == 0) {
            logger.debug("[{}] Waiting room is full, leaving", client.id);
            return;
        }

        if(clientInCuttingChair == null) {
            logger.debug("[{}] entering the cutting chair", client.id);
            clientInCuttingChair = client;
        } else {
            waitingRoom.addFirst(client);
            logger.debug("[{}] entering the waiting room", client.id);
        }
        this.notifyAll();
    }

    public boolean anyClientWaiting() {
        return clientInCuttingChair != null || !waitingRoom.isEmpty();
    }

    public Client nextClient() {
        if(clientInCuttingChair != null) {
            Client nextClient = clientInCuttingChair;
            clientInCuttingChair = null;
            return nextClient;
        }
        return waitingRoom.pollLast();
    }
}
