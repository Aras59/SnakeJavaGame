package org.snake;


import javax.swing.*;
import java.awt.*;

import org.snake.game.SnakeGameWindow;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new SnakeGameWindow();
            frame.setVisible(true);
        });
    }
}