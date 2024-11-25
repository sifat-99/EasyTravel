package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.Easy_Travel;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Objects;

public class HomePage extends JPanel {
//Changed
    public HomePage(Easy_Travel app) {
        setLayout(null); // Use null layout to allow precise positioning
        setBackground(Color.BLACK); // Fallback color in case video fails to load

        // Initialize JavaFX for video background
        JFXPanel fxPanel = new JFXPanel();
        fxPanel.setBounds(0, -100, 1400, 850);
        add(fxPanel);

        // Load and play the video as background
        SwingUtilities.invokeLater(() -> initFX(fxPanel));

        // Set up custom font, with fallback to Arial if needed
        Font lobsterFont = loadFont();
        if (lobsterFont == null) {
            lobsterFont = new Font("Arial", Font.BOLD, 60);
        }

        // Add transparent overlay for contrast
        JPanel overlayPanel = new JPanel();
        overlayPanel.setBounds(1, 1, 1440, 750);
        overlayPanel.setBackground(new Color(255, 255, 255, 0)); // Semi-transparent overlay
        overlayPanel.setLayout(null);
        fxPanel.add(overlayPanel);

        // Welcome text label
        JLabel textLabel = new JLabel("Welcome", SwingConstants.LEFT);
        textLabel.setForeground(Color.BLACK);
        textLabel.setFont(lobsterFont.deriveFont(60f));
        textLabel.setBounds(50, 200, 500, 60);
        overlayPanel.add(textLabel);

        // Add descriptive text labels
        String[] lines = {
                "We help you to find wonderful trips and great vacation",
                "Places and we will provide famous and popular tourist",
                "Places all over the world"
        };

        int yOffset = 300;
        for (String line : lines) {
            JLabel textLabel2 = new JLabel(line, SwingConstants.LEFT);
            textLabel2.setForeground(Color.BLACK);
            textLabel2.setFont(new Font("SansSerif", Font.BOLD, 20));
            textLabel2.setBounds(50, yOffset, 700, 30);
            overlayPanel.add(textLabel2);
            yOffset += 30;
        }

        // Explore button setup
        CustomButton exploreButton = new CustomButton("Explore", 30);
        exploreButton.setBounds(100, 410, 150, 50);
        overlayPanel.add(exploreButton);

        // Action listener for button
        exploreButton.addActionListener(e -> app.showPanel("Blog"));
    }

    // Initialize JavaFX video background
    private void initFX(JFXPanel fxPanel) {
        String videoPath = Objects.requireNonNull(getClass().getResource("/background.mp4")).toExternalForm();
        Media media = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop the video

        MediaView mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(1400);
        mediaView.setFitHeight(850);

        Group root = new Group(mediaView);
        Scene scene = new Scene(root, 1400, 850);
        fxPanel.setScene(scene);
        mediaPlayer.play();
    }

    // Load custom font for labels
    public Font loadFont() {
        try (InputStream is = getClass().getResourceAsStream("/Lobster-Regular.ttf")) {
            assert is != null;
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            return font;
        } catch (Exception e) {
            System.err.println("Failed to load font: " + "/Lobster-Regular.ttf");
            return null;
        }
    }
}
