import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener {

    // Game settings
    private final int WIDTH = 300;
    private final int HEIGHT = 300;
    private final int DOT_SIZE = 10;
    private final int MAX_DOTS = 900;
    private final int RAND_POS = 29;
    private int DELAY = 140;

    // Snake body position arrays
    private final int[] x = new int[MAX_DOTS];
    private final int[] y = new int[MAX_DOTS];

    private int lengthOfSnake;
    private int appleX;
    private int appleY;

    // Directions for movement
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean isGameRunning = true;

    // Timer for game loop
    private Timer gameTimer;

    public SnakeGame() {
        // Initialize the game
        initGame();
    }

    private void initGame() {
        // Set up the game window and key listener
        addKeyListener(new KeyInputAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        // Set up game variables
        lengthOfSnake = 3;
        for (int i = 0; i < lengthOfSnake; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }
        placeApple();

        // Start the game loop
        gameTimer = new Timer(DELAY, this);
        gameTimer.start();
    }

    // Called to update the game graphics
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (isGameRunning) {
            drawSnake(g);
            drawApple(g);
            moveSnake();
            checkCollisions();
        } else {
            displayGameOver(g);
        }
    }

    // Draw the snake
    private void drawSnake(Graphics g) {
        for (int i = 0; i < lengthOfSnake; i++) {
            if (i == 0) {
                g.setColor(Color.green);  // Head of the snake
            } else {
                g.setColor(Color.blue);   // Body of the snake
            }
            g.fillRect(x[i], y[i], DOT_SIZE, DOT_SIZE);
        }
    }

    // Draw the apple
    private void drawApple(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(appleX, appleY, DOT_SIZE, DOT_SIZE);
    }

    // Move the snake in the current direction
    private void moveSnake() {
        for (int i = lengthOfSnake; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        if (left) {
            x[0] -= DOT_SIZE;
        }
        if (right) {
            x[0] += DOT_SIZE;
        }
        if (up) {
            y[0] -= DOT_SIZE;
        }
        if (down) {
            y[0] += DOT_SIZE;
        }
    }

    // Check for collisions
    private void checkCollisions() {
        // Game over if snake hits the wall
        if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
            isGameRunning = false;
        }

        // Game over if snake collides with itself
        for (int i = lengthOfSnake; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                isGameRunning = false;
            }
        }

        // Snake eats the apple
        if (x[0] == appleX && y[0] == appleY) {
            lengthOfSnake++;
            placeApple();
            if (DELAY > 50) { // Set a lower limit to avoid too high speed
            DELAY -= 5;
            gameTimer.stop();  // Stop the current timer
            gameTimer = new Timer(DELAY, this);  // Create a new timer with the updated delay
            gameTimer.start();
        }
    }
    }

    // Place the apple at a random position
    private void placeApple() {
        appleX = (int) (Math.random() * RAND_POS) * DOT_SIZE;
        appleY = (int) (Math.random() * RAND_POS) * DOT_SIZE;
    }

    // Display game over message
    private void displayGameOver(Graphics g) {
        String message = "Game Over! Press R to Restart.";
        g.setColor(Color.white);
        g.drawString(message, WIDTH / 4, HEIGHT / 2);
    }

    // Handle key inputs for controlling the snake
    private class KeyInputAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_LEFT && !right) {
                left = true;
                up = down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left) {
                right = true;
                up = down = false;
            }
            if (key == KeyEvent.VK_UP && !down) {
                up = true;
                left = right = false;
            }
            if (key == KeyEvent.VK_DOWN && !up) {
                down = true;
                left = right = false;
            }
            if (key == KeyEvent.VK_R && !isGameRunning) {
                restartGame();
            }
        }
    }

    // Restart the game
    private void restartGame() {
        lengthOfSnake = 3;
        for (int i = 0; i < lengthOfSnake; i++) {
            x[i] = 50 - i * DOT_SIZE;
            y[i] = 50;
        }
        left = false;
        right = true;
        up = false;
        down = false;
        isGameRunning = true;
        placeApple();
        gameTimer.start();
    }

    // Called by the timer every DELAY milliseconds
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        // Set up the window and start the game
        javax.swing.JFrame frame = new javax.swing.JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
