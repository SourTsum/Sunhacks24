import controllers.*;
import events.Pandemonium;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    private static final String runningPath = System.getProperty("user.dir");
    private static final Path assetsPath = Paths.get(runningPath, "assets");
    private static final Path fontPATH = Paths.get(assetsPath.toString(), "font");
    private static final Path audioPATH = Paths.get(assetsPath.toString(), "audio");
    private static final Path dialougePATH = Paths.get(assetsPath.toString(), "dialouge");
    private static final Path imagesPath = Paths.get(assetsPath.toString(), "images");


    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException, AWTException, FontFormatException, InterruptedException {
        startGame();
    }


    private static void startGame() throws UnsupportedAudioFileException, LineUnavailableException, IOException, InterruptedException, AWTException, FontFormatException {
        JFrame mainFrame = new JFrame();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        mainFrame.setSize(width, height);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(null);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        mainFrame.getContentPane().setBackground(Color.BLACK);


        MainMenu mainMenu = new MainMenu(
                STR."\{imagesPath}\\main_menu.png",
                STR."\{imagesPath}\\play_button.png",
                STR."\{imagesPath}\\options_button.png",
                STR."\{imagesPath}\\help_button.png",
                STR."\{audioPATH}\\title_screen.wav"
        );

        SettingsMenu settingsMenu = new SettingsMenu(
                STR."\{imagesPath}\\main_menu.png",
                STR."\{imagesPath}\\cc_on_button.png",
                STR."\{imagesPath}\\cc_off_button.png",
                STR."\{imagesPath}\\return_button.png"
        );

        PlayTransition playTransition;

        mainFrame.add(mainMenu);
        mainFrame.add(settingsMenu);
        settingsMenu.setVisible(false);
        mainFrame.repaint();


        while (true) {
            // Handle main menu button states
            if (mainMenu.buttonStates[0]) {
                // Play button logic
                mainMenu.setVisible(false);
                mainFrame.repaint();

                playTransition = new PlayTransition(dialougePATH.toString(), imagesPath.toString());
                mainFrame.add(playTransition);
                break;
            }

            if (mainMenu.buttonStates[1]) {
                // Open settings menu
                mainMenu.setVisible(false);
                updateSettingsMenu(mainFrame, mainMenu, settingsMenu);
            }


            if (settingsMenu.buttonStates[1]) {
                settingsMenu.setVisible(false);
                mainMenu.setVisible(true);
                mainFrame.repaint();

                settingsMenu.buttonStates[1] = false;
            }

            mainFrame.repaint();
        }

        while (playTransition.intro.timer.isRunning()){}
        mainFrame.remove(playTransition);
        mainFrame.repaint();

        Player player = new Player(STR."\{imagesPath}\\chase_player.png");
        MainGame mainGame = new MainGame(imagesPath.toString(),audioPATH.toString());
        mainFrame.add(player);


        mainFrame.repaint();
        player.repaint();
        player.setLocation(150,400);

        player.requestFocusInWindow();


        JFrame miniGame = new JFrame();
        miniGame.setLayout(null);


        miniGame.setBounds(0,0,width,height);
        miniGame.setLayout(null);
        miniGame.setBackground(Color.darkGray);

        mainFrame.add(mainGame);


        JLabel GreenBox = new JLabel();
        GreenBox.setOpaque(true);
        GreenBox.setBackground(Color.GREEN);
        GreenBox.setSize(500,250);
        GreenBox.setLocation(625,200);
        GreenBox.setVisible(true);


        final boolean[] FUCKYOU = {false};
        GreenBox.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(player.room == 2 || player.room == 3){
                    FUCKYOU[0] = true;
                }
            }
        });

        mainFrame.add(GreenBox);

        while (true) {
            if(FUCKYOU[0]){
                startMiniGame(miniGame);
                FUCKYOU[0] = false;
            }
            if (player.roomChanged) {
                System.out.println("Room changed to: " + player.room);
                mainGame.updateRoom(player.room);
                player.roomChanged = false;

                if (player.room == 2) {
                    System.out.println("Door box visible");
                } else {
                    System.out.println("Door box hidden");
                }
                mainFrame.revalidate();
            }
            mainFrame.repaint();

            if (!mainGame.backgroundMusic.isPlaying()) {
                mainGame.backgroundMusic.play();
            }
        }

    }

    public static void updateSettingsMenu(Frame mainFrame, MainMenu mainMenu, SettingsMenu settingsMenu){
        mainFrame.add(settingsMenu);
        settingsMenu.setVisible(true);
        settingsMenu.revalidate();
        settingsMenu.repaint();
        mainFrame.repaint();
    }

    public static void wipeFrame(JFrame j){
        j.removeAll();
    }

    public static void addToFrame(JFrame j, JPanel p, JLabel label){
        p.add(label);
        j.add(p);
    }

    public static void startMiniGame(JFrame miniGame) throws UnsupportedAudioFileException, LineUnavailableException, IOException, AWTException, FontFormatException, InterruptedException {
        miniGame.setVisible(true);
        Pandemonium test = new Pandemonium(miniGame,imagesPath.toString(),audioPATH.toString(),fontPATH.toString());
        test.start();
    }

}