public class Client {

    private volatile boolean hairCut = false;

    public int order;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    public Client(String name) {
        this.name = name;
    }

    public void enter(BarberShop barberShop) {
        barberShop.enterShop(this);
    }

    public void cut() {
        hairCut = true;
    }

    public boolean hasHairCut() {
        return hairCut;
    }
}

