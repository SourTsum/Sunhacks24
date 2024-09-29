package events;

import controllers.AudioPlayer;
import controllers.DeathScreen;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class Pandemonium {
    private final JFrame mainFrame; // mainFrame is a JPanel
    private final AudioPlayer KnockKnockOST;
    private final Robot robot;
    private boolean playerInCircle = false;
    private JPanel healthBar;
    private int healthBarWidth = 200; // Initial width
    private Timer swayTimer; // Timer for cursor sway
    private boolean healthBarActive = true; // Flag to control health bar updates
    private final int anchorY;

    private final double[] healthConstants = {1.15, 10};

    public boolean playerDied = false;

    private String fontPath;
    private String imagePath;

    public Pandemonium(JFrame mainFrame, String imagePATH, String audioPATH, String fontPATH) throws UnsupportedAudioFileException, LineUnavailableException, IOException, AWTException {
        this.mainFrame = mainFrame;
        KnockKnockOST = new AudioPlayer(audioPATH + "\\vaper_door_music.wav");
        robot = new Robot();
        anchorY = mainFrame.getHeight() / 2 - 150;
        this.imagePath = imagePATH;
        this.fontPath = fontPATH;
    }

    public void start() throws IOException, FontFormatException, InterruptedException, UnsupportedAudioFileException, LineUnavailableException {
        mainFrame.setLayout(null); // Disable layout management
        addTopText();
        addBar();
        addHealthBar();
        addCenter();

        mainFrame.setCursor(createCustomCursor());
        startCursorSway();

        mainFrame.setVisible(true); // Ensure frame is visible
        mainFrame.repaint();

        cursorThrowSequence();
    }


    private void playerDied() throws InterruptedException {
        playerDied = true;
        System.out.println("[CLIENT]: Player died to Pandemonium");
        KnockKnockOST.stop();
        swayTimer.stop();


        mainFrame.setCursor(Cursor.getDefaultCursor());



        DeathScreen deathScreen = new DeathScreen(imagePath);
        deathScreen.setVisible(true); // Show the DeathScreen JFrame
        deathScreen.startAnimation(); // Start the animation


        this.mainFrame.setVisible(false);
    }

    private String generateBar() {
        return "_".repeat(76);
    }

    private void addTopText() throws IOException, FontFormatException {
        Font gameFont = Font.createFont(Font.TRUETYPE_FONT, new FileInputStream(fontPath + "\\zekton.ttf"));
        gameFont = gameFont.deriveFont(Font.BOLD, 38f); // Set size and style

        JLabel staticText = new JLabel("keep the key in the hole", SwingConstants.CENTER);
        staticText.setFont(gameFont);
        staticText.setForeground(Color.WHITE);
        staticText.setBounds((mainFrame.getWidth() - 500) / 2, anchorY, 500, 38); // Set bounds
        staticText.setVisible(true);
        staticText.setOpaque(false);

        mainFrame.add(staticText);
    }


    private void addBar() {
        JLabel bar = new JLabel(generateBar(), SwingConstants.CENTER);
        bar.setSize(mainFrame.getWidth(), 50);
        bar.setForeground(Color.white);
        bar.setVisible(true);
        bar.setLocation((mainFrame.getWidth() - bar.getWidth()) / 2, anchorY + 6);

        mainFrame.add(bar);
    }

    private void addCenter() {
        JLabel centerIMG = new JLabel();
        ImageIcon doorLock = new ImageIcon(imagePath + "\\door_lock.png");
        centerIMG.setIcon(doorLock);
        centerIMG.setSize(doorLock.getIconWidth(), doorLock.getIconHeight());
        centerIMG.setLocation(mainFrame.getWidth() / 2 - centerIMG.getWidth() / 2, anchorY + 122);
        centerIMG.setVisible(true);

        JLabel center = new JLabel();
        center.setSize(75, 75);
        center.setLocation((mainFrame.getWidth() - center.getWidth()) / 2, anchorY + 200);
        center.setVisible(true);

        center.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                playerInCircle = true;
            }

            public void mouseExited(MouseEvent e) {
                playerInCircle = false;
            }
        });

        mainFrame.add(center);
        mainFrame.add(centerIMG);
    }

    private void addHealthBar() {
        healthBar = new JPanel();
        healthBar.setSize(healthBarWidth, 8);
        healthBar.setBackground(Color.red); // Start as red
        healthBar.setLocation((mainFrame.getWidth() - healthBarWidth) / 2, anchorY + 75);
        mainFrame.add(healthBar);

        // Start a timer to update the health bar's state based on playerInCircle
        Timer healthBarTimer = new Timer(100, e -> {
            try {
                updateHealthBar();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        });
        healthBarTimer.start();
    }

    private void updateHealthBar() throws InterruptedException {
        if (!healthBarActive || playerDied) {
            // If the music has stopped, do not update the health bar
            return;
        }

        // Exponential increase when inside the circle
        if (playerInCircle) {
            int maxHealthBarWidth = 200;
            if (healthBarWidth < maxHealthBarWidth) {
                healthBarWidth *= healthConstants[0]; // Exponential increase
                healthBarWidth = Math.min(maxHealthBarWidth, healthBarWidth); // Cap at max width
                healthBar.setBackground(Color.white); // Change color to white
            }
        } else {
            // Faster linear decrease when outside the circle
            int minHealthBarWidth = 0;
            if (healthBarWidth - healthConstants[1] > minHealthBarWidth) {
                healthBarWidth -= healthConstants[1]; // Decrease width faster
                // Cap at min width
                healthBar.setBackground(Color.red); // Change color to red
            }
            if (healthBarWidth - healthConstants[1] <= minHealthBarWidth) {
                playerDied();
                return;
            }
        }

        // Re-center the health bar as its size changes
        healthBar.setSize(healthBarWidth, 8);
        healthBar.setLocation((mainFrame.getWidth() - healthBarWidth) / 2, anchorY + 75);
        mainFrame.repaint();
    }

    private void cursorThrowSequence() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        double init_frequency = 0.15;

        System.out.println("[CLIENT]: Playing Knock Knock OST");
        KnockKnockOST.play();

        Thread.sleep(6250);
        if (!playerDied) {
            System.out.println("[CLIENT]: Starting cursor throw sequence");
            throwCursor();

            while (KnockKnockOST.isPlaying()) {
                if (Math.random() <= init_frequency) {
                    throwCursor();
                    init_frequency += 0.01;
                    Thread.sleep(100);
                }
                Thread.sleep(500);
            }

            // Music has stopped; stop the sway timer and change the health bar color
            swayTimer.stop(); // Stop the sway timer
            healthBarActive = false; // Stop health bar updates
            healthBar.setBackground(Color.white); // Change the health bar color to white

            // Fade out components
            fadeOutComponents();

            // Revert cursor to default
            mainFrame.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void throwCursor() throws InterruptedException, UnsupportedAudioFileException, LineUnavailableException, IOException {
        new AudioPlayer("C:\\Users\\alexa\\IdeaProjects\\JavaTestField\\assets\\audio\\throwSFX.wav").play();

        int targetX = (int) (Math.random() * 1620) + 300;
        int targetY = (int) (Math.random() * 780) + 300;
        Point currentPos = MouseInfo.getPointerInfo().getLocation();

        // Calculate the difference between the current and target positions
        int deltaX = targetX - currentPos.x;
        int deltaY = targetY - currentPos.y;

        // Break down the movement into smaller steps for a smoother transition
        int steps = 20;  // Number of steps to divide the movement into
        for (int i = 0; i < steps; i++) {
            int newX = currentPos.x + (deltaX * i) / steps;
            int newY = currentPos.y + (deltaY * i) / steps;
            robot.mouseMove(newX, newY);
            Thread.sleep(5);  // Small delay between each step
        }
    }

    private Cursor createCustomCursor() {
        // Specify the path to the image
        String imgPath = imagePath + "\\door_key_cursor.png"; // Replace with your actual image path
        BufferedImage cursorImg;

        try {
            // Load the image from the specified path
            cursorImg = ImageIO.read(new File(imgPath));
        } catch (IOException e) {
            e.printStackTrace();
            return Cursor.getDefaultCursor(); // Fallback to default cursor if loading fails
        }

        // Create the cursor using the loaded image
        return Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "custom cursor");
    }

    private void startCursorSway() {
        final int amplitude = 3; // Max distance from the center
        final double frequency = 0.005; // Frequency of the sway
        AtomicLong lastUpdateTime = new AtomicLong(System.currentTimeMillis()); // Store the last update time
        Random random = new Random(); // Random generator

        swayTimer = new Timer(16, e -> { // About 60 FPS
            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - lastUpdateTime.get()) / 1000.0; // Time in seconds
            lastUpdateTime.set(currentTime);

            // Calculate sway offsets with randomness
            int swayX = (int) (Math.sin(currentTime * frequency) * amplitude + random.nextInt(3) - 1); // Add slight random offset
            int swayY = (int) (Math.cos(currentTime * frequency) * amplitude + random.nextInt(3) - 1); // Add slight random offset

            // Get the current mouse position
            Point currentMousePos = MouseInfo.getPointerInfo().getLocation();

            // Move the mouse cursor slightly
            robot.mouseMove(currentMousePos.x + swayX, currentMousePos.y + swayY);
        });

        swayTimer.start();
    }

    private void fadeOutComponents() {
        Timer fadeOutTimer = new Timer(30, null); // Timer for fade out effect
        final double[] opacity = {1.0}; // Start with full opacity

        fadeOutTimer.addActionListener(e -> {
            opacity[0] -= 0.05f; // Decrease opacity
            if (opacity[0] <= 0) {
                fadeOutTimer.stop(); // Stop the timer when fully transparent
                mainFrame.setVisible(false); // Hide components after fading
            } else {
                // Ensure opacity is clamped between 0 and 1
                int alpha = Math.max(0, Math.min(255, (int) (opacity[0] * 255)));

                for (Component comp : mainFrame.getComponents()) {
                    // Get original color
                    Color originalColor = comp.getForeground();
                    // Create new color with updated alpha
                    comp.setForeground(new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), alpha));

                    if (comp instanceof JPanel) {
                        Color bgColor = ((JPanel) comp).getBackground(); // Get original background color
                        // Set new background color
                        ((JPanel) comp).setBackground(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), alpha));
                    }
                }
            }
            mainFrame.repaint(); // Repaint the frame to update changes
        });

        fadeOutTimer.start(); // Start the fade out effect
    }
}
