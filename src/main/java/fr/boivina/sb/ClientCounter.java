package fr.boivina.sb;

import java.util.concurrent.atomic.AtomicInteger;

public class ClientCounter {

    private AtomicInteger counter = new AtomicInteger(0);

    public Integer getAndIncrement() {
        return counter.getAndIncrement();
    }
}
