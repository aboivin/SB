import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class BarberShop {

    public static final int MAX_WAITING_ROOM_SIZE = 2;
    private ReentrantLock clientLock = new ReentrantLock();

    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    private Barber barber;

    public BarberShop() {
    }

    private Queue<Client> waitingRoom = new ArrayDeque<>();

    public void enterShop(Client client) {
        try {
            clientLock.lock();

            System.out.println(client + "enter the shop.");
            if (waitingRoom.isEmpty() && barber.acceptNewClient(Optional.of(client))) {
                System.out.println(client + "accepted by barber");
                return;
            }

            if (waitingRoomIsFull())
                return;

            waitingRoom.add(client);
            System.out.println("Adding " + client + " to the waiting room.");

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
