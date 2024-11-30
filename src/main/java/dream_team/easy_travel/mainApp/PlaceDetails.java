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
        fetchBlogDetails(id,app);

//        // Back button
//        JButton backButton = new Button();
//                backButton.setText("⬅ Back");
//        backButton.setBounds(50, 500, 100, 30);
//        backButton.setBackground(new Color(173, 216, 230)); // Soft blue button
//        backButton.setBounds(50, 630, 100, 30);
////        backButton.setFocusPainted(false);
//        add(backButton);
//
//        backButton.addActionListener(e -> app.showPanel("places"));


        // Start image carousel if images are loaded
        if (images != null && images.length > 0) {
            startImageCarousel();
        }
    }

    private void fetchBlogDetails(int id, Easy_Travel app) {
        String query = "SELECT title, location, description, image1, image2, image3 " +
                        "FROM blog_posts " +
                        "WHERE id = ?";
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
                descriptionArea.setBounds(50, 90, 400, 600);
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

                images = Arrays.stream(images).filter(Objects::nonNull).toArray(ImageIcon[]::new);
              if (images.length > 0) {
                imageLabel = new JLabel(images[0]);
                imageLabel.setBounds(500, 10, 600, 350);
                imageLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add a border to the image
                imageLabel.setHorizontalAlignment(JLabel.CENTER);
                imageLabel.setVerticalAlignment(JLabel.CENTER);
                imageLabel.setIcon(new ImageIcon(images[0].getImage().getScaledInstance(imageLabel.getWidth(), imageLabel.getHeight(), Image.SCALE_SMOOTH)));
                add(imageLabel);
                  // Back button
                  JButton backButton = new Button();
                  backButton.setText("⬅ Back");
                  backButton.setBounds(50, 500, 100, 30);
                  backButton.setBackground(new Color(173, 216, 230)); // Soft blue button
                  backButton.setBounds(50, 630, 100, 30);
                  backButton.setFocusPainted(true);
                  backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                  add(backButton);

                  backButton.addActionListener(e -> app.showPanel("Blog"));
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
