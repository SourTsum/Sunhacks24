package controllers;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.File;

public class SettingsMenu extends JPanel {
    public boolean[] buttonStates = new boolean[2];
    boolean closedCaptions = true;
    SettingsFile sf = new SettingsFile("SettingsFile");

    public SettingsMenu(String optionsMenuPATH, String CloseCaptionsOnPATH,String CloseCaptionsOffPATH, String ReturnToMenuPATH) {
        buttonStates[1] = false;

        File f = new File(String.format(STR."{SaveData}\\%s.json", sf.getFileName()));
        if(f.exists()) {
            sf.load();
        } else{
            sf.save();
        }

        closedCaptions = sf.getCloseCaption();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        this.setSize(width, height);
        this.setVisible(true);
        this.setLayout(null);

        ImageIcon originalIcon = new ImageIcon(optionsMenuPATH);
        Image originalImage = originalIcon.getImage();

        double aspectRatio = (double) originalImage.getWidth(null) / originalImage.getHeight(null);
        int newWidth = width;
        int newHeight = (int) (width / aspectRatio);

        if (newHeight > height) {
            newHeight = height;
            newWidth = (int) (height * aspectRatio);
        }

        double scaleX = (double) newWidth / originalImage.getWidth(null);
        double scaleY = (double) newHeight / originalImage.getHeight(null);
        double scale = Math.min(scaleX, scaleY); // Use the smaller scale to maintain aspect ratio

        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        JLabel backgroundImage = new JLabel(new ImageIcon(scaledImage));

        backgroundImage.setBounds(0, 0, newWidth, newHeight);
        backgroundImage.setOpaque(true);

        // Create buttons with icons
        JButton ccBTN = new JButton();
        JButton returnBTN = new JButton();

        // Load icons
        ImageIcon ccOnIcon = new ImageIcon(CloseCaptionsOnPATH);
        ImageIcon ccOffIcon = new ImageIcon(CloseCaptionsOffPATH);
        ImageIcon returnIcon = new ImageIcon(ReturnToMenuPATH);

        // Resize icons based on the scale
        int scaledCCWidth = (int) (ccOnIcon.getIconWidth() * scale);
        int scaledCCHeight = (int) (ccOnIcon.getIconHeight() * scale);
        int scaledReturnWidth = (int) (returnIcon.getIconWidth() * scale);
        int scaledReturnHeight = (int) (returnIcon.getIconHeight() * scale);

        if(closedCaptions){
            ccBTN.setIcon(new ImageIcon(ccOnIcon.getImage().getScaledInstance(scaledCCWidth, scaledCCHeight, Image.SCALE_SMOOTH)));
        }else{
            ccBTN.setIcon(new ImageIcon(ccOffIcon.getImage().getScaledInstance(scaledCCWidth, scaledCCHeight, Image.SCALE_SMOOTH)));
        }

        returnBTN.setIcon(new ImageIcon(returnIcon.getImage().getScaledInstance(scaledReturnWidth, scaledReturnHeight, Image.SCALE_SMOOTH)));


        // Position buttons on the screen (keeping original icon size for positioning)
        ccBTN.setBounds(36,620, scaledCCWidth, scaledCCHeight);
        returnBTN.setBounds(50 + scaledReturnWidth, 620, scaledReturnWidth, scaledReturnHeight);

        ccBTN.setVisible(true);
        returnBTN.setVisible(true);

        ccBTN.addActionListener(e -> {
            buttonStates[0] = true;
            closedCaptions = !closedCaptions;
            if(closedCaptions){
                ccBTN.setIcon(new ImageIcon(ccOnIcon.getImage().getScaledInstance(scaledCCWidth, scaledCCHeight, Image.SCALE_SMOOTH)));
                sf.setSettings(true);
            }else{
                ccBTN.setIcon(new ImageIcon(ccOffIcon.getImage().getScaledInstance(scaledCCWidth, scaledCCHeight, Image.SCALE_SMOOTH)));
                sf.setSettings(false);
            }
            sf.save();
        });

        returnBTN.addActionListener(e -> {
            buttonStates[1] = true;
        });

        // Add buttons to the panel
        add(returnBTN);
        add(ccBTN);
        this.add(backgroundImage); // Add background image to the panel

        // Ensure the panel is revalidated and repainted
        this.revalidate();
        this.repaint();
    }
}
