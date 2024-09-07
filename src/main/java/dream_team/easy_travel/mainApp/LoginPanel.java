package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ManageDatabase;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class LoginPanel extends JPanel {
    private final Image backgroundImage;
    private boolean isPasswordVisible = false;

    public LoginPanel(Easy_Travel app) {
        // Ensure the image path is correct
        String imagePath = "/LoginPageBG.png";
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath), "Image not found: " + imagePath)).getImage();

        setLayout(null);

        JLabel EmailField = new JLabel("Username:");
        EmailField.setForeground(Color.BLACK);
        EmailField.setFont(new Font("Arial", Font.BOLD, 16));
        PaddedTextField EmailInputField = new PaddedTextField(30);
        EmailInputField.setBackground(Color.decode("#46BBF7"));
        EmailInputField.setForeground(Color.BLACK);
        EmailInputField.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel PasswordLabel = new JLabel("Password:");
        PasswordLabel.setForeground(Color.BLACK);
        PasswordLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPasswordField passwordField = new JPasswordField(30);
        passwordField.setBorder(new EmptyBorder(5, 10, 5, 10));
        passwordField.setForeground(Color.BLACK);
        passwordField.setFont(new Font("Arial", Font.BOLD, 16));
        passwordField.setBackground(Color.decode("#46BBF7"));

        // Add eye icon to toggle password visibility
        JLabel viewText;
        try {
            viewText = new JLabel("View");
            } catch (NullPointerException e) {
            viewText = new JLabel("View");

        }
        viewText.setBounds(1110, 250, 30, 30);
        viewText.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        JLabel finalViewText = viewText;
        viewText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    passwordField.setEchoChar((char) 0);
                    finalViewText.setText("Hide");
                } else {
                    passwordField.setEchoChar('\u2022');
                    finalViewText.setText("View");
                }
            }
        });

        JButton loginButton = createCustomButton(Color.decode("#46BBF7"), new Font("Arial", Font.BOLD, 16));

        JLabel gotoSignUp = new JLabel("Don't have an account? Sign Up");

        // Set bounds for components
        EmailField.setBounds(800, 200, 120, 25);
        EmailInputField.setBounds(900, 200, 200, 30); // Adjust bounds for increased size
        PasswordLabel.setBounds(800, 250, 120, 25);
        passwordField.setBounds(900, 250, 200, 30); // Adjust bounds for increased size
        loginButton.setBounds(900, 300, 100, 50);
        gotoSignUp.setBounds(900, 350, 200, 25);
        viewText.setBounds(1110, 250, 30, 30);

        // Add components to the panel
        add(EmailField);
        add(EmailInputField);
        add(PasswordLabel);
        add(passwordField);
        add(viewText);
        add(loginButton);
        add(gotoSignUp);

        gotoSignUp.setForeground(Color.BLUE);
        gotoSignUp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        gotoSignUp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                app.showPanel("SignUp");
            }
        });

        loginButton.addActionListener(e -> {
            String username = EmailInputField.getText();
            String password = new String(passwordField.getPassword());
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields");
                return;
            } else {
                try {
                    ManageDatabase db = new ManageDatabase();
                    User user = db.getUserByUsername(username);
                    if (user != null && user.getPassword().equals(password)) {
                        JOptionPane.showMessageDialog(null, "Login Successful");
                        app.setLoggedInUser(user);
                        app.showPanel("Home");
                        app.updateFrameTitle("Home");
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                    }
                } catch (Exception ex) {
                    System.err.println(ex);
                }
            }
        });
    }
    private JButton createCustomButton(Color backgroundColor, Font font) {
        JButton button = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2.dispose();
            }
        };
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(font);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, 1200, 700, this);
    }
}