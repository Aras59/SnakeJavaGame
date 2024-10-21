package org.snake.map;

import static java.lang.Math.abs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.snake.game.objects.Food;
import org.snake.game.objects.Snake;

public class SnakeGameArea extends JPanel implements ActionListener {
    private static final Logger LOGGER = LogManager.getLogger(SnakeGameArea.class);

    public static final int SEGMENT_SIZE = 50;
    private final int AREA_WIDTH;
    private final int AREA_HEIGHT;
    private final int DELAY = 200;
    private final ImageIcon bgImage;

    //OBJECTS
    private Snake snake;
    private final ArrayList<Food> foodList = new ArrayList<>();

    //GAME VARIABLES
    private int score;
    private boolean running = false;
    private final Random random;
    private KeyEvent moveEvent;
    private Timer timer;

    public SnakeGameArea(int width, int height) {
        AREA_WIDTH = width;
        AREA_HEIGHT = height;
        this.bgImage = new ImageIcon("src/main/resources/grass.jpeg");
        random = new Random();
        setOpaque(true);
        setPreferredSize(new Dimension(AREA_WIDTH * SEGMENT_SIZE, AREA_HEIGHT * SEGMENT_SIZE));
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (moveEvent == null || event.getKeyChar() != moveEvent.getKeyChar()) { // CONDITION TO PREVENT KEY SPAMMING (SNAKE SPEEDBOOST)
                    handleKeyInput(event);
                }
            }
        });
        gameStart();
    }

    @Override
    protected void paintComponent(Graphics graphicsManager) {
        super.paintComponent(graphicsManager);
        graphicsManager.drawImage(bgImage.getImage(), 0, 0, AREA_WIDTH, AREA_HEIGHT, this);
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
            if (snake.getSnakeLength() == (AREA_HEIGHT / SEGMENT_SIZE) * (AREA_WIDTH / SEGMENT_SIZE)) {
                showWinGame(graphicsManager);
            } else {
                showGameOver(graphicsManager);
            }
        }
        printScore(graphicsManager);
        Toolkit.getDefaultToolkit().sync();
    }

    //GAME LOGIC
    private void gameStart() {
        this.snake = new Snake(3, AREA_WIDTH, AREA_HEIGHT);
        score = 0;
        foodList.add(createFood());
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
            handleFoodList();
        }
    }

    private Food createFood() {
        int randomX = random.nextInt(AREA_WIDTH / SEGMENT_SIZE );
        int randomY = random.nextInt(AREA_HEIGHT / SEGMENT_SIZE );
        return new Food(abs(randomX * SEGMENT_SIZE - SEGMENT_SIZE), abs(randomY * SEGMENT_SIZE - SEGMENT_SIZE));
    }

    private void handleSnakeActions(Snake snake) {
        LOGGER.info("SNAKE POSITION: X - {} Y - {}", snake.getX()[0], snake.getY()[0]);
        for (Food food : foodList) {
            if (snake.checkIfSnakeEatFood(food)) {
                if (food.getColor().equals(Color.ORANGE)) {
                    score += 500;
                } else {
                    score += 100;
                }
                snake.setCollectedFood(snake.getCollectedFood() + 1);                              //HANDLE SCORE
                snake.getX()[snake.getSnakeLength()] = snake.getX()[snake.getSnakeLength() - 1];
                snake.getY()[snake.getSnakeLength()] = snake.getY()[snake.getSnakeLength() - 1];
                snake.setSnakeLength(snake.getSnakeLength() + 1);                                 //HANDLE SNAKE BODY SEGMENT ADD
            }
        }
        if (snake.checkIfSnakeNotHitHimself() || checkIfSnakeHitWall(snake)) {
            timer.stop();                                                                        //HANDLE GAME END
            running = false;
        }
    }

    private boolean checkIfSnakeHitWall (Snake snake) {
         if (snake.getX()[0] == AREA_HEIGHT || snake.getX()[0] < 0 || snake.getY()[0] == AREA_WIDTH || snake.getY()[0] < 0 ){
            LOGGER.info("SNAKE HIT THE WALL AT:  X - {} Y - {} ", snake.getX()[0], snake.getY()[0]);
            return true;
         }
         return false;
    }

    private void handleFoodList() {
        foodList.removeIf(Food::isEaten);
        if (foodList.isEmpty()) {
            LOGGER.info("ALL FOODS HAVE BEEN EATEN!");
            Food newFood = createFood();
            foodList.add(newFood);
            LOGGER.info("CREATE A NEW ONE AT:  X - {} Y - {} ", newFood.getFoodX(), newFood.getFoodY());
        }
    }

    private void showGameOver(Graphics g) {
        String message = "Game Over!";
        String scoreMessage = "Score: " + score + " Collected food: " + snake.getCollectedFood();
        Font font = new Font("Helvetica", Font.BOLD, 35);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(message, (AREA_WIDTH - metrics.stringWidth(message)) / 2, AREA_HEIGHT / 2);
        g.drawString(scoreMessage, (AREA_WIDTH - metrics.stringWidth(scoreMessage)) / 2, AREA_HEIGHT / 2 + SEGMENT_SIZE);
    }

    private void showWinGame(Graphics g) {
        String message = "Winner!";
        String scoreMessage = "Score: " + score + " Collected food: " + snake.getCollectedFood();
        Font font = new Font("Helvetica", Font.BOLD, 35);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(message, (AREA_WIDTH - metrics.stringWidth(message)) / 2, AREA_HEIGHT / 2);
        g.drawString(scoreMessage, (AREA_WIDTH - metrics.stringWidth(scoreMessage)) / 2, AREA_HEIGHT / 2 + SEGMENT_SIZE);
    }

    private void printScore(Graphics g) {
        String scoreMessage = "Score: " + score + " Collected food: " + snake.getCollectedFood();
        Font font = new Font("Helvetica", Font.BOLD, 20);
        FontMetrics metrics = getFontMetrics(font);
        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(scoreMessage, (AREA_WIDTH - metrics.stringWidth(scoreMessage)) / 2, SEGMENT_SIZE / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            handleKeyInput(moveEvent);
        }
        repaint();
    }

}
