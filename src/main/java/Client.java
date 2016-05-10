import java.util.concurrent.CountDownLatch;

public class Client implements Runnable {

    private volatile boolean hairCut = false;

    public int order;
    private String name;
    private BarberShop barberShop;
    private CountDownLatch latch;

    public Client(String name, BarberShop barberShop) {
        this.name = name;
        this.barberShop = barberShop;
    }

    public Client(String name, BarberShop barberShop, CountDownLatch latch) {
        this.name = name;
        this.barberShop = barberShop;
        this.latch = latch;
    }

    @Override
    public void run() {
        if(latch != null) {
            latch.countDown();
            await(latch);
            System.out.println(this + "latch over");
        }

        this.barberShop.enterShop(this);
    }

    private void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
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

