import java.util.Optional;

public class Barber implements Runnable {

    public static final int TIME_TO_CUT = 10;
    private volatile Optional<Client> currentClient = Optional.empty();
    private int order;

    private final BarberShop shop;
    private volatile boolean sleep;

    public Barber(BarberShop shop) {
        this.shop = shop;
        new Thread(this).start();
    }

    public boolean welcome(Client client) {
        synchronized (shop) {
            if (currentClient.isPresent()) {
                return false;
            }

            this.currentClient = Optional.of(client);
            this.wakeUp();
            System.out.println("Hey welcome new client wakingUp " + client);
            return true;
        }
    }

    private void wakeUp() {
        sleep = false;
    }

    @Override
    public void run() {
        while (true) {
            while(sleep) {
                waiting(10);
            }
            synchronized (shop) {
                checkNextClient();
                currentClient.ifPresent(client -> {
                                            cut(client);
                                            sayByeTo(client);
                                            checkTimeToSleep();
                                        }
                );
            }
        }
    }

    private void checkNextClient() {
        if (!currentClient.isPresent()) {
            currentClient = shop.nextClient();
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
    }

    private void checkTimeToSleep() {
        currentClient = shop.nextClient();
        if(!currentClient.isPresent())
            sleep = true;
    }

    private void waiting(int timeToWait) {
        try {
            Thread.sleep(timeToWait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
