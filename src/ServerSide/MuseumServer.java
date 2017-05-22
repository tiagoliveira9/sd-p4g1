package ServerSide;


import java.util.concurrent.ThreadLocalRandom;

/**
 * Museum interface.
 *
 * @author João Cravo joao.cravo@ua.pt n.:63784
 * @author Tiago Oliveira tiago9@ua.pt n.:51687
 */
public class MuseumServer {

    private final Museum mus;

    /**
     * Museum interface.
     */
    public MuseumServer()
    {
        mus = Museum.getInstance();

        int distance, canvas;
        for (int i = 0; i < 5; i++)
        {
            // distance between 15 and 30
            distance = ThreadLocalRandom.current().nextInt(15, 30 + 1);
            // canvas between 8 and 16
            canvas = ThreadLocalRandom.current().nextInt(8, 16 + 1);
            mus.setUpRoom(i, distance, canvas);
        }
    }

}
