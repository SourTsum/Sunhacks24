package controllers;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TypeWriterLabel extends JLabel {
    private String text;
    private int charIndex = 0;
    private Timer timer;
    private static final int MAX_LABELS = 2; // Max number of labels allowed on screen
    private static List<TypeWriterLabel> activeLabels = new ArrayList<>(); // Store active labels
    private static Font gameFont;
    private static final float FADE_OUT_TIME_PER_CHAR = 50; // milliseconds per character for fade out
    private static final float FADE_OUT_STEP = 0.05f; // Opacity decrease per step

    static {
        try {
            // Load the game font
            gameFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream("C:\\Users\\alexa\\IdeaProjects\\JavaTestField\\assets\\font\\zekton.ttf"));
            gameFont = gameFont.deriveFont(Font.BOLD, 25f); // Set size and style
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            gameFont = new Font("Arial", Font.BOLD, 25); // Fallback to default font if loading fails
        }
    }

    public TypeWriterLabel(String text, boolean italics) {
        super("", SwingConstants.CENTER);
        super.setForeground(Color.white);
        this.text = text;

        // Set font to the game font (Zekton)
        if(italics) {
            setFont(gameFont.deriveFont(Font.ITALIC,25f));
        } else {
            setFont(gameFont);
        }

        // Center label horizontally
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();
        setSize(width, gameFont.getSize() + 1);

        // Set location towards bottom-middle of the screen
        int yPosition = height - gameFont.getSize() - 100;  // Adjust for some bottom margin
        this.setLocation(0, yPosition);

        // Ensure max labels
        if (activeLabels.size() == MAX_LABELS) {
            deleteOldestLabel(); // Remove the oldest label if max is reached
        }

        // Add current label to the list of active labels
        activeLabels.add(this);

        // Push the second-to-last label up if there are two labels
        if (activeLabels.size() > 1) {
            TypeWriterLabel secondLastLabel = activeLabels.get(activeLabels.size() - 2);
            secondLastLabel.setLocation(0, yPosition - 26); // Move the second-to-last label up
        }
    }

    public void type() {
        // Timer to simulate typing effect
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                if (charIndex < text.length()) {
                    setText(getText() + text.charAt(charIndex));
                    charIndex++;
                } else {
                    timer.cancel();  // Stop the timer once typing is complete
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    fadeOut();       // Start fade-out after typing is complete
                }
            }
        };
        timer.schedule(task, 0, 50); // 100 ms delay between characters
    }

    private void fadeOut() {
        Timer fadeTimer = new Timer();
        TimerTask fadeTask = new TimerTask() {
            float alpha = 1.0f; // Start with full opacity
            int totalSteps = (int) (text.length() / FADE_OUT_STEP); // Calculate total steps based on text length
            long fadeDuration = totalSteps * (long) FADE_OUT_TIME_PER_CHAR; // Calculate total fade duration
            long fadeInterval = fadeDuration / totalSteps; // Calculate fade interval based on steps

            @Override
            public void run() {
                if (alpha > 0) {
                    alpha -= FADE_OUT_STEP; // Decrease opacity
                    if (alpha < 0) alpha = 0; // Ensure alpha doesn't go below 0
                    setForeground(new Color(255, 255, 255, (int) (alpha * 255))); // Update color with new alpha
                } else {
                    fadeTimer.cancel(); // Stop the fade timer
                    deleteOldestLabel(); // Remove the label once faded out
                }
            }
        };

        // Schedule the fade task based on the calculated interval
        fadeTimer.schedule(fadeTask, 0, (long) (FADE_OUT_TIME_PER_CHAR)); // Using constant FADE_OUT_TIME_PER_CHAR for smoother fading
    }

    private void deleteOldestLabel() {
        TypeWriterLabel oldestLabel = activeLabels.removeFirst(); // Remove the oldest label from the list
        oldestLabel.getParent().remove(oldestLabel);  // Remove label from its parent container
        oldestLabel.repaint();  // Repaint the parent to reflect the change

        // Push the new second-to-last label up if necessary
        if (!activeLabels.isEmpty()) {
            TypeWriterLabel newSecondLastLabel = activeLabels.getLast();
            int yPosition = oldestLabel.getY(); // Get the old Y position to adjust
            newSecondLastLabel.setLocation(0, yPosition + 26); // Adjust position back to normal
        }
    }
}
