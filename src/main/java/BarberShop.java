import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BarberShop {

    private static final Logger logger = LoggerFactory.getLogger(BarberShop.class);

    public static final int MAX_WAITING_ROOM_SIZE = 2;
    private ReentrantLock clientLock = new ReentrantLock();

    private Barber barber;

    public BarberShop() {
    }

    private Queue<Client> waitingRoom = new ArrayDeque<>();

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    public void enterShop(Client client) {
        try {
            clientLock.lock();

            logger.debug(client + "enter the shop.");
            if (waitingRoom.isEmpty() && barber.acceptNewClient(Optional.of(client))) {
                logger.debug(client + "accepted by barber");
                return;
            }

            if (waitingRoomIsFull())
                return;

            Utils.await(client.midBarrier);

            logger.debug("Adding " + client + " to the waiting room.");
            waitingRoom.add(client);

        } finally {
            clientLock.unlock();
        }
    }

    private boolean waitingRoomIsFull() {
        return waitingRoom.size() == MAX_WAITING_ROOM_SIZE;
    }

    public Optional<Client> nextClient() {
        return Optional.ofNullable(waitingRoom.poll());
    }
}
