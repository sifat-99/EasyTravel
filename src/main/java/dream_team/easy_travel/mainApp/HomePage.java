package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import dream_team.easy_travel.swing.RoundedBorder;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class HomePage extends JPanel {


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
        overlayPanel.setOpaque(false); // Ensure transparency
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
        JButton exploreButton = new Button();
        exploreButton.setOpaque(false); // Ensure button is opaque
        exploreButton.setFocusPainted(false);
        exploreButton.setText("Explore");
        exploreButton.setBounds(100, 410, 150, 50);
        overlayPanel.add(exploreButton);

        // Action listener for button
        exploreButton.addActionListener(e -> app.showPanel("Blog"));

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/chat.png")));
        Image img = icon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton chatbotButton = new Button();
        chatbotButton.setIcon(icon);
        chatbotButton.setBackground(Color.black);
//        chatbotButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4, true)); // Rounded border
        chatbotButton.setBounds(1090, 650, 100, 100);
        chatbotButton.setFont(new Font("Arial", Font.PLAIN, 50));
        chatbotButton.setFocusPainted(false);
        overlayPanel.add(chatbotButton);

        // Chatbot popup
        chatbotButton.addActionListener(e -> showChatbot(app));
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

    // Chatbot logic
    private void showChatbot(Easy_Travel app) {
        JFrame chatbotFrame = new JFrame();
        chatbotFrame.setSize(400, 600);
        chatbotFrame.setLocationRelativeTo(null);
        chatbotFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Main container with vertical layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(59, 152, 110)); // Messenger-like blue
        headerPanel.setPreferredSize(new Dimension(400, 50));

        JLabel titleLabel = new JLabel("Chatbot", JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Chat area with background image
        JPanel chatArea = new JPanel() {
            private final Image backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/WPBG.png"))).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setOpaque(false); // Ensure transparency

// JScrollPane wraps the chat area
        JScrollPane scrollPane = new JScrollPane(chatArea) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                chatArea.repaint(); // Force chat area to repaint background image
            }
        };
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getViewport().setOpaque(false); // Transparent viewport
        scrollPane.setOpaque(false); // Transparent JScrollPane

        // Input panel
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(Color.WHITE);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        inputPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

        JTextField inputField = new JTextField();
        inputField.setFont(new Font("Arial", Font.PLAIN, 14));
        inputField.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/send.png")));
        Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        JButton sendButton = new Button();
        sendButton.setIcon(icon);
        sendButton.setBackground(new Color(59, 89, 152));
        sendButton.setPreferredSize(new Dimension(50, 50));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setFocusPainted(false);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(inputPanel, BorderLayout.SOUTH);

        chatbotFrame.add(mainPanel);
        chatbotFrame.setVisible(true);

        // Chatbot responses
        Map<String, String> responses = fetchResponsesFromDatabase();

        // Create JPopupMenu for suggestions
        JPopupMenu suggestionsMenu = new JPopupMenu();

// Add DocumentListener to the input field
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showSuggestions();
            }

            private void showSuggestions() {
                String text = inputField.getText().trim().toLowerCase();

                // Filter responses for suggestions based on input string and limit to 10
                java.util.List<String> filteredSuggestions = responses.keySet().stream()
                        .filter(s -> s.toLowerCase().startsWith(text)) // Match suggestions that start with the input text
                        .limit(10)
                        .toList();

                // Clear old suggestions and repopulate
                suggestionsMenu.removeAll();

                if (!filteredSuggestions.isEmpty()) {
                    for (String suggestion : filteredSuggestions) {
                        JMenuItem item = new JMenuItem(suggestion);
                        item.setFont(new Font("Arial", Font.PLAIN, 14));
                        item.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding for a smooth look
                        item.addActionListener(e -> {
                            inputField.setText(suggestion); // Set the selected suggestion in the input field
                            suggestionsMenu.setVisible(false); // Hide the suggestions menu
                            inputField.requestFocusInWindow(); // Ensure the input field regains focus
                            suggestionsMenu.removeAll(); // Clear suggestions after selection
                        });
                        suggestionsMenu.add(item);
                    }
                } else {
                    JMenuItem noResults = new JMenuItem("No suggestions");
                    noResults.setEnabled(false);
                    noResults.setFont(new Font("Arial", Font.ITALIC, 14));
                    noResults.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                    suggestionsMenu.add(noResults);
                }

                // Calculate the position above the input field
                int menuHeight = suggestionsMenu.getPreferredSize().height; // Height of the suggestions menu
                int yPosition = -menuHeight; // Position the menu above the input field

                // Show the suggestions menu
                suggestionsMenu.pack();
                suggestionsMenu.show(inputField, 0, yPosition);
                suggestionsMenu.setVisible(true);
            }
        });

