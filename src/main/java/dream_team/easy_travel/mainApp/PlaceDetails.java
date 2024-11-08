package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaceDetails extends JPanel {
    private JLabel imageLabel;
    private ImageIcon[] images;
    private int imageIndex = 0;

    public PlaceDetails(Easy_Travel app, int id) {
        setLayout(null);

        // Fetch and display details
        fetchBlogDetails(id);

        // Back button
        JButton backButton = new JButton("Back");
        backButton.setBounds(100, 500, 100, 30);
        add(backButton);
        backButton.addActionListener(e -> app.showPanel("Blog"));

        // Start image carousel if images are loaded
        if (images != null && images.length > 0) {
            startImageCarousel();
        }
    }

  private void fetchBlogDetails(int id) {
    String query = "SELECT bp.title, bp.location, bp.description, bp.image1, bp.image2, bp.image3, nr.name, nr.location AS restaurant_location, nr.rating, nr.price " +
                   "FROM blog_posts bp " +
                   "LEFT JOIN Nearby_Restaurants nr ON bp.id = nr.blog_post_id " +
                   "WHERE bp.id = ?";
    try (Connection conn = ConnectDB.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {

        // Set the id parameter for the query
        stmt.setInt(1, id);

        // Execute query
        ResultSet rs = stmt.executeQuery();

        List<String> restaurants = new ArrayList<>();
        if (rs.next()) {
            // Set title
            JLabel titleLabel = new JLabel(rs.getString("title"));
            titleLabel.setBounds(100, 50, 300, 30);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            add(titleLabel);

            // Set location
            JLabel locationLabel = new JLabel("Location: " + rs.getString("location"));
            locationLabel.setBounds(100, 90, 300, 20);
            add(locationLabel);

            // Set description
            JLabel descriptionLabel = new JLabel("<html>" + rs.getString("description") + "</html>");
            descriptionLabel.setBounds(100, 120, 300, 100);
            add(descriptionLabel);

            // Load images
            images = new ImageIcon[3];
            images[0] = rs.getBytes("image1") != null ? new ImageIcon(rs.getBytes("image1")) : null;
            images[1] = rs.getBytes("image2") != null ? new ImageIcon(rs.getBytes("image2")) : null;
            images[2] = rs.getBytes("image3") != null ? new ImageIcon(rs.getBytes("image3")) : null;

            // Filter null entries in the images array
            images = java.util.Arrays.stream(images).filter(Objects::nonNull).toArray(ImageIcon[]::new);

            // Display the first image if any
            if (images.length > 0) {
                imageLabel = new JLabel(images[0]);
                imageLabel.setBounds(100, 230, 200, 150);
                add(imageLabel);
            }

            // Collect restaurant details
            do {
                String restaurantName = rs.getString("name");
                if (restaurantName != null) {
                    String restaurantLocation = rs.getString("restaurant_location");
                    double rating = rs.getDouble("rating");
                    double price = 0.0;
                    try {
                        price = rs.getDouble("price");
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid price format: " + rs.getString("price"));
                    }
                    restaurants.add(String.format("Name: %s, Location: %s, Rating: %.1f, Price: %.2f", restaurantName, restaurantLocation, rating, price));
                }
            } while (rs.next());
        }

        // Display restaurant details
        if (!restaurants.isEmpty()) {
            JTextArea restaurantsArea = new JTextArea();
            restaurantsArea.setBounds(100, 400, 300, 100);
            restaurantsArea.setText(String.join("\n", restaurants));
            restaurantsArea.setEditable(false);
            add(restaurantsArea);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    private void startImageCarousel() {
        Timer timer = new Timer(2000, e -> {
            imageIndex = (imageIndex + 1) % images.length;
            imageLabel.setIcon(images[imageIndex]);
        });
        timer.start();
    }
}