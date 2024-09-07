package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Blog extends JPanel {

    public Blog(Easy_Travel app) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1200, 750));

        ImageIcon imageIcon = loadImageIcon();
        if (imageIcon == null) {
            throw new RuntimeException("Failed to load image: /HomeBG.png");
        }

        JLabel imageLabel = new JLabel(imageIcon);
        imageLabel.setBounds(0, 0, 1200, 750);
        layeredPane.add(imageLabel, Integer.valueOf(0));

        // Create a panel with GridLayout(0, 2) to allow 2 cards per row and vertical growth
        JPanel cardPanel = new JPanel(new GridLayout(0, 2, 10, 10)); // 0 rows, 2 columns, 10px gap
        cardPanel.setOpaque(false); // Make sure the panel is transparent so the background image is visible

        // Add cards with titles and descriptions
        for (int i = 1; i <= 8; i++) {
            String title = "Card Title " + i;
            String description = "This is a description for Card " + i + ". It contains some example text.";
            JPanel card = createCard(title, description);
            cardPanel.add(card);
        }

        // Wrap the cardPanel inside a scrollable container
        JScrollPane scrollPane = new JScrollPane(cardPanel);
        scrollPane.setBounds(50, 50, 1100, 600); // Adjust size and position of scroll pane
        scrollPane.setOpaque(false); // Make scroll pane background transparent
        scrollPane.getViewport().setOpaque(false); // Make viewport background transparent
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // Always show vertical scrollbar
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER); // Disable horizontal scroll

        // Add the scroll pane as the top layer
        layeredPane.add(scrollPane, Integer.valueOf(1));

        add(layeredPane, BorderLayout.CENTER); // Add the layered pane to the main panel
    }

    private ImageIcon loadImageIcon() {
        try {
            ImageIcon icon = new ImageIcon(Objects.requireNonNull(this.getClass().getResource("/HomeBG.png")));
            Image image = icon.getImage().getScaledInstance(1200, 750, Image.SCALE_SMOOTH);
            return new ImageIcon(image);
        } catch (NullPointerException e) {
            System.err.println("Resource not found: " + "/HomeBG.png");
            return null;
        }
    }

    // Create a card with a border, title, and description
    private JPanel createCard(String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setOpaque(false); // Make the card background transparent
        card.setPreferredSize(new Dimension(500, 400)); // Set the fixed card height and width
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.BLACK),
                title, TitledBorder.CENTER, TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), Color.BLUE)
        );

        // Add card content
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLUE);


        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 12));

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(descriptionArea, BorderLayout.CENTER);

        // Add mouse click event to the card
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println(title + " clicked");
                // Add your click event logic here
            }
        });

        return card;
    }
}
