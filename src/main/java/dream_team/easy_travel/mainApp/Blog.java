package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


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

        layeredPane.add(searchPanel, Integer.valueOf(2));
        searchPanel.setBounds(200, 50, 800, 50);

        // Load initial blog posts
        loadBlogPosts(app);

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBounds(200, 110, 800, 600);
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

    public void loadBlogPosts(Easy_Travel app) {
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

        JTextPane descriptionLabel = new JTextPane();
        descriptionLabel.setText(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setEditable(false);
        descriptionLabel.setOpaque(false);
        // Justify text
        StyledDocument doc = descriptionLabel.getStyledDocument();
        SimpleAttributeSet justify = new SimpleAttributeSet();
        StyleConstants.setAlignment(justify, StyleConstants.ALIGN_LEFT);
        doc.setParagraphAttributes(0, doc.getLength(), justify, false);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding around the description

        JScrollPane scrollPane = new JScrollPane(descriptionLabel);
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
                JButton button = new JButton();
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
        JButton viewButton = new JButton("View");
        viewButton.setPreferredSize(new Dimension(80, 25)); // Make button smaller
        viewButton.addActionListener(e -> app.showPanelWithID("showBlogPostDetails", id));
        buttonPanel.add(viewButton);

        card.add(buttonPanel, BorderLayout.SOUTH);

        return card;
    }

}