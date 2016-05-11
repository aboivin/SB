import static org.assertj.core.api.StrictAssertions.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
        barberShop = new BarberShop();
        barber = new Barber(barberShop);
        barberShop.setBarber(barber);
        Thread.sleep(30);
    }

    @Test
    public void should_accept_new_client() throws Exception {
        // Given
        Client client = new Client("C", barberShop);

        // When
        client.run();
        Thread.sleep(100);

        // Then
        assertThat(client.hasHairCut()).isTrue();
        assertThat(client.order).isEqualTo(0);
    }

    @Test
    public void should_send_second_client_in_wainting_room() throws Exception {
        // Given
        Client client1 = new Client("C1", barberShop);
        Client client2 = new Client("C2", barberShop);

        // When
        client1.run();
        client2.run();

        Thread.sleep(200);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client1.order).isEqualTo(0);
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client2.order).isEqualTo(1);
    }

    @Test
    public void should_keep_client_order() throws Exception {
        // Given
        Client client1 = new Client("C1", barberShop);
        Client client2 = new Client("C2", barberShop);
        Client client3 = new Client("C3", barberShop);

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
        Client client1 = new Client("C1", barberShop);
        Client client2 = new Client("C2", barberShop);
        Client client3 = new Client("C3", barberShop);
        Client client4 = new Client("C4", barberShop);

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
    public void should_accept_concurent_client() throws Exception {
        // Given
        CyclicBarrier barrier = new CyclicBarrier(3);
        Client client1 = new Client("C1", barberShop, barrier);
        Client client2 = new Client("C2", barberShop, barrier);
        Client client3 = new Client("C3", barberShop, barrier);

        Thread.sleep(50);

        // When
        ExecutorService service = Executors.newFixedThreadPool(3);
        service.submit(client1);
        service.submit(client2);
        service.submit(client3);
        Thread.sleep(1000);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client3.hasHairCut()).isTrue();
    }

    @Test
    public void should_not_have_sleeping_barber_and_waiting_client() throws Exception {
        // Given
        CyclicBarrier barrier = new CyclicBarrier(2);
        CyclicBarrier clientBarrier = new CyclicBarrier(2);
        BarberShop barberShop = new BarberShop();
        Barber barber = new Barber(barberShop, barrier);
        barberShop.setBarber(barber);

        Client client1 = new Client("C1", barberShop);
        Client client2 = new Client("C2", barberShop, null, barrier);

        Thread.sleep(50);

        // When
        ExecutorService service = Executors.newFixedThreadPool(2);
        service.submit(client1,clientBarrier);
        Thread.sleep(50);
        barrier.await();
        service.submit(client2,clientBarrier);
        Thread.sleep(2000);

        // Then
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client2.hasHairCut()).isTrue();
    }
}
