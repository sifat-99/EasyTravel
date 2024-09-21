package dream_team.easy_travel;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.mainApp.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Easy_Travel {
    private final JFrame frame;
    private final JPanel contentPanel;
    private JButton homeButton;
    private JButton blogButton, postButton;
    private JButton aboutButton;
    private JButton loginButton;
    private JButton logoutButton;
    private User loggedInUser;
    private final List<BlogPost> blogPosts;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Easy_Travel::new);
    }

    public Easy_Travel() {
        frame = new JFrame("Easy Travel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setBackground(Color.WHITE);
        frame.setResizable(false);
        ImageIcon Logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
        frame.setIconImage(Logo.getImage());

        // Initialize blogPosts list
        blogPosts = new ArrayList<>();
        // Load initial blog posts from the database
        loadInitialBlogPosts();

        // Create the custom menu bar with rounded style
        JPanel menuBar = createMenuBar();

        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setPreferredSize(new Dimension(1200, 800));

        // Add different views to the content panel
        contentPanel.add(new HomePage(this), "Home");
        contentPanel.add(new PostBlog(blogPosts, this), "Post"); // Updated constructor call
        contentPanel.add(new AboutUsPanel(this), "About");
        contentPanel.add(new Blog(blogPosts,this), "Blog");
        contentPanel.add(new LoginPanel(this), "Login");
        contentPanel.add(new SignUp(this), "SignUp");
        contentPanel.add(new showBlogPostDetails(1,this), "showBlogPostDetails");


        // Create a layered pane to hold the logoPanel and contentPanel
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 800));

        // Add the contentPanel to the default layer
        contentPanel.setBounds(0, 0, 1200, 800);
        layeredPane.add(contentPanel, JLayeredPane.DEFAULT_LAYER);

        // Set up the frame layout
        frame.setLayout(new BorderLayout());
        frame.add(menuBar, BorderLayout.NORTH);
        frame.add(layeredPane, BorderLayout.CENTER);

        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustMenuItems();
            }
        });

        // Show the initial view
        showPanel("Home");
        updateButtonColors(homeButton);
        updateFrameTitle("Home");
        updateButtonVisibility();

        frame.setVisible(true);
    }

    private void loadInitialBlogPosts() {
        String query = "SELECT * FROM blog_posts";
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                byte[] image = rs.getBytes("image1"); // Assuming image1 is the main image
                blogPosts.add(new BlogPost(id, title, description, image));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private JPanel createMenuBar() {
        JPanel menuBar = new JPanel(new BorderLayout());
        menuBar.setBackground(Color.WHITE);
        menuBar.setPreferredSize(new Dimension(1200, 80));

        // Create logo panel
        JPanel logoPanel = getLogoPanel();

        // Create navigation panel
        JPanel navPanel = getNavPanel();

        // Create and style navigation buttons
        homeButton = createStyledButton("Home");
        blogButton = createStyledButton("Blog");
        postButton = createStyledButton("Post");
        aboutButton = createStyledButton("About");
        loginButton = createStyledButton("Login");
        logoutButton = createStyledButton("Logout");

        // Add buttons to nav panel
        navPanel.add(homeButton);
        navPanel.add(blogButton);
        navPanel.add(postButton);
        navPanel.add(aboutButton);
        navPanel.add(loginButton);
        navPanel.add(logoutButton);

        // Add logo and navigation panels to the menu bar
        menuBar.add(logoPanel, BorderLayout.WEST);
        menuBar.add(navPanel, BorderLayout.EAST);

        // Add action listeners to the buttons
        homeButton.addActionListener(e -> {
            showPanel("Home");
            updateButtonColors(homeButton);
            updateFrameTitle("Home");
        });
        blogButton.addActionListener(e -> {
            showPanel("Blog");
            updateButtonColors(blogButton);
            updateFrameTitle("Blog");
        });
        postButton.addActionListener(e -> {
            showPanel("Post");
            updateButtonColors(postButton);
            updateFrameTitle("Post");
        });
        aboutButton.addActionListener(e -> {
            showPanel("About");
            updateButtonColors(aboutButton);
            updateFrameTitle("About");
        });
        loginButton.addActionListener(e -> {
            showPanel("Login");
            updateButtonColors(loginButton);
            updateFrameTitle("Login");
        });
        logoutButton.addActionListener(e -> {
            setLoggedInUser(null);
            showPanel("Home");
            updateButtonColors(homeButton);
            updateFrameTitle("Home");
        });

        return menuBar;
    }

    private static JPanel getNavPanel() {
        JPanel navPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.decode("#46BBF7"));
                int width = getWidth();
                int height = getHeight();
                int arcSize = 40;
                g2.fillRoundRect(0, 0, arcSize, height, arcSize, arcSize);
                g2.fillRect(arcSize / 2, 0, width - arcSize / 2, height);
            }
        };
        navPanel.setOpaque(false);
        navPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        return navPanel;
    }
    private static JPanel getLogoPanel() {
        JPanel logoPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Draw rounded rectangle on the right side
                g2.setColor(Color.decode("#46BBF7"));
                int arcSize = 40;
                g2.fillRoundRect(getWidth() - arcSize, 0, arcSize, getHeight(), arcSize, arcSize); // Right side with rounded corners
                g2.fillRect(0, 0, getWidth() - arcSize / 2, getHeight()); // Left side without rounded corners
            }
        };
        logoPanel.setOpaque(false);
        logoPanel.setPreferredSize(new Dimension(120, 80)); // Adjust the size to ensure visibility

        // Create the logo label with the resized image
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(Easy_Travel.class.getResource("/logo.png")));
        Image logoImage = logoIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setPreferredSize(new Dimension(120, 120));

        // Add the logo label to the center of the logo panel
        logoPanel.add(logoLabel, new GridBagConstraints());

        return logoPanel;
    }

    private JButton createStyledButton(String text) {
        Font lobsterFont = loadFont();
        if (lobsterFont == null) {
            lobsterFont = new Font("SansSerif", Font.ITALIC, 16);
        }
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 40));
        button.setFocusPainted(false);
        button.setFont(lobsterFont);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        return button;
    }

    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
        if ("Blog".equals(panelName)) {
            refreshBlogPanel();
        }
    }

    public void refreshBlogPanel() {
        Blog blogPanel = (Blog) getPanel("Blog");
        if (blogPanel != null) {
            blogPanel.loadBlogPosts();
        }
    }

    private JPanel getPanel(String panelName) {
        for (Component comp : contentPanel.getComponents()) {
            if (comp instanceof JPanel && panelName.equals(comp.getName())) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    private void updateButtonColors(JButton activeButton) {
        JButton[] buttons = {homeButton, blogButton, postButton, aboutButton, loginButton, logoutButton};
        for (JButton button : buttons) {
            if (button == activeButton) {
                button.setForeground(Color.BLUE);
            } else {
                button.setForeground(Color.BLACK);
            }
        }
    }

    public void updateFrameTitle(String title) {
        User user = getLoggedInUser();
        if (user == null) {
            frame.setTitle("Easy Travel - " + title);
        } else {
            frame.setTitle("Easy Travel - " + title + " (Logged in as " + user.getUsername() + ")");
        }
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        updateButtonVisibility();
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    private void updateButtonVisibility() {
        if (loggedInUser == null) {
            loginButton.setVisible(true);
            logoutButton.setVisible(false);
        } else {
            loginButton.setVisible(false);
            logoutButton.setVisible(true);
        }
    }

    private void adjustMenuItems() {
        int frameWidth = frame.getWidth();

        homeButton.setVisible(true);
        blogButton.setVisible(true);
        postButton.setVisible(true);
        aboutButton.setVisible(true);
        if (loggedInUser == null) {
            loginButton.setVisible(true);
            logoutButton.setVisible(false);
        } else {
            loginButton.setVisible(false);
            logoutButton.setVisible(true);
        }
    }

    private JMenuItem createMenuItemFromButton(JButton button) {
        JMenuItem menuItem = new JMenuItem(button.getText());
        menuItem.setPreferredSize(new Dimension(menuItem.getPreferredSize().width, 50)); // Set custom height
        for (java.awt.event.ActionListener al : button.getActionListeners()) {
            menuItem.addActionListener(al);
        }
        return menuItem;
    }

    public Font loadFont() {
        try (InputStream is = getClass().getResourceAsStream("/Lobster-Regular.ttf")) {
            return Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(Font.PLAIN, 20);
        } catch (FontFormatException | IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}