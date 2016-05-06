import java.util.ArrayDeque;
import java.util.Optional;
import java.util.Queue;

public class BarberShop {
    public void setBarber(Barber barber) {
        this.barber = barber;
    }

    private Barber barber;

    public BarberShop() {
    }

    private Queue<Client> waitingRoom = new ArrayDeque<>();

    public void enterShop(Client client) {
        if (!barber.welcome(client)) {
            waitingRoom.add(client);
            System.out.println("Adding "+ client +" to the waiting room.");
            return;
        }
    }

    private void goForHaircut(Client client) {
        barber.welcome(client);
    }

    public boolean isClientWaiting(Client client) {
        return waitingRoom.contains(client);
    }

    public boolean waitingRoomIsEmpty() {
        return waitingRoom.isEmpty();
    }

    public Optional<Client> nextClient() {
        return Optional.ofNullable(waitingRoom.poll());
    }
}
