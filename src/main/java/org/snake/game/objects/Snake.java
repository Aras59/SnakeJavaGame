package org.snake.game.objects;

import static org.snake.map.SnakeGameArea.SEGMENT_SIZE;

public class Snake {
    private int snakeLength;
    private int[] x;
    private int[] y;
    private int collectedFood;

    public Snake(int startLength, int screenWidth, int screenHeight) {
        snakeLength = startLength;
        this.x = new int[screenWidth];
        this.y = new int[screenHeight];
        collectedFood = 0;

        for (int i = 0; i < snakeLength; i++) {
            x[i] = 50 - i * SEGMENT_SIZE;
            y[i] = 50;
        }
    }

    public void snakeMove(String direction) {
        for (int i = snakeLength -1 ; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case "LEFT" :
                x[0] = x[0] - SEGMENT_SIZE;
                break;
            case "RIGHT" :
                x[0] += SEGMENT_SIZE;
                break;
            case "UP" :
                y[0] = y[0] - SEGMENT_SIZE;
                break;
            case "DOWN" :
                y[0] += SEGMENT_SIZE;
                break;
        }
    }

    public boolean checkIfSnakeEatFood(Food food) {
        if (food != null) {
            if (food.getFoodX() == x[0] && food.getFoodY() == y[0]) {
                food.setEaten(true);
                collectedFood += 1;
                snakeLength += 1;
                return true;
            }
        }
        return false;
    }

    public boolean checkIfSnakeNotHitHimself () {
        for (int i = 1; i < snakeLength; i++) {
            if (x[0] == x[i] && y[0] == y[i]) return true;
        }
        return false;
    }

    public int[] getX() {
        return x;
    }

    public void setX(int[] x) {
        this.x = x;
    }

    public int[] getY() {
        return y;
    }

    public void setY(int[] y) {
        this.y = y;
    }

    public int getSnakeLength() {
        return snakeLength;
    }

    public void setSnakeLength(int snakeLength) {
        this.snakeLength = snakeLength;
    }

    public int getCollectedFood() {
        return collectedFood;
    }

    public void setCollectedFood(int collectedFood) {
        this.collectedFood = collectedFood;
    }
}
