package controllers;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class Player extends JLabel implements KeyListener {
    private static SpriteSheet spriteSheet;
    private static int frame = 0;
    private boolean movingUp = false;
    private boolean movingDown = false;
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private Timer movementTimer;
    PlayerFile pf = new PlayerFile();
    private int points;

    public int room = 0;
    public boolean roomChanged = false;

    public Player(String spriteSheetPath) {
        // Assuming each sprite is 40x110 pixels in size
        spriteSheet = new SpriteSheet(spriteSheetPath, 80, 220);
        ImageIcon idle = new ImageIcon(spriteSheet.getSprite(0, 0));

        File f = new File(String.format(STR."{SaveData}\\%s.json", pf.getFileName()));
        if(f.exists()) {
            pf.load();
        } else{
            pf.save();
        }

        points = pf.getPoints();

        SwingUtilities.invokeLater(() -> {
            this.setIcon(idle);
            this.setSize(80,220);
            this.setVisible(true);
            this.setFocusable(true);
            this.requestFocusInWindow();
            this.addKeyListener(this);
        });

        // Initialize timer to handle continuous movement
        movementTimer = new Timer(58, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePlayer();
            }
        });
        movementTimer.start();
    }



    private void movePlayer() {
        int moveDistance = 10; // Adjust as necessary
        int x = getX();
        int y = getY();

        int[] startRoomBounds = {450,50,50,1080};
        int[] hallwayRoomsBounds = {301,175,50,1080};

        int[] bounds = room < 2 ? startRoomBounds : hallwayRoomsBounds;

        // Move and animate based on current direction
        if (movingDown && y + moveDistance <= bounds[0]) {
            this.setLocation(x, y + moveDistance);
            animateMovement(0); // Moving down animation
        } else if (movingUp && y - moveDistance >= bounds[1]) {
            this.setLocation(x, y - moveDistance);
            animateMovement(3); // Moving up animation
        } else if (movingLeft && x - moveDistance >= bounds[2]) {
            this.setLocation(x - moveDistance, y);
            animateMovement(2); // Moving left animation
        } else if (movingRight && x + moveDistance - getWidth() <= bounds[3]) {
            this.setLocation(x + moveDistance, y);
            animateMovement(1); // Moving right animation
        } else if (movingRight && x + moveDistance > bounds[3]) {
            // Teleport to the new location and increment the room
            if (room < 3) { // Only increment if room is less than 3
                this.setLocation(150, 400);
                room++; // Increment room
                System.out.println("Room incremented to: " + room); // Debug output
                roomChanged = true;
                adjustPlayerYBounds(); // Adjust Y bounds based on room number
            }
        } else if (movingLeft && x - moveDistance < bounds[2]) {
            // Teleport to the new location and decrement the room
            if (room >= 1) {
                this.setLocation(1080, 350); // Assuming you want to teleport to a specific location
                room--; // Decrement room
                System.out.println("Room decremented to: " + room); // Debug output
                roomChanged = true;
                adjustPlayerYBounds(); // Adjust Y bounds based on room number
            }
        } else {
            // If not moving, reset to idle frame
            if (!movingUp && !movingDown && !movingLeft && !movingRight) {
                resetToIdle();
            }
        }
    }


    // Method to adjust player Y bounds based on the room number
    private void adjustPlayerYBounds() {
        int padding = 150; // Padding from the top and bottom
        int baseY = 400;   // Base Y position when in room 0 or 1

        // Change Y position based on the room number
        if (room == 2 || room == 3) {
            // Adjust to a new Y position based on padding
            this.setLocation(getX(), baseY - padding);
        } else {
            // Reset to the base Y position when in room 0 or 1
            this.setLocation(getX(), baseY);
        }
    }





    private void animateMovement(int direction) {
        if (frame % 2 == 0) {
            SwingUtilities.invokeLater(() -> {
                this.setIcon(new ImageIcon(spriteSheet.getSprite(direction, (frame / 2) % 2 + 1))); // Animation frames
                this.revalidate();
            });
        }

        if ((frame / 2) % 2 + 1 == 3) {
            frame = 0; // Reset frame back to 0 when it reaches 3
        } else {
            frame++; // Otherwise, keep incrementing the frame
        }
    }


    private void resetToIdle() {
        // Set the idle icon
        SwingUtilities.invokeLater(() -> {
            this.setIcon(new ImageIcon(spriteSheet.getSprite(0, 0))); // Idle frame
            this.revalidate();
        });
        frame = 0; // Reset frame for idle animation
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            movingDown = true;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            movingUp = true;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            movingLeft = true;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movingRight = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            movingDown = false;
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            movingUp = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            movingLeft = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            movingRight = false;
        }

        // Check if still moving before resetting to idle
        if (!movingUp && !movingDown && !movingLeft && !movingRight) {
            resetToIdle(); // Reset to idle frame on key release if not moving
        }
    }

    public void incrementPoints(){
        points+= 10;
        pf.setPoints(points);
    }
}
