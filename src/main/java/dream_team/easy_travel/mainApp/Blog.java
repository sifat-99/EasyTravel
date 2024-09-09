package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
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
import javax.imageio.ImageIO;

public class Blog extends JPanel {
    private List<BlogPost> blogPosts;
    private JPanel cardPanel;

    public Blog(List<BlogPost> blogPosts, Easy_Travel app) {
        this.blogPosts = blogPosts;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 750));

        ImageIcon imageIcon = loadImageIcon();
        if (imageIcon == null) {
            throw new RuntimeException("Failed to load image: /HomeBG.png");
        }

        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(imageLabel, Integer.valueOf(0));

        cardPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        cardPanel.setOpaque(false);

        // Load initial blog posts
        loadBlogPosts();

        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBounds(50, 50, 1100, 600);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        layeredPane.add(scrollPane, Integer.valueOf(1));

        add(layeredPane, BorderLayout.CENTER);
    }

    public void loadBlogPosts() {
        blogPosts = fetchBlogPostsFromDatabase();
        cardPanel.removeAll();
        for (BlogPost post : blogPosts) {
            JPanel card = createCard(post.getTitle(), post.getDescription(), post.getImage(), post.getId());
            cardPanel.add(card);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private List<BlogPost> fetchBlogPostsFromDatabase() {
        List<BlogPost> posts = new ArrayList<>();
        String query = "SELECT id, title, description, image1 FROM blog_posts";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
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

    private JPanel createCard(String title, String description, byte[] imageBytes, int blogId) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setSize(500, 800);
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                title, TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 20), Color.WHITE)
        );

        JLabel imageLabel = new JLabel();
        if (imageBytes != null) {
            try {
                BufferedImage img = ImageIO.read(new ByteArrayInputStream(imageBytes));
                if (img != null) {
                    ImageIcon imageIcon = new ImageIcon(img.getScaledInstance(500, 400, Image.SCALE_SMOOTH));
                    imageLabel.setIcon(imageIcon);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JButton moreDetailsButton = new JButton("More Details");
        moreDetailsButton.addActionListener(e -> {
            // app.showBlogPostDetails(blogId); // Implement this method to navigate to the details page
        });

        card.add(imageLabel, BorderLayout.NORTH);
        card.add(new JLabel("<html>" + description + "</html>", SwingConstants.CENTER), BorderLayout.CENTER);
        card.add(moreDetailsButton, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // app.showBlogPostDetails(blogId); // Navigate to the blog post details page
            }
        });

        return card;
    }
}