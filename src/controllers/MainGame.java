package controllers;

//import events.VaperEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class MainGame extends JPanel {
    public ArrayList<ImageIcon> roomIcons = new ArrayList<>();
    private int currentRoomIndex = 0; // Keep track of the current room
    public AudioPlayer backgroundMusic;
    public JLabel backgroundImage;

    public MainGame(String imagePATH, String audioPATH) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        backgroundMusic = new AudioPlayer(STR."\{audioPATH}\\background_music.wav");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        this.setSize(width, height);
        this.setLayout(null); // Using null layout for manual positioning

        // Load the background images
        roomIcons.add(new ImageIcon(imagePATH + "\\common_room.png"));
        roomIcons.add(new ImageIcon(imagePATH + "\\kitchen_room.png"));
        roomIcons.add(new ImageIcon(imagePATH + "\\hallway1_smoky_room.png")); // Changed from kitchen_room to living_room for variety
        roomIcons.add(new ImageIcon(imagePATH + "\\hallway2_smoky_room.png")); // Added a new image for variety

        updateBackgroundImage(); // Initialize with the first room

    }

    private void updateBackgroundImage() {
        // Ensure the room index is within bounds
        if (currentRoomIndex < 0 || currentRoomIndex >= roomIcons.size()) {
            return; // Invalid index, do nothing
        }

        Image originalImage = roomIcons.get(currentRoomIndex).getImage();

        // Calculate the aspect ratio for the background
        double aspectRatio = (double) originalImage.getWidth(null) / originalImage.getHeight(null);
        int newWidth = getWidth();
        int newHeight = (int) (getWidth() / aspectRatio);

        // Adjust dimensions based on the height
        if (newHeight > getHeight()) {
            newHeight = getHeight();
            newWidth = (int) (getHeight() * aspectRatio);
        }

        // Resize the background image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        backgroundImage = new JLabel(new ImageIcon(scaledImage));

        backgroundImage.setBounds(0, 0, newWidth, newHeight);
        backgroundImage.setOpaque(true);

        // Clear existing components and add new background image
        this.removeAll();
        this.add(backgroundImage);
        this.revalidate();
        this.repaint(); // Ensure the panel repaints itself after changes
    }

    public void updateRoom(int newRoomIndex) {
        // Update current room index and change room
        if (newRoomIndex >= 0 && newRoomIndex < roomIcons.size()) {
            currentRoomIndex = newRoomIndex;
            updateBackgroundImage(); // Update the displayed image
        }
    }
}
