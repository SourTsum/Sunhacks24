//package controllers;
//
//import events.Pandemonium;
//
//import javax.sound.sampled.LineUnavailableException;
//import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.IOException;
//
//public class SmokeSprite extends JLabel {
//
//    public SmokeSprite(int xPos, int yPos, JFrame mainPanel, String audioPATH, String imagePATH) {
//        // Set the initial icon
//        this.setLocation(xPos, yPos);
//        this.setSize(187, 250);
//        this.setBackground(Color.GREEN);
//        this.setOpaque(true); // Needed to show background color on JLabel
//        this.setVisible(true);
//
//        this.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                try {
//                    new Pandemonium(, imagePATH, audioPATH,"").start();
//                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException |
//                         AWTException | FontFormatException | InterruptedException ex) {
//                    throw new RuntimeException(ex);
//                }
//            }
//        });
//    }
//}