// Ensure the input field stays typeable while suggestions are visible
        suggestionsMenu.setFocusable(false);

// Add FocusListener to keep suggestions visible
        inputField.addFocusListener(new FocusAdapter() {
            @Override
                public void focusLost(FocusEvent e) {
    // Only hide the menu if focus is lost to something other than the menu
                if (e.getOppositeComponent() != null && !suggestionsMenu.isVisible() && !SwingUtilities.isDescendingFrom(e.getOppositeComponent(), suggestionsMenu)) {
                suggestionsMenu.setVisible(false);
    }
}
        });

        // Make suggestionsMenu dismissible with mouse clicks outside
        Toolkit.getDefaultToolkit().addAWTEventListener(event -> {
            if (event instanceof MouseEvent mouseEvent) {
                if (mouseEvent.getID() == MouseEvent.MOUSE_PRESSED) {
                    if (!SwingUtilities.isDescendingFrom(mouseEvent.getComponent(), suggestionsMenu)) {
                        suggestionsMenu.setVisible(false);
                    }
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);


        @FunctionalInterface
        interface MessageAdder {
            void addMessage(String sender, String message, boolean isUser);
        }



            // Add new chat bubbles
            MessageAdder addMessage = (String sender, String message, boolean isUser) -> {
            JPanel bubble = new JPanel();
            bubble.setBackground(isUser ? new Color(220, 248, 198) : new Color(240, 240, 240));
            bubble.setLayout(new BorderLayout());
            bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            bubble.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
            bubble.setBorder(new RoundedBorder(10));
            JLabel messageLabel = new JLabel("<html><body style='width: 200px;'><b>" + sender + ":</b> " + message + "</body></html>");
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            messageLabel.setForeground(Color.BLACK);
            bubble.add(messageLabel, BorderLayout.CENTER);

            JLabel timestampLabel = new JLabel(new SimpleDateFormat("hh:mm a").format(new Date()));
            timestampLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            timestampLabel.setForeground(Color.GRAY);
            bubble.add(timestampLabel, BorderLayout.SOUTH);

            JPanel alignmentWrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
            alignmentWrapper.setBackground(new Color(0, 0, 0, 0)); // Fully transparent background
            alignmentWrapper.add(bubble);

            chatArea.add(alignmentWrapper);
            chatArea.revalidate();
            chatArea.repaint(); // Ensure the background is redrawn

            // Scroll to the bottom of the chat area
            SwingUtilities.invokeLater(() -> {
                JScrollBar vertical = scrollPane.getVerticalScrollBar();
                vertical.setValue(vertical.getMaximum());
            });

        };


        // Action logic for sending messages
        Runnable sendMessage = () -> {
            String message = inputField.getText().trim();

           String loggedInUser = (app.getLoggedInUser() != null) ? app.getLoggedInUser().getName() : "Guest";

            if (!message.isEmpty()) {
                addMessage.addMessage(loggedInUser, message, true); // User message
                inputField.setText("");

                // Generate a response
                String response = responses.getOrDefault(message.toLowerCase(), responses.get("default"));
                addMessage.addMessage("Bot", response, false); // Bot response
            }
        };


        // Add action listener for the button
        sendButton.addActionListener(e -> {
            sendMessage.run();
            suggestionsMenu.setVisible(false);
            suggestionsMenu.removeAll();
        });

        // Add key binding for Enter key
        inputField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "send");
        inputField.getActionMap().put("send", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                sendMessage.run();
                // Hide and clear the suggestions menu
                suggestionsMenu.setVisible(false);
                suggestionsMenu.removeAll();
            }
        });
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
    @org.jetbrains.annotations.NotNull
    private Map<String, String> fetchResponsesFromDatabase() {
        Map<String, String> responses = new HashMap<>();
        String query = "SELECT keyword, response FROM chatbot_responses";

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                responses.put(rs.getString("keyword"), rs.getString("response"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responses;
    }
}
