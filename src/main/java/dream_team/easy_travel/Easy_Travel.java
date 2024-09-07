package dream_team.easy_travel;

import dream_team.easy_travel.mainApp.AboutUsPanel;
import dream_team.easy_travel.mainApp.Blog;
import dream_team.easy_travel.mainApp.HomePage;
import dream_team.easy_travel.mainApp.LoginPanel;
import dream_team.easy_travel.mainApp.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public final class Easy_Travel {
    private final JFrame frame;
    private final JPanel contentPanel;
    private final JButton homeButton;
    private final JButton blogButton, postButton;
    private final JMenu hamburgerMenu;
    private final JButton aboutButton;
    private final JButton loginButton;
    private final JButton logoutButton;
    private User loggedInUser;

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

        // Create the menu bar
        JMenuBar menuBar = new JMenuBar();
        menuBar.setPreferredSize(new Dimension(menuBar.getPreferredSize().width, 50));
        menuBar.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));
        // Load and resize the logo image
        ImageIcon logoIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.png")));
        Image logoImage = logoIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(logoImage);

        // Create the logo label with the resized image
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setPreferredSize(new Dimension(50, 50));
        logoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        logoLabel.setVerticalAlignment(SwingConstants.CENTER);

        menuBar.add(logoLabel);
        menuBar.add(Box.createHorizontalGlue());

        // Create navigation buttons
        homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(100, 100));
        blogButton = new JButton("Blog");
        blogButton.setPreferredSize(new Dimension(100, 80));
        postButton = new JButton("Post");
        postButton.setPreferredSize(new Dimension(100, 80));
        aboutButton = new JButton("About");
        aboutButton.setPreferredSize(new Dimension(100, 80));
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(100, 80));
        logoutButton = new JButton("Logout");
        logoutButton.setPreferredSize(new Dimension(100, 80));

        // Add buttons to the menu bar
        menuBar.add(homeButton);
        menuBar.add(blogButton);
        menuBar.add(postButton);
        menuBar.add(aboutButton);
        menuBar.add(loginButton);
        menuBar.add(logoutButton);
        logoutButton.setVisible(loggedInUser != null);

        // Create the hamburger menu
        hamburgerMenu = new JMenu("\uD83D\uDCDC");
        menuBar.add(hamburgerMenu);

        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setPreferredSize(new Dimension(1200, 800));

        // Add different views to the content panel
        contentPanel.add(new HomePage(this), "Home");
        contentPanel.add(createContactPanel(), "Post");
        contentPanel.add(new AboutUsPanel(this), "About");
        contentPanel.add(new Blog(this), "Blog");
        contentPanel.add(new LoginPanel(this), "Login");

        // Add action listeners to buttons
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
            updateButtonVisibility();
            showPanel("Home");
            updateButtonColors(homeButton);
            updateFrameTitle("Home");
        });

        // Set up the frame layout
        frame.setLayout(new BorderLayout());
        frame.setJMenuBar(menuBar);
        frame.add(contentPanel, BorderLayout.CENTER);

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

    private void adjustMenuItems() {
        int frameWidth = frame.getWidth();

        int buttonsTotalWidth = homeButton.getPreferredSize().width + blogButton.getPreferredSize().width
                + postButton.getPreferredSize().width + aboutButton.getPreferredSize().width
                + loginButton.getPreferredSize().width + logoutButton.getPreferredSize().width;

        if (frameWidth < buttonsTotalWidth + 200) {
            homeButton.setVisible(false);
            blogButton.setVisible(false);
            postButton.setVisible(false);
            aboutButton.setVisible(false);
            loginButton.setVisible(false);
            logoutButton.setVisible(false);

            // Add buttons to the hamburger menu
            if (hamburgerMenu.getItemCount() == 0) {
                hamburgerMenu.add(createMenuItemFromButton(homeButton));
                hamburgerMenu.add(createMenuItemFromButton(blogButton));
                hamburgerMenu.add(createMenuItemFromButton(postButton));
                hamburgerMenu.add(createMenuItemFromButton(aboutButton));
                hamburgerMenu.add(createMenuItemFromButton(loginButton));
                hamburgerMenu.add(createMenuItemFromButton(logoutButton));
            }
        } else {
            // Show buttons and hide hamburger menu
            homeButton.setVisible(true);
            blogButton.setVisible(true);
            postButton.setVisible(true);
            aboutButton.setVisible(true);
            loginButton.setVisible(true);
            if (loggedInUser != null) {
                logoutButton.setVisible(true);
            }
            hamburgerMenu.removeAll();
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

    private JPanel createContactPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.GRAY);
        panel.add(new JLabel("Contact Us"));
        return panel;
    }

    public void showPanel(String panelName) {
        CardLayout cl = (CardLayout) (contentPanel.getLayout());
        cl.show(contentPanel, panelName);
    }

    private void updateButtonColors(JButton activeButton) {
        JButton[] buttons = {homeButton, blogButton, postButton, aboutButton, loginButton, logoutButton};
        for (JButton button : buttons) {
            if (button == activeButton) {
                button.setBackground(Color.LIGHT_GRAY);
            } else {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        }
    }

    public void updateFrameTitle(String title) {
        User user = getLoggedInUser();
        if (user == null) {
            frame.setTitle("Easy Travel - " + title);
        } else {
            frame.setTitle("Easy Travel" + title + " - " + user.getUsername());
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
}