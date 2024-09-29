package controllers;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Dialogue {
    private final JPanel mainFrame;
    private final LinkedHashMap<String, Integer> dialogueSequence = new LinkedHashMap<>(); // Use LinkedHashMap
    private boolean generated = false;
    public Timer timer;

    public Dialogue(JPanel mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void generateFromFile(File textFile) throws IOException {
        List<String> lines = new ArrayList<>();
        Scanner fileReader = new Scanner(textFile);

        // Read all lines from the file
        while (fileReader.hasNextLine()) {
            lines.add(fileReader.nextLine());
        }

        // Reverse the list to read lines from bottom to top
        Collections.reverse(lines);

        // Process the reversed list
        for (String kvPair : lines) {
            String text = kvPair.substring(0, kvPair.indexOf(" | "));
            kvPair = kvPair.substring(kvPair.indexOf(" | ") + 3);

            int minute = fastINT(kvPair.substring(0, kvPair.indexOf(":")));
            int second = fastINT(kvPair.substring(kvPair.indexOf(":") + 1));

            dialogueSequence.put(text, ((minute * 60) + second) * 1000);
        }
        generated = true;
    }

    public void play() {
        if (generated) {
            String[] keySet = dialogueSequence.keySet().toArray(new String[0]);
            timer = new Timer(0, null); // Create the timer
            final int[] i = {keySet.length - 1}; // Start index at the last element (iterate backwards)

            // Timer for iterating through the dialogueSequence
            timer = new Timer(0, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (i[0] >= 0) {
                        String text = keySet[i[0]]; // Get the current text (going backwards)

                        // Set the delay for the next dialogue based on the current timestamp
                        ((Timer) e.getSource()).setDelay(dialogueSequence.get(text)); // Wait before showing the label

                        // Create and show the label after the wait
                        TypeWriterLabel frameLabel = new TypeWriterLabel(text, false);
                        mainFrame.add(frameLabel);
                        frameLabel.type(); // Start the typing animation
                        mainFrame.revalidate(); // Revalidate to update the UI
                        mainFrame.repaint();    // Repaint to ensure the changes are displayed

                        // Move to the previous text in the sequence (going backwards)
                        i[0]--;
                    } else {
                        ((Timer) e.getSource()).stop(); // Stop the timer when all texts are processed
                    }
                }
            });

            timer.setInitialDelay(0); // Start immediately
            timer.start(); // Start the timer
        } else {
            System.out.println("[DIALOGUE]: Couldn't play your dialogue sequence");
        }
    }




    private int fastINT(String stringINT) {
        int res = 0;
        for (int i = 0; i < stringINT.length(); i++) {
            res = res * 10 + (stringINT.charAt(i) - '0');
        }
        return res;
    }
}
