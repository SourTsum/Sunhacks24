package controllers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class NPC extends JLabel {
    private static SpriteSheet spriteSheet;
    private static int frame = 0;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean isIdle = true; // Track if NPC is idle

    private Timer movementTimer;
    private Random random;

    public NPC(String spriteSheetPath) {
        // Assuming each sprite is 40x110 pixels in size
        spriteSheet = new SpriteSheet(spriteSheetPath, 40, 110);
        ImageIcon idle = new ImageIcon(spriteSheet.getSprite(0, 0));

        SwingUtilities.invokeLater(() -> {
            this.setIcon(idle);
            this.setSize(idle.getIconWidth(), idle.getIconHeight());
            this.setVisible(true);
        });

        random = new Random();

        // Initialize timer to handle movement
        movementTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveNPC();
            }
        });
        movementTimer.start();

        // Initialize a separate timer to change direction randomly
        Timer directionChangeTimer = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isIdle) {
                    // Decide whether to move or stay idle
                    if (random.nextBoolean()) {
                        changeDirection();
                    } else {
                        stayIdle();
                    }
                }
            }
        });
        directionChangeTimer.start();
    }

    private void changeDirection() {
        // Stop current movement
        movingDown = false;
        movingUp = false;
        movingLeft = false;
        movingRight = false;

        // Randomly choose a new direction
        int direction = random.nextInt(4);
        switch (direction) {
            case 0: // Down
                movingDown = true;
                break;
            case 1: // Right
                movingRight = true;
                break;
            case 2: // Up
                movingUp = true;
                break;
            case 3: // Left
                movingLeft = true;
                break;
        }

        isIdle = false; // NPC is now moving
        frame = 0; // Reset frame to restart animation
    }

    private void stayIdle() {
        movingDown = false;
        movingUp = false;
        movingLeft = false;
        movingRight = false;

        isIdle = true; // NPC is now idle
        frame = 0; // Reset frame for idle animation
        updateIdleAnimation(); // Set idle animation
        int idleDuration = 1000 + random.nextInt(2000); // Random idle duration between 1 to 3 seconds

        // After the idle duration, the NPC can start moving again
        Timer idleTimer = new Timer(idleDuration, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeDirection(); // After idle, change direction
            }
        });
        idleTimer.setRepeats(false); // Ensure this timer only runs once
        idleTimer.start();
    }

    private void moveNPC() {
        int moveDistance = 5; // Reduced move distance for smoother movement
        int x = getX();
        int y = getY();

        // Move and animate based on current direction
        if (movingDown && y + moveDistance <= 600 - getHeight()) {
            this.setLocation(x, y + moveDistance);
            animateMovement(0);
        } else if (movingUp && y - moveDistance >= 0) {
            this.setLocation(x, y - moveDistance);
            animateMovement(3);
        } else if (movingLeft && x - moveDistance >= 0) {
            this.setLocation(x - moveDistance, y);
            animateMovement(2);
        } else if (movingRight && x + moveDistance <= 800 - getWidth()) {
            this.setLocation(x + moveDistance, y);
            animateMovement(1);
        } else {
            stayIdle(); // If NPC can't move, stay idle
        }
    }

    private void animateMovement(int direction) {
        // Update the icon for animation
        if (frame % 2 == 0) {
            SwingUtilities.invokeLater(() -> {
                this.setIcon(new ImageIcon(spriteSheet.getSprite(direction, (frame / 2) % 2 + 1))); // Animation frames
                this.revalidate();
            });
        }
        frame++;
    }

    private void updateIdleAnimation() {
        // Set the idle icon
        SwingUtilities.invokeLater(() -> {
            this.setIcon(new ImageIcon(spriteSheet.getSprite(0, 0))); // Idle animation (first frame)
            this.revalidate();
        });
    }
}
