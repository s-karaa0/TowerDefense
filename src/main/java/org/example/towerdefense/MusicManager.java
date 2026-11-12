package org.example.towerdefense;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.MapChangeListener;
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
    private static final SimpleStringProperty currentTrackTitle =
            new SimpleStringProperty("Keine Musik");

    public static ReadOnlyStringProperty currentTrackTitleProperty() {
        return currentTrackTitle;
    }

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
        Media media = new Media(MusicManager.class.getResource(track).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        currentPlayer = player;
        listenForMetadataTitle(media, player);
        player.setOnEndOfMedia(() -> {
            currentIndex = (currentIndex + 1) % currentTracks.size();
            playNext();
        });
        player.play();
    }

    public static void play(String resourcePath) {
        stop();
        Media media = new Media(MusicManager.class.getResource(resourcePath).toExternalForm());
        MediaPlayer player = new MediaPlayer(media);
        currentPlayer = player;
        listenForMetadataTitle(media, player);
        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.play();
    }

    private static void listenForMetadataTitle(Media media, MediaPlayer player) {
        media.getMetadata().addListener((MapChangeListener<String, Object>) change -> {
            if ("title".equalsIgnoreCase(change.getKey()) && currentPlayer == player) {
                setMetadataTitle(change.getValueAdded());
            }
        });
        setMetadataTitle(media.getMetadata().get("title"));
    }

    private static void setMetadataTitle(Object title) {
        if (title != null && !title.toString().isBlank()) {
            currentTrackTitle.set(title.toString());
        }
    }

    public static void stop() {
        if (currentPlayer != null) {
            currentPlayer.stop();
            currentPlayer = null;
        }
        currentTrackTitle.set("Keine Musik");
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