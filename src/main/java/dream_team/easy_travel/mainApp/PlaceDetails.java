package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlaceDetails extends JPanel {
    private JLabel imageLabel;
    private ImageIcon[] images;
    private int imageIndex = 0;

    public PlaceDetails(Easy_Travel app, int id) {
        // Set layout and background
        setLayout(null);
        setOpaque(false);
//        setBackground(new Color(240, 248, 255)); // Light blue background for a clean design

        // Fetch and display blog details
        fetchBlogDetails(id);

        // Back button
        JButton backButton = new JButton("⬅ Back");
        backButton.setBounds(50, 500, 100, 30);
        backButton.setBackground(new Color(173, 216, 230)); // Soft blue button
        backButton.setBounds(50, 630, 100, 30);
        backButton.setFocusPainted(false);
        add(backButton);
        backButton.addActionListener(e -> app.showPanel("Blog"));

        // Start image carousel if images are loaded
        if (images != null && images.length > 0) {
            startImageCarousel();
        }
    }

    private void fetchBlogDetails(int id) {
        String query = "SELECT bp.title, bp.location, bp.description, bp.image1, bp.image2, bp.image3, " +
                "nr.name, nr.location AS restaurant_location, nr.rating, nr.price " +
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
                // Display title
                JLabel titleLabel = new JLabel(rs.getString("title"));
                titleLabel.setBounds(50, 20, 700, 30);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                titleLabel.setForeground(new Color(0, 51, 102)); // Dark blue text
                add(titleLabel);

                // Display location
                JLabel locationLabel = new JLabel("📍 Location: " + rs.getString("location"));
                locationLabel.setBounds(50, 60, 700, 20);
                locationLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                add(locationLabel);

                // Display description
                JTextPane descriptionArea = new JTextPane();
                descriptionArea.setText(rs.getString("description"));
                descriptionArea.setBounds(50, 90, 400, 250);
                descriptionArea.setFont(new Font("Arial", Font.PLAIN, 14));
                descriptionArea.setEditable(false);
//                descriptionArea.setBackground(new Color(240, 248, 255));
                descriptionArea.setOpaque(false);
                // Justify text
                StyledDocument doc = descriptionArea.getStyledDocument();
                SimpleAttributeSet justify = new SimpleAttributeSet();
                StyleConstants.setAlignment(justify, StyleConstants.ALIGN_LEFT);
                doc.setParagraphAttributes(0, doc.getLength(), justify, false);
                add(descriptionArea);

                // Load images
                images = new ImageIcon[3];
                images[0] = rs.getBytes("image1") != null ? new ImageIcon(rs.getBytes("image1")) : null;
                images[1] = rs.getBytes("image2") != null ? new ImageIcon(rs.getBytes("image2")) : null;
                images[2] = rs.getBytes("image3") != null ? new ImageIcon(rs.getBytes("image3")) : null;

                // Filter null entries in the images array
                images = Arrays.stream(images).filter(Objects::nonNull).toArray(ImageIcon[]::new);

                // Display the first image if any
              if (images.length > 0) {
                imageLabel = new JLabel(images[0]);
                imageLabel.setBounds(250, 370, 500, 250);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border to the image
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                imageLabel.setVerticalAlignment(JLabel.CENTER);
                imageLabel.setIcon(new ImageIcon(images[0].getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH)));
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
                        // Add formatted restaurant detail string to the list
                        restaurants.add(String.format("Name: %s,\nLocation: %s,\nRating: %.1f,\nPrice: %.2f", restaurantName, restaurantLocation, rating, price));
                    }
                } while (rs.next());

                // Display restaurants in accordion format
                if (!restaurants.isEmpty()) {
                    JPanel accordionPanel = new JPanel();
                    accordionPanel.setBounds(510, 20, 650, 350);
                    accordionPanel.setLayout(new BoxLayout(accordionPanel, BoxLayout.Y_AXIS));
//                    accordionPanel.setBackground(new Color(240, 248, 255)); // Match panel background
                    accordionPanel.setOpaque(false);


                    for (String restaurantDetails : restaurants) {
                        JPanel restaurantPanel = new JPanel(new BorderLayout());
                        restaurantPanel.setBackground(new Color(240, 255, 255)); // Light cyan
                        restaurantPanel.setOpaque(false);
                        restaurantPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                        restaurantPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

                        // Restaurant details text area
                        JTextArea detailsArea = new JTextArea(restaurantDetails);
                        detailsArea.setFont(new Font("Arial", Font.PLAIN, 14));
                        detailsArea.setLineWrap(true);
                        detailsArea.setWrapStyleWord(true);
                        detailsArea.setEditable(false);
                        detailsArea.setOpaque(false);
//                        detailsArea.setBackground(new Color(240, 255, 255)); // Match panel background

                        // Booking button
                        JButton bookButton = new Button();
                        bookButton.setText("Book Now");
                        bookButton.setPreferredSize(new Dimension(100, 20));
                        bookButton.setBackground(new Color(144, 238, 144)); // Light green for booking
                        bookButton.addActionListener(e -> JOptionPane.showMessageDialog(
                                this, "You booked " + restaurantDetails.split(",")[0].split(": ")[1]));

                        // Add details and button to the panel
                        restaurantPanel.add(detailsArea, BorderLayout.CENTER);
                        restaurantPanel.add(bookButton, BorderLayout.EAST);

                        // Add restaurant panel to the accordion
                        accordionPanel.add(restaurantPanel);
                    }

                    // Add accordion panel to the main panel
                    add(accordionPanel);
                }
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

    // Restaurant class for storing restaurant data
    private static class Restaurant {
        String name;
        String location;
        double rating;
        double price;

        Restaurant(String name, String location, double rating, double price) {
            this.name = name;
            this.location = location;
            this.rating = rating;
            this.price = price;
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = new Color(255, 255, 255); // Light blue
        Color color2 = new Color(173, 216, 230); // Soft blue
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}
