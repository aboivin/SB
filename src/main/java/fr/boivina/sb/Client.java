package fr.boivina.sb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CyclicBarrier;

import static com.github.ylegat.uncheck.Uncheck.uncheck;

public class Client implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public final int id;
    private final BarberShop barberShop;
    private CyclicBarrier barrier;
    private boolean haircut = false;
    public int order;

    public Client(int id, BarberShop barberShop) {
        this.id = id;
        this.barberShop = barberShop;
    }

    public Client(int id, BarberShop barberShop, CyclicBarrier barrier) {
        this.id = id;
        this.barberShop = barberShop;
        this.barrier = barrier;
    }

    public void run() {
        if(barrier != null)
            uncheck(() -> barrier.await());
        barberShop.newClientVisit(this);
    }

    public boolean hasHairCut() {
        return haircut;
    }

    public void cutHairWithOrder(int order) {
        haircut = true;
        this.order = order;
    }
}
