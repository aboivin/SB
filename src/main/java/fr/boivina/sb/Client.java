package fr.boivina.sb;

import static com.github.ylegat.uncheck.Uncheck.uncheck;
import java.util.concurrent.CyclicBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);


    private final BarberShop barberShop;
    private CyclicBarrier barrier;
    private boolean haircut = false;
    public int order;

    public Client(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    public Client(BarberShop barberShop, CyclicBarrier barrier) {
        this.barberShop = barberShop;
        this.barrier = barrier;
    }

    public void wakeUpBarber(Barber barber) {
        barber.wakeUp();
        barber.accept(this);
    }

    public void run() {
        if(barrier != null)
            uncheck(() -> barrier.await());
        barberShop.acceptNewClient(this);
    }

    public boolean hasHairCut() {
        return haircut;
    }

    public void cutHairWithOrder(int order) {
        haircut = true;
        this.order = order;
    }
}
