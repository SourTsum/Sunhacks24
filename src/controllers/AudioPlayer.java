package controllers;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioPlayer {
    private final Clip audioClip;
    private boolean isPlaying;  // Track whether audio is currently playing

    AudioPlayer(File audioFile) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        this.audioClip = AudioSystem.getClip();
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
        this.audioClip.open(audioStream);
        this.isPlaying = false; // Initialize playback state

        // Listener to update playback status
        audioClip.addLineListener(event -> {
            if (event.getType() == LineEvent.Type.START) {
                isPlaying = true;
            } else if (event.getType() == LineEvent.Type.STOP) {
                isPlaying = false;
                audioClip.setFramePosition(0);  // Reset the clip for future use
            }
        });
    }

    public AudioPlayer(String filePath) throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        this(new File(filePath)); // Reuse constructor
    }

    public void play() {
        if (audioClip.isRunning()) {
            audioClip.stop();  // Stop if currently playing
        }

        audioClip.setFramePosition(0);  // Rewind to the beginning
        audioClip.start();  // Start playback
    }

    public boolean isPlaying() {
        return isPlaying;  // Return whether the audio is currently playing
    }

    public void stop() {
        if (audioClip.isRunning()) {
            audioClip.stop();  // Stop playback
        }
    }

    // Optional: Call this to free system resources after usage
    public void close() {
        audioClip.close();
    }
}
