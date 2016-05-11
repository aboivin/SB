import java.util.Optional;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Barber implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Barber.class);

    public static final int TIME_TO_CUT = 20;
    private volatile Optional<Client> currentClient = Optional.empty();
    private int order;

    private final BarberShop shop;
    private ReentrantLock barberLock = new ReentrantLock();
    private CyclicBarrier barrier;

    public Barber(BarberShop shop) {
        this.shop = shop;
        new Thread(this, "BarberThread").start();
    }

    public Barber(BarberShop shop, CyclicBarrier barrier) {
        this.shop = shop;
        this.barrier = barrier;
        new Thread(this, "BarberThread").start();
    }

    @Override
    public void run() {
        while (true) {
            checkWaitingRoom();

            Utils.await(barrier);

            while (!currentClient.isPresent()) {
                waiting(10);
                logger.debug("sleeping");
            }
            currentClient.ifPresent(client -> {
                                        cut(client);
                                        sayByeToClient();
                                    }
            );
        }
    }

    private void checkWaitingRoom() {
        if (acceptNewClient(shop.nextClient())) {
            currentClient.ifPresent(c -> logger.debug("Welcome waiting client " + c));
        }
    }

    private void cut(Client client) {
        logger.debug("Cutting hair of " + client + " on " + Thread.currentThread().getName());
        waiting(TIME_TO_CUT);
        client.cut();
        logger.debug("client " + client + "cut " + client.hasHairCut());
        client.order = order++;
    }

    private void sayByeToClient() {
        currentClient = Optional.empty();
    }

    public boolean acceptNewClient(Optional<Client> client) {
        try {
            barberLock.lock();
            if (!currentClient.isPresent()) {
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
