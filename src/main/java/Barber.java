import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class Barber implements Runnable {

    public static final int TIME_TO_CUT = 20;
    private volatile Optional<Client> currentClient = Optional.empty();
    private int order;

    private final BarberShop shop;
    private ReentrantLock barberLock = new ReentrantLock();

    public Barber(BarberShop shop) {
        this.shop = shop;
        new Thread(this, "BarberThread").start();
    }

    @Override
    public synchronized void run() {
        while (true) {
            checkWaitingRoom();
            while (!currentClient.isPresent()) {
                waiting(10);
            }
            currentClient.ifPresent(client -> {
                                        cut(client);
                                        sayByeTo(client);
                                    }
            );
        }
    }

    private void checkWaitingRoom() {
        if (acceptNewClient(shop.nextClient())) {
            currentClient.ifPresent(c -> System.out.println("Welcome waiting client " + c));
        }
    }

    private void cut(Client client) {
        System.out.println("Cutting hair of " + client + " on " + Thread.currentThread().getName());
        waiting(TIME_TO_CUT);
        client.cut();
        System.out.println("client " + client + "cut " + client.hasHairCut());
        client.order = order++;
    }

    private void sayByeTo(Client client) {
        System.out.println("Bye " + client);
        currentClient = Optional.empty();
    }

    public boolean acceptNewClient(Optional<Client> client) {
        try {
            barberLock.lock();
            if (!currentClient.isPresent()) {
                System.out.println("Hey welcome new client " + client);
                currentClient = client;
                return true;
            }

            return false;
        } finally {
            barberLock.unlock();
        }
    }

    private void waiting(int timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
