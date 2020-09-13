package fr.boivina.sb;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.StrictAssertions.assertThat;

@RunWith(Parameterized.class)
public class BarberShopTest {

    private BarberShop barberShop;
    private Barber barber;

    @Parameterized.Parameters
    public static List<Object[]> data() {
        return Arrays.asList(new Object[1][0]);
    }

    @Before
    public void init_barber() throws Exception {
        barberShop = new BarberShop(2);
        barber = new Barber(barberShop);
        Thread.sleep(30);
    }

    @Test
    public void should_accept_new_client() throws Exception {
        // Given
        Client client = new Client(1, barberShop);
        new Thread(barber).start();

        // When
        client.run();
        Thread.sleep(300);

        // Then
        assertThat(client.hasHairCut()).isTrue();
        assertThat(client.order).isEqualTo(0);
    }

    @Test
    public void should_send_second_client_in_wainting_room() throws Exception {
        // Given
        Client client1 = new Client(1, barberShop);
        Client client2 = new Client(2, barberShop);
        new Thread(barber).start();

        // When
        client1.run();
        client2.run();

        Thread.sleep(600);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client1.order).isEqualTo(0);
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client2.order).isEqualTo(1);
    }

    @Test
    public void should_keep_client_order() throws Exception {
        // Given
        Client client1 = new Client(1, barberShop);
        Client client2 = new Client(2, barberShop);
        Client client3 = new Client(3, barberShop);
        new Thread(barber).start();

        Thread.sleep(50);

        // When
        client1.run();
        client2.run();
        client3.run();
        Thread.sleep(600);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client1.order).isEqualTo(0);
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client2.order).isEqualTo(1);
        assertThat(client3.hasHairCut()).isTrue();
        assertThat(client3.order).isEqualTo(2);
    }

    @Test
    public void should_reject_when_too_many_clients() throws Exception {
        // Given
        Client client1 = new Client(1, barberShop);
        Client client3 = new Client(2, barberShop);
        Client client2 = new Client(3, barberShop);
        Client client4 = new Client(4, barberShop);
        new Thread(barber).start();

        Thread.sleep(50);

        // When
        client1.run();
        client2.run();
        client3.run();
        client4.run();
        Thread.sleep(600);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client3.hasHairCut()).isTrue();
        assertThat(client4.hasHairCut()).isFalse();
    }

    @Test
    public void should_accept_concurrent_client() throws Exception {
        // Given
        CyclicBarrier barrier = new CyclicBarrier(3);
        Client client1 = new Client(1, barberShop, barrier);
        Client client2 = new Client(2, barberShop, barrier);
        Client client3 = new Client(3, barberShop, barrier);
        Client client4 = new Client(4, barberShop, barrier);
        Client client5 = new Client(5, barberShop, barrier);
        Client client6 = new Client(6, barberShop, barrier);
        new Thread(barber).start();

        Thread.sleep(50);

        // When
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.submit(client1);
        service.submit(client2);
        service.submit(client3);
        Thread.sleep(200);
        service.submit(client4);
        service.submit(client5);
        service.submit(client6);
        Thread.sleep(1000);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client3.hasHairCut()).isTrue();
    }
}
