/**
 * Earl Timothy D. Malaki
 * BSCS - II
 * CMSC22 - OOP
 *
 */

import org.newdawn.slick.*;
import org.newdawn.slick.openal.Audio;

import javax.swing.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PingPong extends BasicGame{

    private Audio BallBounceSFX;

    Random rand = new Random();

    private Racquet player1;
    private Racquet player2;
    private Ball ball;

    private int gameSpeed;
    private int scorePlayer1;
    private int scorePlayer2;

    private static final int fieldWidth = 400;
    private static final int fieldHeight= 300;
    private final int fieldWidthHalf = fieldWidth / 2;
    private final int fieldHeightHalf = fieldHeight / 2;

    public PingPong(String gamename) {
        super(gamename);
    }

    public static void main(String[] args) {

        try {
            AppGameContainer appgc;
            appgc = new AppGameContainer(new PingPong("Ping Pong"));
            appgc.setDisplayMode(fieldWidth, fieldHeight, false);
            appgc.setTargetFrameRate(60);
            appgc.setShowFPS(false);
            appgc.start();

        } catch (SlickException ex) {
            Logger.getLogger(PingPong.class.getName()).log(Level.SEVERE, null, ex);
        }


    }


    @Override
    public void init(GameContainer gc) throws SlickException {
//        try {
//            BallBounceSFX = AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("Bottle.aiff"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        player1 = new Racquet(10);
        player2 = new Racquet(380);
        ball = new Ball();
        ball.setX(fieldWidthHalf);
        ball.setY(fieldHeightHalf);

        gameSpeed = 2;

        scorePlayer1 = 0;
        scorePlayer2 = 0;
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        Input input = gc.getInput();

        //  Listen for inputs from player1

        if (input.isKeyDown(Input.KEY_W)){
            if (player1.getY() - gameSpeed > 0)
                player1.setY(player1.getY() - gameSpeed);
        }
        if (input.isKeyDown(Input.KEY_S)){
            if (player1.getY() + gameSpeed < fieldHeight - player1.getHEIGHT())
                player1.setY(player1.getY() + gameSpeed);
        }

        //  Listen for inputs from player2
        if (input.isKeyDown(Input.KEY_UP)){
            if (player2.getY() - gameSpeed > 0)
                player2.setY(player2.getY() - gameSpeed);
        }
        if (input.isKeyDown(Input.KEY_DOWN)){
            if (player2.getY() + gameSpeed < fieldHeight - player2.getHEIGHT())
                player2.setY(player2.getY() + gameSpeed);
        }



        boolean changeDirection = true;
        if (ball.getY() + ball.getYa() < 0) {    // hits up side wall
            ball.setYa(gameSpeed);
        }
        else if (ball.getY() + ball.getYa() > fieldHeight - ball.getDIAMETER()) {     // hits down side wall
            ball.setYa(-gameSpeed);
        }
        else if (ball.getX() + ball.getXa() < 0) { // hits player 1 side, score for player 2
            ball.setX(fieldWidthHalf);
            ball.setY(fieldHeightHalf);
            gameSpeed = 1;
            ball.setXa(gameSpeed);
            ball.setYa(gameSpeed);
            scorePlayer2++;
            sleep();
            scoreCheck();
        }
        else if (ball.getX() + ball.getXa() > fieldWidth - ball.getDIAMETER()) {  // hits player 2 side, score for player 1
            ball.setX(fieldWidthHalf);
            ball.setY(fieldHeightHalf);
            gameSpeed = 1;
            ball.setXa(-gameSpeed);
            ball.setYa(-gameSpeed);
            scorePlayer1++;
            sleep();
            scoreCheck();
        }
        else if (collisionWith() == 1){     // hit player 1 racquet
            if (powerUp() == 3) { // power up bounce
                ball.setXa(gameSpeed * 3);
            }
            else {  // normal bounce
                gameSpeed++;
                ball.setXa(gameSpeed);
            }
        }
        else if (collisionWith() == 2){   // hit player 2 racquet
            if (powerUp() == 3) { // power up bounce
                ball.setXa(-gameSpeed * 3);
            }
            else { // normal bounce
                gameSpeed++;
                ball.setXa(-gameSpeed);
            }
        }
        else
            changeDirection = false;

//        if (changeDirection)
//            Sound.BALL.play();
        ball.setX(ball.getX() + ball.getXa());
        ball.setY(ball.getY() + ball.getYa());

        scoreCheck();
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        g.setAntiAlias(true);
        g.setBackground(Color.gray);
        g.setColor(Color.black);

        g.fillRect(player1.getX(), player1.getY(), player1.getWIDTH(), player1.getHEIGHT());    // draw player1 paddle
        g.fillRect(player2.getX(), player2.getY(), player2.getWIDTH(), player2.getHEIGHT());    // draw player2 paddle

        g.drawLine(fieldWidthHalf, 0, fieldWidthHalf, fieldHeight);     // draw field divison line

        g.drawString(""+scorePlayer1, fieldWidthHalf - 30, 10);
        g.drawString(""+scorePlayer2, fieldWidthHalf + 20, 10);

        if (ball.getXa() == gameSpeed * 3 || ball.getXa() == -gameSpeed * 3){   // if POWER UP
            g.setColor(Color.orange);       // orange ball means speed boost
        }
        g.fillOval(ball.getX(), ball.getY(), ball.getDIAMETER(), ball.getDIAMETER());       // draw ball

    }


    // Check collision of ball.
    // Returns 1 if ball collided with player 1
    // Returns 2 if ball collided with player 2
    public int collisionWith() {
        if (player1.getBounds().intersects(ball.getBounds())) {
            return 1;
        }
        else if (player2.getBounds().intersects(ball.getBounds())) {
            return 2;
        }
        return 0;

    }


    public void sleep(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void scoreCheck(){
        int winnerPlayer = 0;
        if (scorePlayer1 == 3){
            winnerPlayer = 1;
        }else if (scorePlayer2 == 3){
            winnerPlayer = 2;
        }
        int choice = 3;
        if (winnerPlayer == 1){
            // Implement message, and replay prompt
            choice = JOptionPane.showConfirmDialog(null, "Player 1 won!\nPlay again?", "Prompt", JOptionPane.YES_NO_OPTION);
        }
        else if (winnerPlayer == 2){
            // Implement message, and replay prompt
            choice = JOptionPane.showConfirmDialog(null, "Player 2 won!\nPlay again?", "Prompt", JOptionPane.YES_NO_OPTION);
        }

        if (choice == 0) {   // 0 == YES in prompt. PLAY AGAIN
            scorePlayer1 = 0;
            scorePlayer2 = 0;
            gameSpeed = 2;
        } else if (choice == 1) {        // 1 == NO in prompt
            System.exit(0);
        }


    }

    // Random power up
    // Power up is a ball bounce speed boost
    // A return of 3 means power up bounce, else normal bounce
    public int powerUp(){
        return rand.nextInt(4); // return random from 0 to 3
    }




}
