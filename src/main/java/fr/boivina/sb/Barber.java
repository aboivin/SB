package fr.boivina.sb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.ylegat.uncheck.Uncheck.uncheck;
import static java.lang.Thread.sleep;

public class Barber implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(Barber.class);
    private static final long CUT_TIME = 50L;

    private Integer clientCounter = 0;

    private final BarberShop barberShop;

    public Barber(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        while (true) {
            Client client;
            synchronized(barberShop) {
                if (!barberShop.anyClientWaiting()) {
                    logger.info("[Barber] start sleeping...");
                    uncheck(() -> barberShop.wait());
                    logger.info("[Barber] woken up !");
                }
                client = barberShop.nextClient();
            }
            cutHair(client);
        }
    }

    private void cutHair(Client client) {
        logger.debug("[{}] Hair being cut", client.id);
        uncheck(() -> sleep(CUT_TIME));
        client.cutHairWithOrder(clientCounter++);
        logger.debug("[{}] Hair finished being cut", client.id);
    }
}
