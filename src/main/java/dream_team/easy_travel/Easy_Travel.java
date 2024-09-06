package dream_team.easy_travel;

import dream_team.easy_travel.mainApp.AboutUsPanel;
import dream_team.easy_travel.mainApp.HomePage;
import dream_team.easy_travel.mainApp.LoginPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Objects;

public final class Easy_Travel {
    private final JFrame frame;
    private final JPanel contentPanel;
    private final JButton homeButton;
    private final JButton aboutButton, contactButton;
    private final JMenu hamburgerMenu;
    private final JButton signIn;

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

        // Add the logo label to the menu bar
        menuBar.add(logoLabel);
        menuBar.add(Box.createHorizontalGlue()); // Pushes the buttons to the right

        // Create navigation buttons
        homeButton = new JButton("Home");
        homeButton.setPreferredSize(new Dimension(100, 100));
        aboutButton = new JButton("About");
        aboutButton.setPreferredSize(new Dimension(100, 80));
        contactButton = new JButton("Contact");
        contactButton.setPreferredSize(new Dimension(100, 80));
        signIn = new JButton("Login");
        signIn.setPreferredSize(new Dimension(100, 80));

        // Add buttons to the menu bar
        menuBar.add(homeButton);
        menuBar.add(aboutButton);
        menuBar.add(contactButton);
        menuBar.add(signIn);

        // Create the hamburger menu
        hamburgerMenu = new JMenu("\uD83D\uDCDC");
        menuBar.add(hamburgerMenu);

        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setPreferredSize(new Dimension(1200, 800));

        // Add different views to the content panel
        contentPanel.add(new HomePage(this), "Home");
        contentPanel.add(new AboutUsPanel(this), "About");
        contentPanel.add(createContactPanel(), "Contact");
        contentPanel.add(new LoginPanel(this), "Login");

        // Add action listeners to buttons
        homeButton.addActionListener(e -> showPanel("Home"));
        aboutButton.addActionListener(e -> showPanel("About"));
        contactButton.addActionListener(e -> showPanel("Contact"));
        signIn.addActionListener(e -> showPanel("Login"));

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

        frame.setVisible(true);
    }

    private void adjustMenuItems() {
        int frameWidth = frame.getWidth();

        int buttonsTotalWidth = homeButton.getPreferredSize().width + aboutButton.getPreferredSize().width
                + contactButton.getPreferredSize().width + signIn.getPreferredSize().width;

        if (frameWidth < buttonsTotalWidth + 200) {
            homeButton.setVisible(false);
            aboutButton.setVisible(false);
            contactButton.setVisible(false);
            signIn.setVisible(false);

            // Add buttons to the hamburger menu
            if (hamburgerMenu.getItemCount() == 0) {
                hamburgerMenu.add(createMenuItemFromButton(homeButton));
                hamburgerMenu.add(createMenuItemFromButton(aboutButton));
                hamburgerMenu.add(createMenuItemFromButton(contactButton));
                hamburgerMenu.add(createMenuItemFromButton(signIn));
            }
        } else {
            // Show buttons and hide hamburger menu
            homeButton.setVisible(true);
            aboutButton.setVisible(true);
            contactButton.setVisible(true);
            signIn.setVisible(true);
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
}