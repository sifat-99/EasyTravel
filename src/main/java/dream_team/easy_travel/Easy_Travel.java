/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package dream_team.easy_travel;

/**
 *
 * @author sifat
 */
import dream_team.easy_travel.mainApp.AboutUsPanel;
import dream_team.easy_travel.mainApp.LoginPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public final class Easy_Travel {
    private final JFrame frame;
    private final JPanel contentPanel;
    private final JMenuBar menuBar;
    private final JButton homeButton;
    private final JButton aboutButton, contactButton;
    private final JMenu hamburgerMenu;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Easy_Travel::new);
    }

    public Easy_Travel() {
        frame = new JFrame("Easy Travel");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setBackground(Color.GRAY);


        // Create the menu bar
        menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);


        // Add the File menu to the menu bar
        menuBar.add(fileMenu);

        // Create navigation buttons
        homeButton = new JButton("Home");
        aboutButton = new JButton("About");
        contactButton = new JButton("Contact");
        JButton signIn = new JButton("Login");

        // Add buttons to the menu bar
        menuBar.add(homeButton);
        menuBar.add(aboutButton);
        menuBar.add(contactButton);
        menuBar.add(signIn);

        // Create the hamburger menu
        hamburgerMenu = new JMenu("☰");
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(hamburgerMenu);

        // Create the content panel
        contentPanel = new JPanel();
        contentPanel.setLayout(new CardLayout());
        contentPanel.setPreferredSize(new Dimension(800, 600));

        // Add different views to the content panel
        contentPanel.add(createHomePanel(), "Home");
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
                + contactButton.getPreferredSize().width;

        if (frameWidth < buttonsTotalWidth + 200) {
            homeButton.setVisible(false);
            aboutButton.setVisible(false);
            contactButton.setVisible(false);

            // Add buttons to the hamburger menu
            if (hamburgerMenu.getItemCount() == 0) {
                hamburgerMenu.add(createMenuItemFromButton(homeButton));
                hamburgerMenu.add(createMenuItemFromButton(aboutButton));
                hamburgerMenu.add(createMenuItemFromButton(contactButton));
            }
        } else {
            // Show buttons and hide hamburger menu
            homeButton.setVisible(true);
            aboutButton.setVisible(true);
            contactButton.setVisible(true);
            hamburgerMenu.removeAll();
        }
    }

    private JMenuItem createMenuItemFromButton(JButton button) {
        JMenuItem menuItem = new JMenuItem(button.getText());
        for (java.awt.event.ActionListener al : button.getActionListeners()) {
            menuItem.addActionListener(al);
        }
        return menuItem;
    }

    public JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.GRAY);
        panel.add(new JLabel("Welcome to the Home Page....!!"));
        return panel;
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
