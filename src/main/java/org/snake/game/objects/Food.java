package org.snake.game.objects;

import java.awt.*;

public class Food {
    private int foodX,foodY;
    private Color color;
    private boolean isEaten;

    public Food(int foodX, int foodY) {
        this.foodX = foodX;
        this.foodY = foodY;
        if (foodX % 3 == 0 && foodY % 3 == 0) {
            color = Color.ORANGE;
        } else {
            color = Color.RED;
        }
        this.isEaten = false;
    }

    public int getFoodX() {
        return foodX;
    }

    public void setFoodX(int foodX) {
        this.foodX = foodX;
    }

    public int getFoodY() {
        return foodY;
    }

    public void setFoodY(int foodY) {
        this.foodY = foodY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isEaten() {
        return isEaten;
    }

    public void setEaten(boolean eaten) {
        isEaten = eaten;
    }
}
