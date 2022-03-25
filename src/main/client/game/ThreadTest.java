package main.client.game;

public class ThreadTest extends Thread {
    private Game game;
    private static final int DEFAULTTIME = 2000;
    private int time = DEFAULTTIME;



    public ThreadTest(Game game) {
        super();
        this.game = game;
    }

    public ThreadTest(Game game, int time) {
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
        if(time==DEFAULTTIME){
            game.startGame();
        }else{
            game.restartGame();
        }

    }

}
