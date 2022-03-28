package main.client.game;

import main.interfaces.Server;
import main.interfaces.ServerInfo;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Game extends JFrame {

    private JPanel start;
    private JPanel mainpanel;
    private Font font;
    public JTextField textField = null;
    public Server gameServer;
    public ServerInfo info;
    public TCPClient sendtool;
    public ListenUDPThread listentool;
    public JButton[] btns = new JButton[9];
    private Icon goomba = null;

    /**
     * initializes game and waits for user to input
     * the host ip address from which it will acquire
     * all other information like multicast address and TCP and UDP ports
     *  the game tries to launch when the submit button is clicked
     * @param title
     */
    public Game(String title) {

        super(title);
        try {
            goomba  = new ImageIcon("resources/goomba.gif");
        } catch (Exception ex) {
            System.out.println(ex);
            ex.printStackTrace();
        }
        font = null ;
        try {
            //create the font to use. Specify the size!
            font = Font.createFont(Font.TRUETYPE_FONT, new File("PixelOperator8.ttf")).deriveFont(24f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(font);
        } catch (IOException e) {
            e.printStackTrace();
        } catch(FontFormatException e) {
            e.printStackTrace();
        }

        mainpanel = new JPanel(new GridLayout(1,1));
        this.add(mainpanel);
        mainpanel.setFont(font);
        start = new JPanel(new GridBagLayout());

        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);
        GridBagConstraints c2 = new GridBagConstraints();
        start.setBackground(Color.CYAN);
        c2.gridx = 2;
        c2.gridy=0;
        c2.insets = new Insets(10,0,0,0);
        JLabel lab = new JLabel("host ip", SwingConstants.CENTER);
        lab.setFont(font);
        inner.add(lab,c2);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.ipadx = 300;
        c2.ipady = 20;

        c2.gridx=0;
        c2.gridwidth = 3;
        c2.gridy=1;
        textField = new JTextField();
        inner.add(textField ,c2);
        JButton btn = new JButton("submit");
        btn.setFont(font);
        c2.fill = GridBagConstraints.NONE;
        c2.ipadx = 60;
        c2.gridy=2;
        c2.gridx=2;
        c2.gridwidth = 1;

        btn.addActionListener(new SubmitListener(this));
        inner.add(btn,c2);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = c.CENTER;
        c.ipadx = 300;
        start.add(inner, c);

        mainpanel.add(start);
        this.setSize(600, 600);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);


        try {
            System.out.println(InetAddress.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }

    /**
     * draws menu components on menu and starts game
     * a few moments later
     */
    private void drawMenu(String messageText){
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setOpaque(false);
        GridBagConstraints c2 = new GridBagConstraints();
        panel.setBackground(Color.CYAN);
        c2.gridx = 2;
        c2.gridy=0;
        c2.insets = new Insets(20,0,0,0);
        JLabel lab = new JLabel("players", SwingConstants.CENTER);
        lab.setFont(font);
        inner.add(lab,c2);
        c2.fill = GridBagConstraints.HORIZONTAL;
        c2.ipadx = 260;
        c2.ipady = 300;

        c2.gridx=0;
        c2.gridwidth = 3;
        c2.gridheight = 2;
        c2.gridy=1;
        JTextArea ta = new JTextArea();
        ta.setFont(font.deriveFont(20.0f));
        ta.setEditable(false);
        ta.setBackground(new Color(0x666666));
        ta.setForeground(Color.WHITE);

        ta.setText(messageText);
        inner.add(ta,c2);
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = c.CENTER;
        panel.add(inner, c);

        this.mainpanel.remove(start);
        this.mainpanel.removeAll();
        this.mainpanel.add(panel);
        this.mainpanel.revalidate();
        this.mainpanel.repaint();
    }

    /**
     * loads menu waiting for server
     */
    public void loadMenu() {
        drawMenu("waiting for\nserver");
        SmoothTransitioner t = new SmoothTransitioner(this);
        t.start();
    }

    /**
     * loads menu with a winner showing on screen
     * @param winner
     */
    public void loadMenu(String winner) {
        drawMenu("the winner is: \n"+winner);
        SmoothTransitioner t = new SmoothTransitioner(this, 8000);
        t.start();
    }


    public void setGoomba(int cell){
        for(JButton btn: btns){
            btn.setIcon(null);
        }
        this.btns[cell].setIcon(this.goomba);
    }

    private void loadgameGUI(){


        JPanel panel = new JPanel();
        GridLayout experimentLayout = new GridLayout(3,3);
        panel.setLayout(experimentLayout);

        for(int i=0; i<9;i++) {
            btns[i] = new JButton();
            //JButton btn = new JButton("button "+i);
            btns[i].setBackground(new Color(0xd12e2e));

            btns[i].addActionListener(new ClickListener(i, btns[i], this));
            panel.add(btns[i]);
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage("resources/hammer1.png");
        Cursor c = toolkit.createCustomCursor(image , new Point(this.mainpanel.getX(),
                this.mainpanel.getY()), "img");

        this.mainpanel.setCursor(c);
        this.mainpanel.removeAll();
        this.mainpanel.add(panel);
        this.mainpanel.revalidate();
        this.mainpanel.repaint();
    }

    public void startGame() {
        //start.setBackground(Color.BLUE);
        //this.sendtool.start();
        this.loadgameGUI();


    }

}

