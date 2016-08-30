package fr.boivina.sb;

public class Client implements Runnable {

    private final BarberShop barberShop;
    private boolean haircut = false;
    public int order;

    public Client(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    public void wakeUpBarber(Barber barber) {
        barber.wakeUp();
        barber.accept(this);
    }

    public void run() {
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
