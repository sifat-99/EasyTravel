package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.DatabaseConnection.*;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.plaf.basic.BasicScrollBarUI;


public class Blog extends JPanel {
    private List<BlogPost> blogPosts;
    private final JPanel cardPanel;
    public Easy_Travel app;
    private final JTextField searchField;

    public Blog(List<BlogPost> blogPosts, Easy_Travel app) {
        this.blogPosts = blogPosts;
        setLayout(new BorderLayout());
        setBackground(Color.BLUE);
        this.app = app;

        setOpaque(false);

        Font lobsterFont = loadLobsterFont();

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 750));

        ImageIcon imageIcon = loadImageIcon();
        if (imageIcon == null) {
            throw new RuntimeException("Failed to load image: /BlogBG.jpg");
        }

        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(imageLabel, Integer.valueOf(0));

        cardPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        cardPanel.setBounds(200, 70, 400, 400);
        cardPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Blog Posts");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        layeredPane.add(titleLabel, Integer.valueOf(1));
        titleLabel.setBounds(530, 10, 200, 50);


        // Search bar panel
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(800, 50));

        searchField = new JTextField("Search");
        searchField.setPreferredSize(new Dimension(700, 40));
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = new JButton("🔍");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBlogPosts(searchField.getText().trim());
            }
        });
        searchPanel.add(searchButton, BorderLayout.EAST);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBlogPosts();
            }
        });
        searchPanel.add(refreshButton, BorderLayout.WEST);

        JButton uploadYourThoughts = new Button();
        uploadYourThoughts.setText("Upload Post");
        uploadYourThoughts.setBounds(1000, 10, 150, 50);
        layeredPane.add(uploadYourThoughts, Integer.valueOf(2));

        uploadYourThoughts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog popup = CreateBlogUploadPopup(app,blogPosts);
                popup.setVisible(true);
            }
        });


        layeredPane.add(searchPanel, Integer.valueOf(3));
        searchPanel.setBounds(200, 50, 800, 50);

        // Load initial blog posts
        loadBlogPosts();

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBounds(100, 110, 1000, 550);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Custom scrollbar colors for main scrollPane
        scrollPane.getVerticalScrollBar().setUI(createCustomScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(createCustomScrollBarUI());

        layeredPane.add(scrollPane, Integer.valueOf(20));

        add(layeredPane, BorderLayout.CENTER);
    }

    private BasicScrollBarUI createCustomScrollBarUI() {
        return new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(169, 169, 169); // Color of the scrollbar thumb
                this.trackColor = new Color(60, 63, 65); // Color of the scrollbar track (matches background)
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        };
    }

    public void loadBlogPosts() {
        blogPosts = fetchBlogPostsFromDatabase();
        displayBlogPosts(blogPosts);
    }

    private List<BlogPost> fetchBlogPostsFromDatabase() {
        List<BlogPost> posts = new ArrayList<>();
        String query = "SELECT id, title, description, image1 FROM blog_posts";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                byte[] image = rs.getBytes("image1");
                posts.add(new BlogPost(id, title, description, image));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private void displayBlogPosts(List<BlogPost> posts) {
        cardPanel.removeAll();
        for (BlogPost post : posts) {
            JPanel card = createCard(post.getTitle(), post.getDescription(), post.getImage(), post.getId (), app);
            cardPanel.add(card);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void searchBlogPosts(String query) {
        if (query.isEmpty()) {
            displayBlogPosts(blogPosts); // Show all posts if the query is empty
            return;
        }
        List<BlogPost> filteredPosts = new ArrayList<>();
        for (BlogPost post : blogPosts) {
            if (post.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    post.getDescription().toLowerCase().contains(query.toLowerCase())) {
                filteredPosts.add(post);
            }
        }
        displayBlogPosts(filteredPosts); // Show filtered posts
    }

    private ImageIcon loadImageIcon() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/BlogBG.jpg")));
            Image image = icon.getImage().getScaledInstance(1200, 750, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + "/HomeBG.png");
            return null;
        }
    }

    private JPanel createCard(String title, String description, byte[] image, int id, Easy_Travel app) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(500, 250));

        card.setBorder(new TitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 18)));
        card.setOpaque(false);

        JTextArea descriptionLabel = new JTextArea();
        descriptionLabel.setText(description);
        descriptionLabel.setFont(loadLobsterFont().deriveFont(14f));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setEditable(false);
        descriptionLabel.setOpaque(false);
        descriptionLabel.setPreferredSize(new Dimension(250, 150));
        // Justify text
        descriptionLabel.setLineWrap(true);
        descriptionLabel.setWrapStyleWord(true);
        descriptionLabel.setMargin(new Insets(10, 10, 10, 10));
        descriptionLabel.setCaretPosition(0);
        descriptionLabel.setBackground(new Color(0, 0, 0, 0)); // Transparent background

//        StyledDocument doc = descriptionLabel.getStyledDocument();
//        SimpleAttributeSet justify = new SimpleAttributeSet();
//        StyleConstants.setAlignment(justify, StyleConstants.ALIGN_LEFT);
//        doc.setParagraphAttributes(0, doc.getLength(), justify, false);
//        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the description

        JScrollPane scrollPane = new JScrollPane(descriptionLabel);
        scrollPane.setPreferredSize(new Dimension(250, 150)); // Set the size of the scroll pane
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);

        // Custom scrollbar colors
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(169, 169, 169); // Color of the scrollbar thumb
                this.trackColor = new Color(60, 63, 65); // Color of the scrollbar track (matches background)
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new Button();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });

        card.add(scrollPane, BorderLayout.CENTER);

        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(250, 150));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.showPanelWithID("showBlogPostDetails", id);
            }
        });

        if (image != null) {
            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(image));
                Image scaledImage = img.getScaledInstance(250, 150, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        card.add(imageLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10)); // Center-align the button with some top padding
        buttonPanel.setOpaque(false);
        JButton viewButton = new Button();
        viewButton.setText("View more");
        viewButton.setPreferredSize(new Dimension(100, 40)); // Make button smaller
        viewButton.addActionListener(e -> app.showPanelWithID("showBlogPostDetails", id));
        buttonPanel.add(viewButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

    private JDialog CreateBlogUploadPopup(Easy_Travel app, List<BlogPost> blogPosts) {
        JFrame uploadPanel = app.getFrame();
        JDialog popupUploadDialogue = new JDialog(uploadPanel, "Upload Blog", true);
        popupUploadDialogue.setUndecorated(true);
        popupUploadDialogue.setLayout(new BorderLayout());
        popupUploadDialogue.setBackground(Color.DARK_GRAY);

        // Create the PostBlog panel
        PostBlog postBlogPanel = new PostBlog(blogPosts, app);

        // Add the PostBlog panel to the dialog
        popupUploadDialogue.add(postBlogPanel, BorderLayout.CENTER);

        // Close button panel
        JPanel closePanel = new JPanel();
        closePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setBackground(Color.DARK_GRAY);

        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        closeButton.addActionListener(e -> popupUploadDialogue.dispose());
        closePanel.add(closeButton);

        popupUploadDialogue.add(closePanel, BorderLayout.SOUTH);

        popupUploadDialogue.setSize(800, 600);
        popupUploadDialogue.setLocationRelativeTo(uploadPanel);
        popupUploadDialogue.setAlwaysOnTop(true);

        return popupUploadDialogue;
    }
    private Font loadLobsterFont() {
        try (InputStream is = getClass().getResourceAsStream("/Bangla.ttf")) {
            return Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, 20); // Fallback font
        }
    }



}