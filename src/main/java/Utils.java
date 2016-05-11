import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void await(CyclicBarrier barrier) {
        try {
            if (barrier != null) {
                System.out.println("Barrier block " + Thread.currentThread().getName());
                barrier.await();
                System.out.println("Barrier open for " + Thread.currentThread().getName());
            }

        } catch (BrokenBarrierException | InterruptedException e) {
            logger.error("Barrier broken", e);
        }
    }
}
