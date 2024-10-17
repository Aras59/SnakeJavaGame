package org.snake.map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import org.snake.game.objects.Food;
import org.snake.game.objects.Snake;

public class SnakeGameArea extends JPanel implements ActionListener {

    public static final int SEGMENT_SIZE = 25;
    private final int WIDTH;
    private final int HEIGHT;
    private final int DELAY = 150;
    private final ImageIcon bgImage;

    //OBJECTS
    private Snake snake;
    private ArrayList<Food> foodList = new ArrayList<>();

    //GAME VARIABLES
    private int score;
    private boolean running = false;
    private Random random;
    private KeyEvent moveEvent;
    private Timer timer;

    public SnakeGameArea(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        this.bgImage = new ImageIcon("src/main/resources/grass.jpeg");
        random = new Random();
        setOpaque(true);
        setPreferredSize(new Dimension(WIDTH * SEGMENT_SIZE, HEIGHT * SEGMENT_SIZE));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyInput(e);
            }
        });
        gameStart();
    }

    @Override
    protected void paintComponent(Graphics graphicsManager) {
        super.paintComponent(graphicsManager);
        graphicsManager.drawImage(bgImage.getImage(), 0, 0, WIDTH, HEIGHT, this);

        if (running) {
            for (int i = snake.getSnakeLength() -1 ; i >= 0; i--) {
                if (i == 0) {
                    graphicsManager.setColor(Color.BLACK);
                    graphicsManager.fillRect(snake.getX()[i], snake.getY()[i], SEGMENT_SIZE, SEGMENT_SIZE);
                } else {
                    graphicsManager.setColor(Color.YELLOW);
                    graphicsManager.fillRect(snake.getX()[i], snake.getY()[i], SEGMENT_SIZE, SEGMENT_SIZE);
                }
            }

            for (Food food : foodList) {
                graphicsManager.setColor(food.getColor());
                graphicsManager.fillRect(food.getFoodX(), food.getFoodY(), SEGMENT_SIZE, SEGMENT_SIZE);
            }
        } else {
            showGameOver(graphicsManager);
        }

        Toolkit.getDefaultToolkit().sync();
    }

    //GAME LOGIC
    private void gameStart() {
        this.snake = new Snake(3, WIDTH , HEIGHT);
        foodList.add(addFood());
        timer = new Timer(DELAY, this);
        timer.start();
        running = true;
    }

    private void handleKeyInput(KeyEvent event) {
        if (event != null)  {
            moveEvent = event;
            switch (moveEvent.getKeyChar()) {
                case 'a', 'A':
                    snake.snakeMove("LEFT");
                    break;
                case 'd', 'D':
                    snake.snakeMove("RIGHT");
                    break;
                case 'w', 'W':
                    snake.snakeMove("UP");
                    break;
                case 's', 'S':
                    snake.snakeMove("DOWN");
                    break;
            }
            handleSnakeActions(snake);
        }
    }

    private Food addFood() {
        int randomX = random.nextInt(24);
        int randomY = random.nextInt(24);
        return new Food(randomX * SEGMENT_SIZE, randomY * SEGMENT_SIZE);
    }

    private void handleSnakeActions(Snake snake) {
        for (Food food : foodList) {
            if (snake.checkIfSnakeEatFood(food)) handleKeyInput(moveEvent);
        }
        score += snake.getCollectedFood() * 100;
        if (snake.checkIfSnakeNotHitHimself() || checkIfSnakeHitWall(snake)) {
            timer.stop();
            running = false;
        }
    }

    private boolean checkIfSnakeHitWall (Snake snake) {
        return snake.getX()[0] == HEIGHT || snake.getX()[0] == 0 || snake.getY()[0] == WIDTH || snake.getY()[0] == 0;
    }

    private void handleFoodList() {
        foodList.removeIf(Food::isEaten);
        int bet = random.nextInt( 10);
        if (bet % 10 == 0) {
            foodList.add(addFood());
        }
    }

    private void showGameOver(Graphics g) {
        String message = "Game Over";
        Font font = new Font("Helvetica", Font.BOLD, 40);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(message, (WIDTH - metrics.stringWidth(message)) / 2, HEIGHT / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            handleFoodList();
            handleSnakeActions(snake);
            handleKeyInput(moveEvent);
        }
        repaint();
    }

}
