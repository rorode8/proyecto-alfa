package main.server;

public class ServerClock extends Thread{
    private ServerServer game;
    public long t;
    public boolean finish;


    public ServerClock(ServerServer game) {
        this.game = game;
        t = System.currentTimeMillis();
        finish=false;
    }

    public void run(){
        while(!finish){
            if(System.currentTimeMillis()-t>=1500){
                game.sendMonster();
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
