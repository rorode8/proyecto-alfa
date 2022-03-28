package main.client.game;

/**
 * this class only exists because changing the JFrame layout in a single thread
 * has unexpected behaviour
 */
public class SmoothTransitioner extends Thread {
    private Game game;
    private static final int DEFAULTTIME = 2000;
    private int time = DEFAULTTIME;


    /**
     * initializes game with default 2000 time
     * @param game
     */
    public SmoothTransitioner(Game game) {
        super();
        this.game = game;
    }

    /**
     * initializes game with custom time
     * @param game
     * @param time
     */
    public SmoothTransitioner(Game game, int time) {
        super();
        this.game = game;
        this.time = time;
    }



    @Override
    public void run() {
        try {

            Thread.sleep(time);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        game.startGame();

    }

}
