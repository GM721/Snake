import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import static java.awt.Color.*;

public class GamePanel extends JPanel {

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    static final int DELAY = 1;
    static final int SPEED = 5;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    int mSecond;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JButton restart = new JButton("Restart");

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT+50));
        this.setBackground(black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        timer =new Timer(DELAY,new MyActionListener());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        for(int i=1;i<bodyParts;i++){
            x[i]=100000*UNIT_SIZE;
            y[i]=100000*UNIT_SIZE;
        }
        x[0]=0;
        y[0]=0;
        direction='R';
        applesEaten=0;
        bodyParts = 6;
        this.remove(restart);
        timer.setDelay(DELAY);
        timer.setInitialDelay(DELAY);
        mSecond=0;
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        /*for(int i =0 ; i<SCREEN_HEIGHT/UNIT_SIZE;i++){
            g.drawLine(i*UNIT_SIZE,0,i*UNIT_SIZE,SCREEN_HEIGHT);
            g.drawLine(0,i*UNIT_SIZE,SCREEN_WIDTH,i*UNIT_SIZE);

        }*/
        if(running) {
            g.drawLine(0,SCREEN_HEIGHT,SCREEN_WIDTH,SCREEN_HEIGHT);
            g.setColor(Color.green);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(yellow);
                    g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(red);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }
            g.setColor(red);
            g.setFont(new Font("Ink Free", Font.BOLD,25));
            g.drawString("Score: " + applesEaten,0,SCREEN_HEIGHT+35);
        }
        else{
            gameOver(g);
        }
    }

    public void newApple(){
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;

        //System.out.println(GAME_UNITS);
    }

    public void move(){
        mSecond=mSecond+timer.getDelay();
        //System.out.println(mSecond+" "+timer.getDelay());
        //System.out.println(timer.getDelay());
        //System.out.println(timer.getActionListeners()[0]);
        if((mSecond==timer.getDelay()) || (mSecond%(SPEED*timer.getDelay())==0)) {
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
                //System.out.println(x[i]+" "+y[i]);
            }
            switch (direction) {
                case 'U':
                    y[0] = y[0] - UNIT_SIZE;
                    break;
                case 'D':
                    y[0] = y[0] + UNIT_SIZE;
                    break;
                case 'L':
                    x[0] = x[0] - UNIT_SIZE;
                    break;
                case 'R':
                    x[0] = x[0] + UNIT_SIZE;
                    break;
            }
        }
    }

    public void checkApple(){
        if((x[0]==appleX) && (y[0]==appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions(){
        for(int i = bodyParts;i>0;i--){
            if((x[0]==x[i]) && (y[0]==y[i])){
                running = false;
            }
        }
        if(x[0]<0){
            running = false;
        }
        if(x[0]>SCREEN_WIDTH-UNIT_SIZE){
            running = false;
        }
        if(y[0]<0){
            running = false;
        }
        if(y[0]>SCREEN_HEIGHT-UNIT_SIZE){
            running = false;
        }
        if(!running){
            timer.stop();
            //timer.removeActionListener(this);
        }
    }

    public void gameOver(Graphics g){
        g.setColor(red);
        g.setFont(new Font("Ink Free", Font.BOLD,75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over",(SCREEN_WIDTH - metrics.stringWidth("Game Over"))/2,SCREEN_HEIGHT/2-200);
        g.setFont(new Font("Ink Free", Font.BOLD,50));
        g.drawString("Score: " + applesEaten,(SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2+55,SCREEN_HEIGHT/2+75-200);
        restart.setBounds(300, 340, 200,100);
        restart.addActionListener( e-> this.startGame());
        this.add(restart);
    }

    public class MyActionListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            //System.out.println("!!!");
            if(running){
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        }
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch (e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != 'R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L'){
                        direction='R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != 'D'){
                        direction='U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U'){
                        direction='D';
                    }
                    break;
            }
        }
    }
}
