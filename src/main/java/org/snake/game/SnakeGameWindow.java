package org.snake.game;

import javax.swing.*;
import java.awt.*;

import org.snake.map.SnakeGameArea;

public class SnakeGameWindow extends JFrame {
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    public SnakeGameWindow() throws HeadlessException {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        setTitle("Snake The Game");
        setLayout(null);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel gameArena = new SnakeGameArea(WIDTH, HEIGHT);
        gameArena.setBounds(0,0, WIDTH, HEIGHT);

        add(gameArena);
    }
}
