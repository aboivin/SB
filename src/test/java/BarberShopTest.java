import static org.assertj.core.api.StrictAssertions.assertThat;
import java.util.Arrays;
import java.util.List;
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
        Client client = new Client("C");

        // When
        client.enter(barberShop);

        // Then
        Thread.sleep(100);
        System.out.println("Test client " + client + " has hair " + client.hasHairCut());
        assertThat(client.hasHairCut()).isTrue();
        assertThat(client.order).isEqualTo(0);
    }

    @Test
    public void should_send_second_client_in_wainting_room() throws Exception {
        // Given
        Client client1 = new Client("C1");
        Client client2 = new Client("C2");

        // When
        client1.enter(barberShop);
        client2.enter(barberShop);

        Thread.sleep(200);

        // Then
        System.out.println("Test client1 " + client1 + " has hair " + client1.hasHairCut());
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client1.order).isEqualTo(0);

        System.out.println("Test client2 " + client2 + " has hair " + client2.hasHairCut());
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client2.order).isEqualTo(1);
    }


    @Test
    public void should_keep_client_order() throws Exception {
        // Given
        Client client1 = new Client("C1");
        Client client2 = new Client("C2");
        Client client3 = new Client("C3");

        // When
        client1.enter(barberShop);
        client2.enter(barberShop);
        client3.enter(barberShop);

        Thread.sleep(300);

        // Then
        System.out.println("Test client1 " + client1 + " has hair " + client1.hasHairCut());
        assertThat(client1.hasHairCut()).isTrue();
        assertThat(client1.order).isEqualTo(0);

        System.out.println("Test client2 " + client2 + " has hair " + client2.hasHairCut());
        assertThat(client2.hasHairCut()).isTrue();
        assertThat(client2.order).isEqualTo(1);

        System.out.println("Test client3 " + client3 + " has hair " + client3.hasHairCut());
        assertThat(client3.hasHairCut()).isTrue();
        assertThat(client3.order).isEqualTo(2);
    }

//    @Test
//    public void should_reject_when_too_many_clients_are_waiting() throws Exception {
//        // Given
//        Client client1 = new Client();
//        Client client2 = new Client();
//        Client client3 = new Client();
//        Client client4 = new Client();
//
//        // When
//        client1.enter(barberShop);
//        client2.enter(barberShop);
//        client3.enter(barberShop);
//        client4.enter(barberShop);
//
//        Thread.sleep(400);
//
//        // Then
//        System.out.println("Test client1 " + client1 + " has hair " + client1.hasHairCut());
//        assertThat(client1.hasHairCut()).isTrue();
//        assertThat(client1.order).isEqualTo(0);
//
//        System.out.println("Test client2 " + client2 + " has hair " + client2.hasHairCut());
//        assertThat(client2.hasHairCut()).isTrue();
//        assertThat(client2.order).isEqualTo(1);
//
//        System.out.println("Test client3 " + client3 + " has hair " + client3.hasHairCut());
//        assertThat(client1.hasHairCut()).isTrue();
//        assertThat(client1.order).isEqualTo(2);
//
//        System.out.println("Test client4 " + client4 + " has hair " + client4.hasHairCut());
//        assertThat(client4.hasHairCut()).isFalse();
//    }
}
