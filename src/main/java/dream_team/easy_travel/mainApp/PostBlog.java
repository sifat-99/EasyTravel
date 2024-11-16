package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//

public class PostBlog extends JPanel {
    private final JTextField titleField;
    private final JTextField locationField;
    private final JTextArea descriptionArea;
    private final List<JTextField> restaurantNames = new ArrayList<>();
    private final List<JTextField> restaurantLocations = new ArrayList<>();
    private final List<JTextField> restaurantRatings = new ArrayList<>();
    private final List<JTextField> restaurantPrices = new ArrayList<>();
    private final List<byte[]> imagePaths = new ArrayList<>();
    private final List<BlogPost> blogPosts;
    private final Easy_Travel app;

    public PostBlog(List<BlogPost> blogPosts, Easy_Travel app) {
        this.blogPosts = blogPosts;
        this.app = app;
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.cyan);
        setOpaque(false);
        loadImage();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
//        JLabel Title = new JLabel("Post Blog");
//        Title.setFont(new Font("Arial", Font.PLAIN, 30));
//        Title.setForeground(Color.WHITE);
//
//        Title.setBounds(300, -10, 100, 30);
//
//        add(Title);


        JLabel titleLabel = new JLabel("Title: ");
        titleLabel.setFont( new Font("Arial", Font.PLAIN, 20));
        titleLabel.setForeground(Color.WHITE);
        titleField = new JTextField();
        titleField.setToolTipText("Enter the title of your blog");
        titleField.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(titleLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(titleField, gbc);

        JLabel locationLabel = new JLabel("Location: ");
        locationLabel.setFont( new Font("Arial", Font.PLAIN, 20));
        locationLabel.setForeground(Color.WHITE);
        locationField = new JTextField();
        locationField.setToolTipText("Enter the location of your travel destination");
        locationField.setPreferredSize(new Dimension(300, 30));

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(locationLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(locationField, gbc);

        JLabel restaurantsLabel = new JLabel("Restaurants (Name, Location, Rating, Price)");
        restaurantsLabel.setFont( new Font("Arial", Font.PLAIN, 20));
        restaurantsLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        add(restaurantsLabel, gbc);

        gbc.gridwidth = 1;
        int yPosition = 3;
        for (int i = 0; i < 4; i++) {
            JTextField nameField = new JTextField("Restaurant Name");
            nameField.setPreferredSize(new Dimension(200, 30));
            restaurantNames.add(nameField);

            JTextField locationField = new JTextField("Location");
            locationField.setPreferredSize(new Dimension(100, 30));
            restaurantLocations.add(locationField);

            JTextField ratingField = new JTextField("Rating (1-5)");
            ratingField.setPreferredSize(new Dimension(100, 30));
            restaurantRatings.add(ratingField);

            JTextField priceField = new JTextField("Price");
            priceField.setPreferredSize(new Dimension(80, 30));
            restaurantPrices.add(priceField);

            gbc.gridx = 0;
            gbc.gridy = yPosition;
            add(nameField, gbc);

            gbc.gridx = 1;
            add(locationField, gbc);

            gbc.gridx = 2;
            add(ratingField, gbc);

            gbc.gridx = 3;
            add(priceField, gbc);

            yPosition++;
        }

        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionLabel.setFont( new Font("Arial", Font.PLAIN, 20));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        descriptionArea.setPreferredSize(new Dimension(300, 100));

        gbc.gridx = 0;
        gbc.gridy = yPosition;
        gbc.gridwidth = 1;
        add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = yPosition;
        gbc.gridwidth = 2;
        add(new JScrollPane(descriptionArea), gbc);

        JButton imageUpload = new JButton("Upload Images");
        imageUpload.setBackground(new Color(70, 130, 180));
        imageUpload.setForeground(Color.BLACK);
        imageUpload.setPreferredSize(new Dimension(150, 30));
        imageUpload.addActionListener(e -> uploadImages());

        gbc.gridx = 1;
        gbc.gridy = yPosition + 1;
        add(imageUpload, gbc);

        JButton postButton = getPostButton();
        postButton.setBackground(new Color(34, 139, 34));
        postButton.setForeground(Color.BLACK);
        postButton.setPreferredSize(new Dimension(150, 30));

        gbc.gridy = yPosition + 2;
        add(postButton, gbc);
    }

    private void uploadImages() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        int response = fileChooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            if (files.length != 3) {
                JOptionPane.showMessageDialog(this, "Please upload exactly 3 images.");
                return;
            }
            try {
                for (File file : files) {
                    imagePaths.add(Files.readAllBytes(file.toPath()));
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to read image files: " + ex.getMessage());
            }
        }
    }

    private Image backgroundImage;

    private void loadImage() {
        try {
            backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/BlogBG.jpg"))).getImage();
        } catch (NullPointerException e) {
            System.err.println("Background image not found.");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private JButton getPostButton() {
        JButton postButton = new JButton("Post");
        postButton.addActionListener(e -> {
            String title = titleField.getText();
            String location = locationField.getText();
            String description = descriptionArea.getText();

            if (title.isEmpty() || location.isEmpty() || description.isEmpty() || imagePaths.size() != 3) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and upload 3 images.");
                return;
            }

            List<String[]> restaurantData = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                restaurantData.add(new String[]{
                        restaurantNames.get(i).getText(),
                        restaurantLocations.get(i).getText(),
                        restaurantRatings.get(i).getText(),
                        restaurantPrices.get(i).getText()
                });
            }

            saveBlogPostToDatabase(title, location, description, restaurantData, imagePaths);
            int newId = blogPosts.size() + 1;
            blogPosts.add(new BlogPost(newId, title, description, imagePaths.get(0)));
            app.refreshBlogPanel();
        });
        return postButton;
    }


    private void saveBlogPostToDatabase(String title, String location, String description, List<String[]> restaurantData, List<byte[]> imagePaths) {
        String insertBlogSQL = "INSERT INTO blog_posts (title, location, description, image1, image2, image3) VALUES (?, ?, ?, ?, ?, ?)";
        String insertRestaurantSQL = "INSERT INTO Nearby_Restaurants (blog_post_id, name, location, rating, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insertBlogSQL)) {
            stmt.setString(1, title);
            stmt.setString(2, location);
            stmt.setString(3, description);
            stmt.setBytes(4, imagePaths.get(0));
            stmt.setBytes(5, imagePaths.get(1));
            stmt.setBytes(6, imagePaths.get(2));
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Blog post created successfully");

            // Get the ID of the newly inserted blog post
            int blogId = -1;
            try (PreparedStatement idStmt = conn.prepareStatement("SELECT MAX(id) FROM blog_posts");
                 ResultSet rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    blogId = rs.getInt(1);
                }
            }
            if (blogId == -1) {
                throw new SQLException("Failed to retrieve blog ID");
            }
            else {
                for (String[] data : restaurantData) {
                    try (PreparedStatement restaurantStmt = conn.prepareStatement(insertRestaurantSQL)) {
                        restaurantStmt.setInt(1, blogId);
                        restaurantStmt.setString(2, data[0]);
                        restaurantStmt.setString(3, data[1]);
                        restaurantStmt.setString(4, data[2]);
                        restaurantStmt.setString(5, data[3]);
                        restaurantStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save blog post: " + ex.getMessage());
        }
    }
}