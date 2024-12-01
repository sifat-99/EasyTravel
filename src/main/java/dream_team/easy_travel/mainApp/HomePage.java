package dream_team.easy_travel.mainApp;

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
        JButton exploreButton = new Button();
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
        Map<String, String> responses = new HashMap<>();
        responses.put("hello", "Hi there! How can I help you with your travel plans?");
        responses.put("hi", "Hello! Ready to explore new destinations?");
        responses.put("hey", "Hey! Where are you planning to go?");
        responses.put("good morning", "Good morning! Let’s plan your next journey.");
        responses.put("good afternoon", "Good afternoon! Need help planning your trip?");
        responses.put("good evening", "Good evening! How can I assist you?");
        responses.put("good night", "Good night! Dream about amazing adventures.");
        responses.put("thanks", "You're welcome! Let me know if you have more questions.");
        responses.put("thank you", "Anytime! Have a great trip.");
        responses.put("help", "I’m here to assist you with destinations, flights, and tips.");
        responses.put("destination", "Tell me what kind of destination you're looking for.");
        responses.put("cheap flights", "Check out Skyscanner, Kayak, or Hopper for deals.");
        responses.put("visa", "Need visa info? Let me know your destination.");
        responses.put("travel insurance", "Consider Allianz or World Nomads for travel insurance.");
        responses.put("weather", "Need weather info for your trip? I can help.");
        responses.put("currency exchange", "Exchange your currency locally or use travel cards.");
        responses.put("romantic", "How about Santorini, Paris, or the Maldives?");
        responses.put("beach", "Looking for beaches? Try Bora Bora, Bali, or Phuket.");
        responses.put("mountains", "Explore the Swiss Alps, Rockies, or Himalayas.");
        responses.put("adventure", "Skydiving, white-water rafting, or safaris sound fun?");
        responses.put("relax", "Try a spa retreat in Iceland or Bali.");
        responses.put("luxury", "Consider an overwater villa in Bora Bora.");
        responses.put("wildlife", "Visit Kruger National Park or the Galápagos Islands.");
        responses.put("family", "Disneyland, Legoland, or wildlife safaris are great!");
        responses.put("budget travel", "Affordable options include Vietnam or Eastern Europe.");
        responses.put("road trip", "Try Route 66 in the USA or the Garden Route in South Africa.");
        responses.put("history", "Explore ancient Rome, Petra, or Angkor Wat.");
        responses.put("nature", "Visit Yellowstone, Patagonia, or the Norwegian fjords.");
        responses.put("cruise", "How about a Mediterranean or Caribbean cruise?");
        responses.put("unique places", "Try Cappadocia in Turkey or Iceland's Blue Lagoon.");
        responses.put("festivals", "Experience Holi in India or Oktoberfest in Germany.");
        responses.put("local cuisine", "Taste sushi in Japan or street food in Thailand.");
        responses.put("trending destinations", "Iceland, Portugal, and Japan are trending now.");
        responses.put("honeymoon", "The Maldives, Hawaii, or Amalfi Coast are perfect.");
        responses.put("solo travel", "Try backpacking through Southeast Asia.");
        responses.put("eco-friendly", "Visit eco-lodges in Costa Rica or national parks.");
        responses.put("emergency", "Always keep embassy contacts and emergency numbers handy.");
        responses.put("explore", "Let’s explore! What type of place excites you?");
        responses.put("spa", "Relax in a thermal spa in Hungary.");
        responses.put("shopping", "Shop in Dubai’s malls or Paris’ boutiques.");
        responses.put("kids", "Theme parks like Disneyland or wildlife safaris are great for kids.");
        responses.put("art", "Visit the Louvre in Paris or the Uffizi in Florence.");
        responses.put("fitness", "Hike in Patagonia or join a yoga retreat in Bali.");
        responses.put("local culture", "Learn flamenco in Spain or join a tea ceremony in Japan.");
        responses.put("photography", "Capture Iceland's landscapes or Namibia's dunes.");
        responses.put("romantic places", "Paris, Venice, or Santorini are amazing for couples.");
        responses.put("hotels", "Try Booking.com or Airbnb for accommodations.");
        responses.put("budget", "Southeast Asia or Eastern Europe offer great value.");
        responses.put("island", "Explore Seychelles, Galápagos, or Fiji.");
        responses.put("snow", "Go skiing in Switzerland or enjoy Lapland's Northern Lights.");
        responses.put("safety", "Japan, Iceland, and New Zealand are very safe.");
        responses.put("adventure sports", "How about ziplining in Costa Rica or bungee jumping in New Zealand?");
        responses.put("visa-free", "Maldives, Indonesia, and Fiji are great for visa-free travel.");
        responses.put("cultural festivals", "Visit the Lantern Festival in Thailand or Carnival in Brazil.");
        responses.put("cruise holidays", "Consider the Caribbean or Alaskan cruises.");
        responses.put("romantic vacations", "Try Santorini, the Maldives, or Venice.");
        responses.put("hiking", "Hike the Inca Trail or trek through the Rockies.");
        responses.put("history buffs", "Visit Machu Picchu or the Great Wall of China.");
        responses.put("train journeys", "Try the Glacier Express or the Trans-Siberian Railway.");
        responses.put("nearby travel", "Explore local parks or weekend getaways nearby.");
        responses.put("luxury travel", "How about a private island in the Maldives?");
        responses.put("travel planning", "Start with a destination! I’ll help with the rest.");
        responses.put("food travel", "Try pizza in Italy or street food in Vietnam.");
        responses.put("city trips", "Explore the buzz of New York, Tokyo, or Paris.");
        responses.put("nature trips", "Visit the Grand Canyon or Iceland’s waterfalls.");
        responses.put("family-friendly", "Theme parks and zoos are great family destinations.");
        responses.put("short trips", "Weekend getaways to nearby cities or parks work well.");
        responses.put("road trips", "Drive through Scotland or along the Amalfi Coast.");
        responses.put("beaches in Asia", "Check out the beaches in Bali, Phuket, and Maldives.");
        responses.put("Asia travel", "Visit Angkor Wat, the Great Wall, or Mount Fuji.");
        responses.put("Europe travel", "Explore Paris, Barcelona, and Swiss villages.");
        responses.put("Africa travel", "Experience safaris in Kenya or Egypt’s pyramids.");
        responses.put("flight booking", "Book early with tools like Google Flights or Kayak.");
        responses.put("train travel", "Enjoy Europe’s railways or scenic rides in Canada.");
        responses.put("camping", "Try the Rockies, Yosemite, or Patagonia for camping.");
        responses.put("spa retreats", "Relax in Bali or the Blue Lagoon in Iceland.");
        responses.put("eco-lodges", "Stay in Costa Rica or eco-lodges in the Amazon.");
        responses.put("luxury resorts", "How about a 5-star resort in Dubai or Maldives?");
        responses.put("top destinations", "How about Bali, Iceland, or Italy?");
        responses.put("weekend trips", "Plan for a city break or a nearby national park.");
        responses.put("romantic getaways", "The Amalfi Coast or Bora Bora might be perfect.");
        responses.put("desert trips", "Visit the Sahara or the Atacama Desert.");
        responses.put("waterfalls", "See Niagara Falls, Iguazu Falls, or Victoria Falls.");
        responses.put("wildlife experiences", "Try a safari in Kenya or see pandas in China.");
        responses.put("family destinations", "Disney parks or wildlife reserves are great for families.");
        responses.put("nearby adventures", "Look into hidden gems close to your area.");
        responses.put("solo trips", "Try peaceful places like New Zealand or Japan.");
        responses.put("cultural trips", "Explore Kyoto’s temples or Morocco’s medinas.");
        responses.put("wellness trips", "Visit yoga retreats in India or wellness spas in Thailand.");
        responses.put("off-season travel", "Travel in the off-season to Europe for better deals.");
        responses.put("cruise travel", "Sail through Alaska or the Greek islands.");
        responses.put("luxury dining", "Try Michelin-star restaurants in Paris or Tokyo.");
        responses.put("hidden gems", "Discover Albania’s beaches or Slovenia’s lakes.");
        responses.put("short city stays", "Explore Prague, Vienna, or Lisbon.");
        responses.put("last-minute deals", "Look for deals on Expedia or Lastminute.com.");
        responses.put("popular spots", "Paris, Bali, and Rome are perennial favorites.");
        responses.put("festivals to visit", "Consider Holi in India or La Tomatina in Spain.");
        responses.put("beaches in europe", "Santorini, Mykonos, and the Amalfi Coast offer incredible beaches.");
        responses.put("cultural in asia", "Visit the Taj Mahal in India, Angkor Wat in Cambodia, or Kyoto in Japan.");
        responses.put("wildlife in africa", "Explore safaris in Kenya, South Africa, or Tanzania.");
        responses.put("adventure in south america", "Go trekking in Patagonia or zip-lining in Costa Rica.");
        responses.put("luxury vacation", "Enjoy overwater bungalows in the Maldives or a private villa in Bali.");
        responses.put("best places for food", "Try Italy for pasta, Japan for sushi, or Mexico for tacos.");
        responses.put("solo travel destinations", "Thailand, Portugal, and New Zealand are great for solo travelers.");
        responses.put("family-friendly spots", "Visit Disneyland, the Maldives, or Yellowstone National Park.");
        responses.put("best city breaks", "New York, London, and Tokyo offer vibrant urban experiences.");
        responses.put("hiking trails", "Explore the Inca Trail, Appalachian Trail, or Mont Blanc Circuit.");
        responses.put("desert adventures", "Visit the Sahara in Morocco, Wadi Rum in Jordan, or the Atacama in Chile.");
        responses.put("island escapes", "Discover the Galápagos, Seychelles, or Maldives for an island getaway.");
        responses.put("winter holidays", "Ski in the Alps or enjoy the Northern Lights in Lapland.");
        responses.put("beaches in africa", "Check out Zanzibar, Cape Verde, or Seychelles.");
        responses.put("cultural in europe", "Explore Rome, Athens, or Prague for rich cultural experiences.");
        responses.put("wildlife in australia", "Visit Kangaroo Island, the Great Barrier Reef, or the Outback.");
        responses.put("best time to visit europe", "Spring (April-May) or fall (September-October) is ideal.");
        responses.put("visa-free countries", "For many, Thailand, Maldives, and Fiji require no visas.");
        responses.put("popular travel apps", "Try Skyscanner, Google Maps, or Airbnb for seamless travel.");
        responses.put("travel tips for beginners", "Plan ahead, pack light, and always have a backup of documents.");
        responses.put("eco-friendly travel tips", "Use public transport, stay in eco-lodges, and minimize waste.");
        responses.put("unique travel destinations", "Try Bhutan, Easter Island, or Cappadocia for something different.");
        responses.put("road trips in the usa", "Drive along Route 66 or explore the Pacific Coast Highway.");
        responses.put("best places for relaxation", "Visit the Maldives, Bali, or Iceland's Blue Lagoon.");
        responses.put("adventure activities", "Go bungee jumping, white-water rafting, or skydiving.");
        responses.put("art lovers' destinations", "Visit Florence, Paris, or Amsterdam for world-famous art.");
        responses.put("romantic beaches", "Head to Bora Bora, the Maldives, or Maui for dreamy beaches.");
        responses.put("kids' travel activities", "Theme parks, interactive museums, and safaris are great for children.");
        responses.put("local experiences", "Try a homestay, visit local markets, or join a cooking class.");
        responses.put("offbeat places in asia", "Visit Bagan in Myanmar or explore the Komodo Islands in Indonesia.");
        responses.put("budget airlines", "Try Ryanair, AirAsia, or Southwest for affordable flights.");
        responses.put("must-see wonders", "Visit the Great Wall of China, Machu Picchu, or the Grand Canyon.");
        responses.put("travel in covid times", "Check local restrictions and carry masks and sanitizers.");
        responses.put("cruise trips", "Consider a Mediterranean cruise or an Alaskan voyage.");
        responses.put("best places for photography", "Iceland, New Zealand, and Namibia offer breathtaking photo ops.");
        responses.put("unique festivals", "Diwali in India or the Lantern Festival in Thailand are amazing.");
        responses.put("cheap places to stay", "Hostels, guesthouses, and Airbnbs are affordable options.");
        responses.put("camping spots", "Try Yellowstone, Yosemite, or the Lake District for camping.");
        responses.put("currency exchange tips", "Use international cards or exchange at local banks for better rates.");
        responses.put("traveling with pets", "Ensure pet-friendly accommodations and carry necessary documents.");
        responses.put("health tips while traveling", "Stay hydrated, carry basic medicines, and avoid street food in unsafe areas.");
        responses.put("solo safety tips", "Stay in safe areas, keep family informed, and avoid isolated spots at night.");
        responses.put("snow destinations", "Enjoy winter in Switzerland, Lapland, or Canada.");
        responses.put("luxury train rides", "Try the Venice Simplon-Orient-Express or the Blue Train in South Africa.");
        responses.put("beaches in the caribbean", "Barbados, St. Lucia, and Jamaica offer pristine beaches.");
        responses.put("travel packing tips", "Roll your clothes, pack versatile outfits, and keep essentials handy.");
        responses.put("family road trips", "Drive through the Scottish Highlands or the Great Ocean Road.");
        responses.put("best time for safaris", "Visit African parks during the dry season (June-October).");
        responses.put("iconic landmarks", "See the Eiffel Tower, Statue of Liberty, or the Sydney Opera House.");
        responses.put("adventure in africa", "Go sandboarding in Namibia or climb Mount Kilimanjaro.");
        responses.put("street food destinations", "Bangkok, Mexico City, and Istanbul are famous for street food.");
        responses.put("luxury cruise destinations", "The Mediterranean and Caribbean are perfect for luxury cruises.");
        responses.put("beach safety tips", "Check the tides, wear sunscreen, and stay hydrated.");
        responses.put("explore jungles", "Visit the Amazon in Brazil, Borneo in Malaysia, or the Congo Basin.");
        responses.put("romantic cities", "Paris, Kyoto, and Prague are perfect for couples.");
        responses.put("best hiking gear", "Invest in sturdy boots, moisture-wicking clothing, and a good backpack.");
        responses.put("travel scams to avoid", "Beware of overcharging taxis, fake guides, and pickpockets.");
        responses.put("sustainable travel spots", "Costa Rica, New Zealand, and Bhutan are eco-friendly destinations.");
        responses.put("hot air balloon rides", "Try Cappadocia in Turkey or Serengeti in Tanzania.");
        responses.put("famous museums", "The Louvre in Paris, the British Museum in London, and the Smithsonian in the USA.");
        responses.put("train travel in europe", "Use the Eurail Pass for easy access across European countries.");
        responses.put("underwater adventures", "Dive in the Great Barrier Reef, Maldives, or Belize's Blue Hole.");
        responses.put("road trips in europe", "Drive along Amalfi Coast or through the Scottish Highlands.");
        responses.put("desert camping", "Try glamping in Wadi Rum or the Sahara Desert.");
        responses.put("foodie destinations", "Explore Bangkok, Paris, or Istanbul for incredible cuisines.");
        responses.put("wildlife photography", "Capture wildlife in Masai Mara, Yellowstone, or the Galápagos Islands.");
        responses.put("budget family vacations", "Visit Malaysia, Greece, or Mexico for affordable family trips.");
        responses.put("popular travel influencers", "Follow bloggers like Nomadic Matt or Adventurous Kate for inspiration.");
        responses.put("best time for beach trips", "Visit beaches during the dry season for sunny days and calm waters.");
        responses.put("explore local culture", "Join a festival, visit markets, or take a local cooking class.");
        responses.put("romantic nature spots", "Visit Banff in Canada, Lake Bled in Slovenia, or the Amalfi Coast.");
        responses.put("cheap city trips", "Explore Prague, Budapest, or Lisbon for affordable city breaks.");
        responses.put("luxury beach stays", "Enjoy the Four Seasons in Bora Bora or Anantara in Maldives.");
        responses.put("cultural in south america", "Visit Machu Picchu, Buenos Aires, or Rio de Janeiro.");
        responses.put("adventure in australia", "Go scuba diving in the Great Barrier Reef or sandboarding on Kangaroo Island.");
        responses.put("nature trails", "Explore the Pacific Crest Trail or the Fjords in Norway.");
        responses.put("luxury family vacations", "Stay at Atlantis in the Bahamas or Disneyland Resort in Florida.");
        responses.put("wildlife in asia", "See tigers in India, orangutans in Borneo, or pandas in China.");
        responses.put("skiing spots", "Try Whistler in Canada, Zermatt in Switzerland, or Aspen in the USA.");
        responses.put("desert safaris", "Experience the Dubai desert or the Namib Desert in Namibia.");
        responses.put("wildlife in south america", "Explore the Amazon Rainforest or see penguins in Patagonia.");
        responses.put("beaches in australia", "Bondi Beach, Whitehaven Beach, and Bells Beach are iconic.");
        responses.put("family adventure trips", "Go hiking in the Rockies or visit Costa Rica for zip-lining.");
        responses.put("luxury city stays", "Stay at The Ritz in Paris or the Burj Al Arab in Dubai.");
        responses.put("best places for stargazing", "Visit Mauna Kea in Hawaii or the Atacama Desert in Chile.");
        responses.put("offbeat places in europe", "Explore Albania, the Azores, or Slovenia for unique experiences.");
        responses.put("budget adventures", "Trek in Nepal, snorkel in the Philippines, or camp in national parks.");
        responses.put("island adventures", "Go kayaking in Palawan or surfing in Maui.");
        responses.put("street art cities", "Visit Berlin, Melbourne, or São Paulo for vibrant street art.");
        responses.put("unique accommodations", "Stay in an igloo in Finland or a treehouse in Costa Rica.");
        responses.put("cycling routes", "Explore the Danube Cycle Path or the Great Ocean Road.");
        responses.put("family cultural trips", "Visit the Pyramids in Egypt or the Colosseum in Rome.");
        responses.put("romantic road trips", "Drive along the Amalfi Coast or through the French Riviera.");
        responses.put("city travel tips", "Use public transport, walk where possible, and plan attractions ahead.");
        responses.put("popular beaches", "Copacabana in Brazil, Waikiki in Hawaii, and Phuket in Thailand.");
        responses.put("remote destinations", "Explore Antarctica, Bhutan, or the Faroe Islands.");
        responses.put("nature photography", "Visit Yosemite, the Dolomites, or Banff for stunning landscapes.");
        responses.put("best beaches in asia", "Bali, Phuket, and the Maldives are top picks.");
        responses.put("cultural in north america", "Visit museums in Washington, D.C., or cultural festivals in Mexico.");
        responses.put("wildlife in antarctica", "See penguins, seals, and whales on a polar expedition.");
        responses.put("train travel in asia", "Experience the Shinkansen in Japan or the Himalayan Railway in India.");
        responses.put("hot springs", "Relax in Blue Lagoon, Iceland, or Banff Upper Hot Springs in Canada.");
        responses.put("backpacking tips", "Pack light, use hostels, and stick to a budget.");
        responses.put("offbeat adventure", "Explore caves in Vietnam or volcanoes in Hawaii.");
        responses.put("beaches in north america", "Visit Miami Beach, Cancun, or Tulum.");
        responses.put("luxury safaris", "Stay at Singita in Tanzania or &Beyond lodges in South Africa.");
        responses.put("world's best food markets", "Explore Borough Market in London or Tsukiji in Tokyo.");
        responses.put("road trips in asia", "Drive through the Himalayas or Vietnam’s Hai Van Pass.");
        responses.put("budget-friendly islands", "Visit Langkawi, Koh Samui, or Boracay.");
        responses.put("family nature trips", "Visit Yellowstone or explore Costa Rican rainforests.");
        responses.put("romantic cruises", "Sail through the Greek Islands or the Norwegian Fjords.");
        responses.put("top shopping destinations", "Dubai, New York, and Milan are perfect for shopaholics.");
        responses.put("luxury island vacations", "Stay in Bora Bora, the Seychelles, or Mauritius.");
        responses.put("snow activities", "Go dog sledding in Alaska or snowshoeing in Canada.");
        responses.put("wildlife in europe", "See brown bears in Romania or bison in Poland.");
        responses.put("city tours", "Join a walking tour in Barcelona or a bike tour in Amsterdam.");
        responses.put("hidden travel gems", "Visit Meteora in Greece or Plitvice Lakes in Croatia.");
        responses.put("beachside cafes", "Sip coffee in Bali, Bondi Beach, or Santorini.");
        responses.put("family-friendly beaches", "Clearwater Beach, Florida, and Sentosa, Singapore, are ideal.");
        responses.put("volunteer travel", "Help with conservation in Costa Rica or teaching in Nepal.");
        responses.put("budget cruises", "Try Caribbean cruises or budget European river cruises.");
        responses.put("desert wildlife", "Spot camels in the Sahara or oryx in Namibia.");
        responses.put("sustainable wildlife tours", "Explore eco-safaris in Kenya or nature reserves in Costa Rica.");
        responses.put("travel tech tips", "Carry a power bank, noise-canceling headphones, and a travel adapter.");
        responses.put("adventure in the usa", "Raft the Colorado River or climb in Yosemite.");
        responses.put("offbeat cities", "Visit Tallinn, Riga, or Belgrade for lesser-known urban gems.");
        responses.put("luxury spa resorts", "Stay at the Como Shambhala in Bali or Canyon Ranch in Arizona.");
        responses.put("city nightlife", "Enjoy nightlife in Berlin, Bangkok, or Rio de Janeiro.");
        responses.put("historic landmarks", "See the Acropolis in Greece, Petra in Jordan, or Stonehenge in the UK.");
        responses.put("solo-friendly activities", "Take a cooking class, go snorkeling, or join group tours.");
        responses.put("cultural immersion trips", "Stay in a ryokan in Japan or visit monasteries in Bhutan.");
        responses.put("best time to visit asia", "Spring (March-May) or fall (September-November) is ideal.");
        responses.put("exploring caves", "Visit Carlsbad Caverns in the USA or Waitomo in New Zealand.");
        responses.put("family theme parks", "Visit Disneyland, Universal Studios, or Legoland.");
        responses.put("world's longest beaches", "Explore Cox's Bazar in Bangladesh or Praia do Cassino in Brazil.");
        responses.put("romantic winter spots", "Head to Hallstatt in Austria or Quebec City in Canada.");
        responses.put("adventure islands", "Surf in Hawaii or explore the Azores.");
        responses.put("luxury mountain lodges", "Stay at Amangani in Wyoming or Oberoi Wildflower Hall in India.");
        responses.put("hidden beaches", "Discover El Nido in the Philippines or Navagio Beach in Greece.");
        responses.put("travel documentaries", "Watch 'Planet Earth,' 'Expedition Happiness,' or 'The Dawn Wall.'");
        responses.put("best time for the caribbean", "Visit between December and April for ideal weather.");
        responses.put("famous markets", "Visit the Grand Bazaar in Istanbul or Chatuchak in Bangkok.");
        responses.put("food festivals", "Attend Taste of Chicago or Pizzafest in Naples.");
        responses.put("eco-tourism", "Visit Costa Rica, Iceland, or Bhutan for sustainable travel.");
        responses.put("unique cafes", "Check out cat cafes in Tokyo or art cafes in Prague.");
        responses.put("road trips in africa", "Drive along the Garden Route in South Africa.");
        responses.put("luxury city tours", "Take a private gondola ride in Venice or a helicopter tour in New York.");
        responses.put("family ski trips", "Try Aspen in the USA or Whistler in Canada.");
        responses.put("famous bridges", "Walk across the Golden Gate in San Francisco or Tower Bridge in London.");
        responses.put("mountain adventures", "Hike the Rockies or climb Mount Fuji.");
        responses.put("travel insurance tips", "Cover medical emergencies, cancellations, and lost luggage.");
        responses.put("beaches for water sports", "Try Kuta Beach in Bali or Waikiki in Hawaii.");
        responses.put("luxury desert trips", "Stay at Al Maha in Dubai or Sonoran Desert lodges in Arizona.");
        responses.put("exploring ruins", "Visit Pompeii in Italy or Tikal in Guatemala.");
        responses.put("unique city activities", "Take a canal cruise in Amsterdam or a tuk-tuk ride in Bangkok.");
        responses.put("historic train rides", "Ride the Flam Railway in Norway or the Darjeeling Himalayan Railway.");
        responses.put("wildlife parks", "Explore Yellowstone in the USA or Etosha in Namibia.");
        responses.put("river cruises", "Sail along the Danube or the Nile.");
        responses.put("family-friendly activities", "Visit aquariums, zoos, or planetariums.");
        responses.put("city sightseeing passes", "Get city passes for Paris, Rome, or New York.");
        responses.put("cultural in africa", "Explore Timbuktu, Marrakech, or Cape Town.");
        responses.put("adventure in antarctica", "Go ice trekking or explore polar icebergs.");
        responses.put("desert landmarks", "Visit Uluru in Australia or the dunes in Death Valley.");
        responses.put("winter markets", "Explore Christmas markets in Vienna, Prague, or Strasbourg.");
        responses.put("budget-friendly road trips", "Drive through New Zealand or Scotland.");
        responses.put("default", "I'm still learning! How can I assist you today?");
//        Updated hare for testing
//        Again testing the update

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
            bubble.setBorder(new RoundedBorder(10)); // Rounded corners

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
