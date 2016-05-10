public class Client implements Runnable {

    private volatile boolean hairCut = false;

    public int order;
    private String name;
    private BarberShop barberShop;

    public Client(String name, BarberShop barberShop) {
        this.name = name;
        this.barberShop = barberShop;
    }

    @Override
    public void run() {
        this.barberShop.enterShop(this);
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

