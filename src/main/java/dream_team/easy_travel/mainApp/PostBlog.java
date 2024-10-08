package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
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
    private final Easy_Travel app; // Add a reference to Easy_Travel

    public PostBlog(List<BlogPost> blogPosts, Easy_Travel app) {
        this.blogPosts = blogPosts;
        this.app = app;
        setLayout(null);

        JLabel titleLabel = new JLabel("Title");
        titleLabel.setBounds(100, 50, 100, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(200, 50, 200, 30);
        add(titleField);

        JLabel locationLabel = new JLabel("Location");
        locationLabel.setBounds(100, 100, 100, 30);
        add(locationLabel);

        locationField = new JTextField();
        locationField.setBounds(200, 100, 200, 30);
        add(locationField);

        JLabel restaurantsLabel = new JLabel("Restaurants (name, location, rating, price)");
        restaurantsLabel.setBounds(100, 150, 300, 30);
        add(restaurantsLabel);

        int yPosition = 200;
        for (int i = 0; i < 4; i++) {
            JTextField nameField = new JTextField("Name");
            nameField.setBounds(100, yPosition, 150, 30);
            add(nameField);
            restaurantNames.add(nameField);

            JTextField locationField = new JTextField("Location");
            locationField.setBounds(260, yPosition, 150, 30);
            add(locationField);
            restaurantLocations.add(locationField);

            JTextField ratingField = new JTextField("Rating");
            ratingField.setBounds(420, yPosition, 70, 30);
            add(ratingField);
            restaurantRatings.add(ratingField);

            JTextField priceField = new JTextField("Price");
            priceField.setBounds(500, yPosition, 70, 30);
            add(priceField);
            restaurantPrices.add(priceField);

            yPosition += 40;
        }

        JLabel descriptionLabel = new JLabel("Description");
        descriptionLabel.setBounds(100, yPosition, 100, 30);
        add(descriptionLabel);

        descriptionArea = new JTextArea();
        descriptionArea.setBounds(200, yPosition, 200, 100);
        add(descriptionArea);

        JButton imageUpload = new JButton("Upload Images");
        imageUpload.setBounds(200, yPosition + 120, 200, 30);
        add(imageUpload);

        JButton postButton = getPostButton();
        postButton.setBounds(200, yPosition + 160, 200, 30);
        add(postButton);

        imageUpload.addActionListener(e -> uploadImages());
    }

    private void uploadImages() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(true);
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

            // Add to local list
            int newId = blogPosts.size() + 1; // Example ID generation
            blogPosts.add(new BlogPost(newId, title, description, imagePaths.get(0)));

            // Notify the Blog panel to update
            app.refreshBlogPanel(); // Call the refresh method
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