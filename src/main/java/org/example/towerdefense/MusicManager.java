package org.example.towerdefense;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.util.ArrayList;
import java.util.List;

public class MusicManager {

    private static MediaPlayer currentPlayer;
    private static List<String> currentTracks;
    private static int currentIndex = 0;

    private static String currentContext = "";
    private static boolean musicEnabled = true;

    public static void playQueue(List<String> tracks, String context) {
        if (tracks == null || tracks.isEmpty()) {
            return;
        }
        if (context.equals(currentContext)) {
            if (musicEnabled && currentPlayer == null) {
                playNext();
            }
            return;
        }

        currentContext = context;
        currentTracks = new ArrayList<>(tracks);
        currentIndex = 0;
        stop();
        if (musicEnabled) {
            playNext();
        }
    }

    public static boolean isPlaying() {
        return currentPlayer != null &&
                currentPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static boolean isMusicEnabled() {
        return musicEnabled;
    }

    public static void setMusicEnabled(boolean enabled) {
        if (musicEnabled == enabled) {
            return;
        }
        musicEnabled = enabled;
        if (currentPlayer == null) {
            if (enabled && currentTracks != null && !currentTracks.isEmpty()) {
                playNext();
            }
        } else if (enabled) {
            if (currentPlayer.getStatus() == MediaPlayer.Status.PAUSED) {
                currentPlayer.play();
            } else if (currentPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                playNext();
            }
        } else {
            currentPlayer.pause();
        }
    }

    private static void playNext() {
        if (currentTracks == null || currentTracks.isEmpty()) return;
        stop();
        String track = currentTracks.get(currentIndex);
        String url = MusicManager.class.getResource(track).toExternalForm();
        Media media = new Media(url);
        currentPlayer = new MediaPlayer(media);
        currentPlayer.setOnEndOfMedia(() -> {
            currentIndex = (currentIndex + 1) % currentTracks.size();
            playNext();
        });
        currentPlayer.play();
    }

    public static void play(String resourcePath) {
        stop();
        String url = MusicManager.class.getResource(resourcePath).toExternalForm();
        Media media = new Media(url);
        currentPlayer = new MediaPlayer(media);
        currentPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        currentPlayer.play();
    }

    public static void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer = null;
        }
    }

    public static void setVolume(double volume) {
        if (currentPlayer != null) {
            currentPlayer.setVolume(volume);
        }
    }

    public static void skipTrack() {
        if (currentTracks == null || currentTracks.isEmpty()) return;
        currentIndex = (currentIndex + 1) % currentTracks.size();
        if (musicEnabled) {
            playNext();
        }
    }

    public static void resumeContext() {
        setMusicEnabled(true);
    }
}