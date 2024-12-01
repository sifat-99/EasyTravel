package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//

public class PostBlog extends JPanel {
    private final JTextField titleField;
    private final JTextField locationField;
    private final JTextArea descriptionArea;
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
        locationLabel.setFont( new Font("SansSerif", Font.PLAIN, 20));
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

        JLabel descriptionLabel = new JLabel("Description: ");
        descriptionLabel.setFont( new Font("Arial", Font.PLAIN, 20));
        descriptionLabel.setForeground(Color.WHITE);
        descriptionArea = new JTextArea();
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(new EmptyBorder(5, 5, 5, 5));
        descriptionArea.setPreferredSize(new Dimension(300, 100));

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        add(descriptionLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(new JScrollPane(descriptionArea), gbc);

        JButton imageUpload = new JButton("Upload Images");
        imageUpload.setBackground(new Color(70, 130, 180));
        imageUpload.setForeground(Color.BLACK);
        imageUpload.setPreferredSize(new Dimension(150, 30));
        imageUpload.addActionListener(e -> uploadImages());

        gbc.gridx = 1;
        gbc.gridy = 3 + 1;
        add(imageUpload, gbc);

        JButton postButton = getPostButton();
        postButton.setForeground(Color.BLACK);
        postButton.setPreferredSize(new Dimension(150, 30));

        gbc.gridy = 3 + 2;
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
        JButton postButton = new Button();
        postButton.setText("Post");
        postButton.setBackground(new Color(35, 163, 223));
        postButton.addActionListener(e -> {
            String title = titleField.getText();
            String location = locationField.getText();
            String description = descriptionArea.getText();

            if (title.isEmpty() || location.isEmpty() || description.isEmpty() || imagePaths.size() != 3) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields and upload 3 images.");
                return;
            }

            saveBlogPostToDatabase(title, location, description, imagePaths);
            int newId = blogPosts.size() + 1;
            blogPosts.add(new BlogPost(newId, title, description, imagePaths.get(0)));
            app.refreshBlogPanel();
        });
        return postButton;
    }


    private void saveBlogPostToDatabase(String title, String location, String description,  List<byte[]> imagePaths) {
        String insertBlogSQL = "INSERT INTO blog_posts (title, location, description, image1, image2, image3) VALUES (?, ?, ?, ?, ?, ?)";
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
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to save blog post: " + ex.getMessage());
        }
    }
}