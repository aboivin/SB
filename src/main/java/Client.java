import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    private volatile boolean hairCut = false;

    public int order;
    private String name;
    private BarberShop barberShop;
    private CyclicBarrier startBarrier;
    public CyclicBarrier midBarrier;

    public Client(String name, BarberShop barberShop) {
        this.name = name;
        this.barberShop = barberShop;
    }

    public Client(String name, BarberShop barberShop, CyclicBarrier latch) {
        this.name = name;
        this.barberShop = barberShop;
        this.startBarrier = latch;
    }

    public Client(String name, BarberShop barberShop, CyclicBarrier latch, CyclicBarrier midBarrier) {
        this.name = name;
        this.barberShop = barberShop;
        this.midBarrier = midBarrier;
        this.startBarrier = latch;
    }

    @Override
    public void run() {
        if (startBarrier != null) {
            await(startBarrier);
            logger.debug(this + "startBarrier is open");
        }

        this.barberShop.enterShop(this);
    }

    private void await(CyclicBarrier barrier) {
        try {
            barrier.await();
        } catch (BrokenBarrierException | InterruptedException e) {
            logger.error("Barrier broken", e);
        }
    }

    public void cut() {
        hairCut = true;
    }

    public boolean hasHairCut() {
        return hairCut;
    }

    @Override
    public String toString() {
        return name;
    }
}

