package controllers;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PlayTransition extends JPanel {
    public Dialogue intro;
    public PlayTransition(String dialoguePATH, String imagePATH) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        this.setSize(width, height);
        this.setBackground(Color.BLACK);
        this.setVisible(true);
        this.setLayout(null);

        intro = new Dialogue(PlayTransition.this);
        intro.generateFromFile(new File(dialoguePATH + "/intro.txt"));
        intro.play(); // Ensure the dialogue processing is done asynchronously
    }
}
