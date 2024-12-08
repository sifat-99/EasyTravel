package dream_team.easy_travel.AdminPanel;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.swing.Button;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

public class UploadRestaurants extends JDialog {
    // Components for the form
    private final JTextField titleField;
    private final JTextField restaurant1Field;
    private final JTextField price1Field;
    private final JTextField restaurant2Field;
    private final JTextField price2Field;
    private final JTextField restaurant3Field;
    private final JTextField price3Field;
    private final JTextField restaurant4Field;
    private final JTextField price4Field;

    public UploadRestaurants(JFrame parent) {
        // Set modal properties
        super(parent, "Upload Restaurant Data", true);
        setSize(500, 600);
        setLocationRelativeTo(parent);

        // Create a panel with gradient background
        final JPanel backgroundPanel = getJPanel();

        // Form panel for better alignment
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false); // Transparent to show the gradient background
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 5, 20, 5));

        // Initialize components
        JLabel titleLabel = createStyledLabel("Title:");
        titleLabel.setBounds(30, 50, 50, 30);
        titleField = new JTextField();
        titleField.setBounds(90, 50, 396, 30);

        JLabel restaurant1Label = createStyledLabel("Restaurants");
        restaurant1Field = new JTextField();
        restaurant1Label.setBounds(50, 100, 100, 30);
        restaurant1Field.setBounds(50, 140, 200, 30);
        JLabel price1Label = createStyledLabel("Prices");
        price1Label.setBounds(270, 100, 250, 30);

        price1Field = new JTextField();
        price1Field.setBounds(270, 140, 200, 30);

//        JLabel restaurant2Label = createStyledLabel("Restaurant 2:");
        restaurant2Field = new JTextField();
        restaurant2Field.setBounds(50, 180, 200, 30);
//        JLabel price2Label = createStyledLabel("Price 2:");
        price2Field = new JTextField();
        price2Field.setBounds(270, 180, 200, 30);

//        JLabel restaurant3Label = createStyledLabel("Restaurant 3:");
        restaurant3Field = new JTextField();
        restaurant3Field.setBounds(50, 220, 200, 30);
//        JLabel price3Label = createStyledLabel("Price 3:");
        price3Field = new JTextField();
        price3Field.setBounds(270, 220, 200, 30);

//        JLabel restaurant4Label = createStyledLabel("Restaurant 4:");
        restaurant4Field = new JTextField();
        restaurant4Field.setBounds(50, 260, 200, 30);
//        JLabel price4Label = createStyledLabel("Price 4:");
        price4Field = new JTextField();
        price4Field.setBounds(270, 260, 200, 30);

        JButton submitButton = createStyledButton("Submit", new Color(50, 205, 50)); // Green button
        JButton cancelButton = createStyledButton("Cancel", new Color(220, 20, 60)); // Red button

        // Set bounds for the buttons
        submitButton.setBounds(50, 320, 100, 30);
        cancelButton.setBounds(160, 320, 100, 30);


        // Add components to the form panel
        backgroundPanel.add(titleLabel);
        backgroundPanel.add(titleField);

        backgroundPanel.add(restaurant1Label);
        backgroundPanel.add(price1Label);
        backgroundPanel.add(restaurant1Field);

        backgroundPanel.add(price1Field);

//        backgroundPanel.add(restaurant2Label);
        backgroundPanel.add(restaurant2Field);
//        backgroundPanel.add(price2Label);
        backgroundPanel.add(price2Field);

//        backgroundPanel.add(restaurant3Label);
        backgroundPanel.add(restaurant3Field);
//        backgroundPanel.add(price3Label);
        backgroundPanel.add(price3Field);

//        backgroundPanel.add(restaurant4Label);
        backgroundPanel.add(restaurant4Field);
//        backgroundPanel.add(price4Label);
        backgroundPanel.add(price4Field);

        backgroundPanel.add(submitButton);
        backgroundPanel.add(cancelButton);

        backgroundPanel.add(formPanel);
        add(backgroundPanel);

        // Submit button action
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = titleField.getText();
                String restaurant1 = restaurant1Field.getText();
                String price1 = price1Field.getText();
                String restaurant2 = restaurant2Field.getText();
                String price2 = price2Field.getText();
                String restaurant3 = restaurant3Field.getText();
                String price3 = price3Field.getText();
                String restaurant4 = restaurant4Field.getText();
                String price4 = price4Field.getText();

                // Validate input
                if (title.isEmpty() || restaurant1.isEmpty() || price1.isEmpty() || restaurant2.isEmpty() ||
                        price2.isEmpty() || restaurant3.isEmpty() || price3.isEmpty() || restaurant4.isEmpty() || price4.isEmpty()) {
                    JOptionPane.showMessageDialog(UploadRestaurants.this, "All fields must be filled!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Process the data (e.g., insert into database)
                String query = "INSERT INTO restaurants (title, restaurant_1, price_1, restaurant_2, price_2, restaurant_3, price_3, restaurant_4, price_4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                // Execute the query
                //
                try {
                    Connection conn = ConnectDB.getConnection();
                    java.sql.PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, title);
                    stmt.setString(2, restaurant1);
                    stmt.setString(3, price1);
                    stmt.setString(4, restaurant2);
                    stmt.setString(5, price2);
                    stmt.setString(6, restaurant3);
                    stmt.setString(7, price3);
                    stmt.setString(8, restaurant4);
                    stmt.setString(9, price4);
                    stmt.executeUpdate();
                    // Show success message
                    JOptionPane.showMessageDialog(UploadRestaurants.this, "Data uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // Close the modal
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(UploadRestaurants.this, "Failed to upload data!", "Error", JOptionPane.ERROR_MESSAGE);
                }


            }
        });

        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the modal
            }
        });
    }

    public static JPanel getJPanel() {
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color gradientStart = new Color(173, 216, 230); // Light Blue
                Color gradientEnd = new Color(240, 248, 255);  // Alice Blue
                GradientPaint gradient = new GradientPaint(0, 0, gradientStart, 0, getHeight(), gradientEnd);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(null);
        return backgroundPanel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new Button();
        button.setText(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static void showUploadDialog(JFrame parent) {
        UploadRestaurants dialog = new UploadRestaurants(parent);
        dialog.setVisible(true);
    }
}
