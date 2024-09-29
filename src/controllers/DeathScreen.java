package controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeathScreen extends JFrame implements ActionListener {
    private ImageIcon[] deathImages; // Array to hold the images
    private int currentFrame = 0; // Current frame index
    private Timer timer; // Timer for animation

    public DeathScreen(String imagePath) {
        // Load images into the array
        deathImages = new ImageIcon[7];
        for (int i = 1; i <= 7; i++) {
            deathImages[i - 1] = new ImageIcon(imagePath + "/death_screen_animated" + i + ".png");
        }

        // Set the size of the JFrame to 1920 x 1080
        this.setSize(1920, 1080); // Adjusted to the desired resolution
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Center the frame on the screen
        this.setResizable(false); // Prevent resizing
        this.setBackground(Color.BLACK); // Set background color if desired
        this.setLayout(new BorderLayout()); // Use a layout manager

        // Create and start the timer
        timer = new Timer(100, this); // Change 100 to control the frame rate (milliseconds)
        timer.start(); // Timer will now start
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g); // Call the superclass method
        // Ensure currentFrame is within bounds before drawing the image
        if (currentFrame < deathImages.length && deathImages[currentFrame] != null) {
            g.drawImage(deathImages[currentFrame].getImage(), 0, 0, getWidth(), getHeight(), this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Update to the next frame
        currentFrame++;
        if (currentFrame >= deathImages.length) {
            // Stop the animation when the last frame is reached
            timer.stop();
            currentFrame = deathImages.length - 1; // Ensure the frame index does not go out of bounds
        }
        repaint(); // Trigger a repaint to show the new frame
    }

    public void stopAnimation() {
        timer.stop(); // Method to stop the animation if needed
    }

    public void startAnimation() {
        currentFrame = 0; // Reset the frame to the beginning
        timer.start(); // Method to start the animation if needed
    }
}
