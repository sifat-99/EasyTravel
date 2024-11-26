package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
        chatbotButton.addActionListener(e -> showChatbot());
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
    private void showChatbot() {
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

        // Chat area
        JPanel chatArea = new JPanel();
        chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
        chatArea.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

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
        Map<String, String> responses = new HashMap<>();
        responses.put("hello", "Hi there! Looking for some travel inspiration?");
        responses.put("hi", "Hello! How can I assist you with your travel plans today?");
        responses.put("hii", "Hello! How can I assist you with your travel plans today?");
        responses.put("hey", "Hey! Ready to explore some amazing destinations?");
        responses.put("good morning", "Good morning! Let's plan your next adventure.");
        responses.put("good afternoon", "Good afternoon! Where are you dreaming of traveling to?");
        responses.put("good evening", "Good evening! Let's find your perfect vacation spot.");
        responses.put("good night", "Good night! Dream of your next travel destination.");
        responses.put("thanks", "You're welcome! Let me know if you have more questions.");
        responses.put("help", "I'm here to help you with travel recommendations, tips, and more!");
        responses.put("destination", "Where are you thinking of traveling to? I can help with recommendations.");
        responses.put("travel", "Traveling is a great way to explore new cultures and create lasting memories.");
        responses.put("vacation", "Everyone deserves a break! Where are you dreaming of going?");
        responses.put("dream", "Dream big! Whether it's a beach getaway or a mountain adventure, I can help.");
        responses.put("plan", "Planning ahead is key to a successful trip. Let's start with your dream destination!");
        responses.put("explore", "Exploring new places opens your mind and enriches your life.");
        responses.put("how are you", "I'm just a chatbot, but I'm here to help make your travel dreams come true!");
        responses.put("recommendation", "I can recommend beautiful beaches, adventurous mountains, and cultural experiences. What are you in the mood for?");
        responses.put("beach", "Looking for a beach getaway? Maldives, Bora Bora, or the Amalfi Coast are top picks!");
        responses.put("mountains", "The Swiss Alps, Rockies, or the Himalayas offer breathtaking mountain adventures.");
        responses.put("adventure", "Consider skydiving in Dubai, white-water rafting in Costa Rica, or a safari in Kenya.");
        responses.put("relax", "Try a wellness retreat in Bali or a spa experience in Iceland’s Blue Lagoon.");
        responses.put("food", "Explore street food in Thailand, pasta in Italy, or tacos in Mexico.");
        responses.put("luxury", "Experience luxury at the Ritz in Paris or an overwater villa in Bora Bora.");
        responses.put("wildlife", "Discover the Great Migration in Tanzania or penguins in Antarctica.");
        responses.put("cultural", "Explore the pyramids of Egypt, the temples of Kyoto, or the castles of Scotland.");
        responses.put("family", "Disneyland in California or Florida is great for families, as are safaris in South Africa.");
        responses.put("romantic", "Paris, Santorini, or Venice are perfect for romantic getaways.");
        responses.put("solo", "Consider backpacking in Europe or trekking in Nepal.");
        responses.put("budget", "Find affordable trips in Southeast Asia or Eastern Europe.");
        responses.put("road trip", "Drive along Route 66 in the USA or the Great Ocean Road in Australia.");
        responses.put("history", "Explore ancient Rome, Machu Picchu, or the Great Wall of China.");
        responses.put("city", "New York, Tokyo, and London offer vibrant city experiences.");
        responses.put("nature", "Visit Yellowstone National Park or the fjords in Norway.");
        responses.put("desert", "Experience the vastness of the Sahara or the beauty of the Atacama Desert.");
        responses.put("island", "The Galápagos Islands and Seychelles are perfect for nature lovers.");
        responses.put("train", "The Trans-Siberian Railway or the Glacier Express offer scenic journeys.");
        responses.put("cruise", "Cruise through the Caribbean or along the Mediterranean.");
        responses.put("hiking", "Trek to Everest Base Camp or walk the Inca Trail to Machu Picchu.");
        responses.put("spa", "Relax in a thermal spa in Hungary or a luxury retreat in Bali.");
        responses.put("shopping", "Explore markets in Marrakech or high-end boutiques in Paris.");
//        responses.put("festivals", "Attend Oktoberfest in Germany or Carnival in Rio.");
        responses.put("snow", "Ski in the French Alps or enjoy the Northern Lights in Lapland.");
        responses.put("visa", "Looking for visa-free destinations? Maldives, Thailand, and Fiji are great options!");
        responses.put("safety", "Japan, Iceland, and Switzerland are known for their safety.");
        responses.put("honeymoon", "Maldives, Bora Bora, and Greece are popular honeymoon destinations.");
        responses.put("weather", "Planning for sunshine? Try Bali or Hawaii during their dry seasons.");
        responses.put("hello there", "Hello! How can I help you plan your next adventure?");
        responses.put("cheap flights", "Consider using tools like Skyscanner or Google Flights for deals.");
        responses.put("hotels", "Booking.com or Airbnb are great for finding accommodations.");
        responses.put("best time to travel", "It depends! For Europe, summer is great. For Southeast Asia, aim for the dry season.");
        responses.put("thank you", "You're welcome! Let me know if you have more questions.");
        responses.put("bye", "Goodbye! Have a great day and happy travels!");
        responses.put("travel insurance", "Travel insurance is essential for peace of mind. Check out World Nomads or Allianz.");
        responses.put("staycation", "Explore hidden gems in your local area or nearby national parks.");
        responses.put("eco-friendly", "Visit eco-resorts like Soneva Fushi in the Maldives or Costa Rica's lodges.");
        responses.put("flight delay", "Check with your airline for updates and enjoy airport lounges if available.");
        responses.put("currency exchange", "Exchange money at local banks or use international debit/credit cards for convenience.");
        responses.put("trending", "Bali, Iceland, and Portugal are currently trending among travelers.");
        responses.put("covid travel", "Stay updated with travel restrictions and test requirements for your destination.");
        responses.put("beaches in asia", "Check out the beaches in Bali, Phuket, and the Maldives.");
        responses.put("europe", "Explore Paris, Barcelona, Rome, or the stunning landscapes of Switzerland.");
        responses.put("unique", "Visit Cappadocia in Turkey or the glowworm caves in New Zealand.");
        responses.put("art", "Visit the Louvre in Paris or the Uffizi Gallery in Florence.");
        responses.put("kids", "Theme parks like Legoland or nature reserves are great for kids.");
        responses.put("fitness", "Try yoga retreats in India or hiking trails in Patagonia.");
        responses.put("local cuisine", "Taste paella in Spain, ramen in Japan, or gumbo in New Orleans.");
        responses.put("festivals", "Diwali in India or the Lantern Festival in Thailand are amazing cultural experiences.");
        responses.put("emergency", "For emergencies, ensure you have your country's embassy contact details handy.");
        responses.put("sustainable travel", "Consider slow travel options like trains or staying in eco-certified accommodations.");
        responses.put("photography", "Capture stunning shots in Iceland, Namibia, or the Canadian Rockies.");
        responses.put("recommand me a place", "Tanguar Haor, A beautiful Haor in Bangladesh");

        responses.put("default", "Our customer center will contact you as soon as possible.");

        @FunctionalInterface
        interface MessageAdder {
            void addMessage(String sender, String message, boolean isUser);
        }

        // Method to add chat bubbles
        MessageAdder addMessage = (String sender, String message, boolean isUser) -> {
            JPanel bubble = new JPanel();
            bubble.setBackground(isUser ? new Color(220, 248, 198) : new Color(240, 240, 240)); // Different colors for user/bot
            bubble.setLayout(new BorderLayout());
            bubble.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            bubble.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

            JLabel messageLabel = new JLabel("<html><body style='width: 200px;'>" + message + "</body></html>");
            messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            messageLabel.setForeground(Color.BLACK);
            bubble.add(messageLabel, BorderLayout.CENTER);

            JLabel timestampLabel = new JLabel(new SimpleDateFormat("hh:mm a").format(new Date()));
            timestampLabel.setFont(new Font("Arial", Font.ITALIC, 10));
            timestampLabel.setForeground(Color.GRAY);
            bubble.add(timestampLabel, BorderLayout.SOUTH);

            JPanel alignmentWrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT));
            alignmentWrapper.setBackground(Color.WHITE);
            alignmentWrapper.add(bubble);

            chatArea.add(alignmentWrapper);
            chatArea.revalidate();
            chatArea.repaint();
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
        };

        // Action logic for sending messages
        Runnable sendMessage = () -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                addMessage.addMessage("You", message, true); // User message
                inputField.setText("");

                // Generate a response
                String response = responses.getOrDefault(message.toLowerCase(), responses.get("default"));
                addMessage.addMessage("Bot", response, false); // Bot response
            }
        };


        // Add action listener for the button
        sendButton.addActionListener(e -> sendMessage.run());

        // Add key binding for Enter key
        inputField.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "send");
        inputField.getActionMap().put("send", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage.run();
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
}
