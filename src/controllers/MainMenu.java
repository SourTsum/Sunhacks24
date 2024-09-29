package controllers;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainMenu extends JPanel {
    public boolean[] buttonStates = new boolean[3];

    public MainMenu(String mainMenuPATH, String playPATH, String optionsPATH, String helpPATH, String audioPATH) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        AudioPlayer mainTheme = new AudioPlayer(audioPATH);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        this.setSize(width, height);
        this.setVisible(true);
        this.setLayout(null);

        // Load the background image
        ImageIcon originalIcon = new ImageIcon(mainMenuPATH);
        Image originalImage = originalIcon.getImage();

        // Calculate the aspect ratio for the background
        double aspectRatio = (double) originalImage.getWidth(null) / originalImage.getHeight(null);
        int newWidth = width;
        int newHeight = (int) (width / aspectRatio);

        // Adjust dimensions based on the height
        if (newHeight > height) {
            newHeight = height;
            newWidth = (int) (height * aspectRatio);
        }

        // Calculate scale factor
        double scaleX = (double) newWidth / originalImage.getWidth(null);
        double scaleY = (double) newHeight / originalImage.getHeight(null);
        double scale = Math.min(scaleX, scaleY); // Use the smaller scale to maintain aspect ratio

        // Resize the background image
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        JLabel backgroundImage = new JLabel(new ImageIcon(scaledImage));

        backgroundImage.setBounds(0, 0, newWidth, newHeight);
        backgroundImage.setOpaque(true);

        // Create buttons with icons
        JButton playBTN = new JButton();
        JButton optionsBTN = new JButton();
        JButton helpBTN = new JButton();

        // Load icons
        ImageIcon playIcon = new ImageIcon(playPATH);
        ImageIcon optionsIcon = new ImageIcon(optionsPATH);
        ImageIcon helpIcon = new ImageIcon(helpPATH);

        // Resize icons based on the scale
        int scaledPlayWidth = (int) (playIcon.getIconWidth() * scale);
        int scaledPlayHeight = (int) (playIcon.getIconHeight() * scale);
        int scaledOptionsWidth = (int) (optionsIcon.getIconWidth() * scale);
        int scaledOptionsHeight = (int) (optionsIcon.getIconHeight() * scale);
        int scaledHelpWidth = (int) (helpIcon.getIconWidth() * scale);
        int scaledHelpHeight = (int) (helpIcon.getIconHeight() * scale);

        playBTN.setIcon(new ImageIcon(playIcon.getImage().getScaledInstance(scaledPlayWidth, scaledPlayHeight, Image.SCALE_SMOOTH)));
        optionsBTN.setIcon(new ImageIcon(optionsIcon.getImage().getScaledInstance(scaledOptionsWidth, scaledOptionsHeight, Image.SCALE_SMOOTH)));
        helpBTN.setIcon(new ImageIcon(helpIcon.getImage().getScaledInstance(scaledHelpWidth, scaledHelpHeight, Image.SCALE_SMOOTH)));

        // Position buttons on the screen (keeping original icon size for positioning)
        playBTN.setBounds(36, 513, scaledPlayWidth, scaledPlayHeight);
        optionsBTN.setBounds(36,620, scaledOptionsWidth, scaledOptionsHeight);
        helpBTN.setBounds(50 + scaledOptionsWidth, 620, scaledHelpWidth, scaledHelpHeight);

        // Make buttons visible
        playBTN.setVisible(true);
        optionsBTN.setVisible(true);
        helpBTN.setVisible(true);


        playBTN.addActionListener(e -> {
            mainTheme.stop();
            buttonStates[0] = true;
        });

        optionsBTN.addActionListener(e -> {
            mainTheme.stop();
            buttonStates[1] = true;
        });

        // Add buttons to the panel
        add(playBTN);
        add(optionsBTN);
        add(helpBTN);
        this.add(backgroundImage); // Add background image to the panel

        // Ensure the panel is revalidated and repainted
        revalidate();
        repaint();
        mainTheme.play();
    }
}
