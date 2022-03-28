package main.client.game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class ClickListener implements ActionListener{

    private int coordinate;
    private JButton btn;
    private Game game;

    public ClickListener(int coordinate, JButton btn, Game game) {
        super();
        this.btn = btn;
        this.coordinate = coordinate;
        this.game = game;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        btn.setIcon(null);
        game.sendtool.sendNum(this.coordinate);
    }

}
