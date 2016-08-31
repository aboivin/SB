package fr.boivina.sb;

import static com.github.ylegat.uncheck.Uncheck.uncheck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Barber implements Runnable {

    private static final long SLEEP_TIME = 50L;
    private static final long CUT_TIME = 50L;
    private final static Logger logger = LoggerFactory.getLogger(Barber.class);

    private boolean sleeping = true;
    private Client client;
    private ClientCounter clientCounter = new ClientCounter();

    private BarberShop barberShop;

    public boolean isSleeping() {
        return sleeping;
    }

    public void wakeUp() {
        logger.info("Barber woken up !");
        sleeping = false;
    }

    public void accept(Client client) {
        this.client = client;
    }

    @Override
    public void run() {
        while (true) {
            if (sleeping) {
                logger.info("Barber is still sleeping.");
                uncheck(() -> Thread.sleep(SLEEP_TIME));
            } else {
                work();
            }
        }
    }

    private void work() {
        cutHair(client);
        if(!barberShop.anyClientWaiting()) {
            sleeping = true;
        }
    }

    private void cutHair(Client client) {
        logger.debug("Cutting hair of {}", client);
        uncheck(() -> Thread.sleep(CUT_TIME));
        client.cutHairWithOrder(clientCounter.getAndIncrement());
        logger.info("Hair are cut for {}", client);
    }

    public void setBarberShop(BarberShop barberShop) {
        this.barberShop = barberShop;
    }
}
