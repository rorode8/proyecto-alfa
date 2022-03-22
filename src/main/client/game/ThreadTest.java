package main.client.game;

public class ThreadTest extends Thread {
    private Game game;



    public ThreadTest(Game game) {
        super();
        this.game = game;
    }



    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        game.startGame();
    }

}
