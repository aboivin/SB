package fr.boivina.sb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarberShop {

    private static final Logger logger = LoggerFactory.getLogger(BarberShop.class);

    private final Barber barber;

    private final WaitingRoom waitingRoom;

    public BarberShop(Barber barber, WaitingRoom waitingRoom) {
        this.barber = barber;
        this.waitingRoom = waitingRoom;
    }

    public void acceptNewClient(Client client) {
        logger.debug("{} enter the shop.", client);

        if (!newClientIsAccepted(client)) {
            return;
        }
    }

    private synchronized boolean newClientIsAccepted(Client client) {
        if (waitingRoom.isFull()) {
            logger.debug("Waiting room is full for client %s", client);
            return false;
        }

        if (barber.isSleeping()) {
            client.wakeUpBarber(barber);
        } else {
            waitingRoom.accept(client);
        }

        return true;
    }

    public synchronized boolean anyClientWaiting() {
        if(waitingRoom.isEmpty()) {
            return false;
        }

        Client nextClient = waitingRoom.nextClient();
        barber.accept(nextClient);
        return true;
    }
}
