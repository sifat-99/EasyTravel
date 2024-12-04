package dream_team.easy_travel.mainApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.Objects;
import javax.swing.*;
import dream_team.easy_travel.DatabaseConnection.ManageDatabase;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import dream_team.easy_travel.swing.MyPasswordField;
import dream_team.easy_travel.swing.MyTextField;
import net.miginfocom.swing.MigLayout;
import org.mindrot.jbcrypt.BCrypt;


public class PanelLoginAndRegister extends javax.swing.JLayeredPane {
    private boolean isPasswordVisible = false;
    public PanelLoginAndRegister(Easy_Travel app) {
        initComponents();
        initRegister(app);
        initLogin(app);
        login.setVisible(false);
        register.setVisible(true);
    }

    private void initRegister(Easy_Travel app) {
        register.setLayout(null); // Disable layout manager

        // Create Account Label
        JLabel label = new JLabel("Create Account");
        label.setFont(new Font("SansSerif", Font.BOLD, 30));
        label.setForeground(new Color(0, 0, 0));
        label.setBounds(290, 200, 300, 50);
        register.add(label);

        // Name TextField
        MyTextField txtUser = new MyTextField();
        txtUser.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/user.png"))));
        txtUser.setHint("Name");
        txtUser.setBounds(270, 270, 300, 50);
        register.add(txtUser);

        // Email TextField
        MyTextField txtEmail = new MyTextField();
        txtEmail.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/mail.png"))));
        txtEmail.setHint("Username");
       txtEmail.setToolTipText("Enter your unique username");
        txtEmail.setBounds(270, 340, 300, 50);
        register.add(txtEmail);

        // Password TextField
        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setPrefixIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/pass.png"))));
        txtPass.setHint("Password");
        txtPass.setToolTipText("Enter your password");
        txtPass.setBounds(270, 410, 300, 50);
        register.add(txtPass);

        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(new Color(223, 42, 42));
        errorLabel.setBounds(290, 460, 300, 20);
        register.add(errorLabel);


        // Sign Up Button
        Button cmd = new Button();
        cmd.setBackground(new Color(0, 0, 0));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN UP");
        cmd.setBounds(290, 500, 200, 50);
        register.add(cmd);

        // Action Listener for the Sign-Up Button
        cmd.addActionListener(e -> {
            String name = txtUser.getText();
            String username = txtEmail.getText();
            String password = new String(txtPass.getPassword());
            String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(register, "Please fill in all fields");
                return;
            }

            ManageDatabase db = new ManageDatabase();
            try {
                if (db.getUserByUsername(username) != null) {
//                    JOptionPane.showMessageDialog(register, "Username already exists");
                    errorLabel.setText("Username already exists");
                    return;
                }
                if(!username.matches("^[a-zA-Z0-9._-]{3,}$")){
//                    JOptionPane.showMessageDialog(register, "Username must be at least 3 characters long and can only contain letters, numbers, underscores and hyphens");
                    return;
                }
                if(!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")){
//                    JOptionPane.showMessageDialog(register, "Password must be at least 8 characters long and contain at least one digit, one uppercase letter, one lowercase letter, one special character and no whitespace");
                    if(password.length() < 8){
                        errorLabel.setText("Password must be at least 8 characters long");
                    }
                    if(!password.matches(".*[0-9].*")){
                        errorLabel.setText("Password must contain at least one digit");
                    }
                    if(!password.matches(".*[a-z].*")){
                        errorLabel.setText("Password must contain at least one lowercase letter");
                    }
                    if(!password.matches(".*[A-Z].*")){
                        errorLabel.setText("Password must contain at least one uppercase letter");
                    }
                    if(!password.matches(".*[@#$%^&+=].*")){
                        errorLabel.setText("Password must contain at least one special character");
                    }
                    if(password.matches(".*\\s.*")){
                        errorLabel.setText("Password must not contain any whitespace");
                    }
                    return;
                }
                db.addNewUser(name, username, encryptedPassword);
                JOptionPane.showMessageDialog(register, "User created successfully, Please login");
                register.setVisible(false);
                login.setVisible(true);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    private void initLogin(Easy_Travel app) {
        login.setLayout(null); // Disable layout managers

        // Sign In Label
        JLabel label = new JLabel("Sign In");
        label.setFont(new Font("SansSerif", Font.BOLD, 30));
        label.setForeground(new Color(9, 9, 9));
        label.setBounds(320, 245, 200, 50);
        login.add(label);

        // Email TextField
        MyTextField txtEmail = new MyTextField();
        txtEmail.setHint("Username");
        txtEmail.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Add border
        txtEmail.setBorder(BorderFactory.createCompoundBorder(txtEmail.getBorder(), BorderFactory.createEmptyBorder(0, 10, 0, 0))); // Add padding
        txtEmail.setBounds(290, 340, 200, 50);
        login.add(txtEmail);

        // Password Panel
        JPanel passwordPanel = new JPanel(null); // Disable layout manager for panel
        passwordPanel.setBackground(Color.WHITE);
        passwordPanel.setBounds(290, 397, 200, 50);
        passwordPanel.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1)); // Add border

        MyPasswordField txtPass = new MyPasswordField();
        txtPass.setHint("Password");
        txtPass.setBounds(5, 5, 160, 40); // Relative to the panel
        txtPass.setOpaque(false); // Make the password field transparent
        passwordPanel.add(txtPass);

        ImageIcon eyeOpenIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/eyeOpen.png"))).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
        ImageIcon eyeClosedIcon = new ImageIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("/eyeClosed.png"))).getImage().getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH));
        JButton btn = new Button();
        btn.setIcon(eyeClosedIcon);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBounds(160, 5, 40, 40); // Relative to the panel
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        passwordPanel.add(btn);

        login.add(passwordPanel);

        // Password visibility toggle
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    txtPass.setEchoChar((char) 0);
                    btn.setIcon(eyeOpenIcon);
                } else {
                    txtPass.setEchoChar('•');
                    btn.setIcon(eyeClosedIcon);
                }
            }
        });

        // Forgot Password Button
        JButton cmdForget = new JButton("Forgot your password ?");
        cmdForget.setForeground(new Color(100, 100, 100));
        cmdForget.setFont(new Font("SansSerif", Font.BOLD, 12));
        cmdForget.setContentAreaFilled(false);
        cmdForget.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cmdForget.setBounds(290, 470, 200, 30);
        login.add(cmdForget);

        cmdForget.addActionListener(e -> {
            JDialog popupForgotDialogue = createPopupForgotDialogue(app);
            SwingUtilities.invokeLater(() -> popupForgotDialogue.setVisible(true));
        });

        // Sign In Button
        Button cmd = new Button();
        cmd.setBackground(new Color(0, 0, 0));
        cmd.setForeground(new Color(250, 250, 250));
        cmd.setText("SIGN IN");
        cmd.setBounds(280, 570, 200, 50);
        login.add(cmd);

        cmd.addActionListener(e -> {
            JDialog loadingDialog = createLoadingDialog(app);
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    String username = txtEmail.getText();
                    String password = new String(txtPass.getPassword());
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Please fill in all fields");
                    } else {
                        try {
                            SwingUtilities.invokeLater(() -> loadingDialog.setVisible(true));
                            ManageDatabase db = new ManageDatabase();
                            User user = db.getUserByUsername(username);

                            if (BCrypt.checkpw(password, user.getPassword())) {
                                Thread.sleep(500);
                                SwingUtilities.invokeLater(() -> {
                                    loadingDialog.dispose();
                                    app.setLoggedInUser(user);
                                    app.showPanel("Home");
                                    app.updateFrameTitle("Home");
                                });
                            } else {
                                Thread.sleep(500);
                                SwingUtilities.invokeLater(() -> {
                                    loadingDialog.dispose();
                                    JOptionPane.showMessageDialog(null, "Invalid Username or Password");
                                });
                            }
                        } catch (Exception ex) {
                            System.err.println(ex);
                        } finally {
                            SwingUtilities.invokeLater(() -> {
                                try {
                                    Thread.sleep(500);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                                loadingDialog.dispose();
                            });
                        }
                    }
                    return null;
                }
            };
            worker.execute();
        });

        // Key Binding for Enter Key
        txtPass.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login");
        txtPass.getActionMap().put("login", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmd.doClick();
            }
        });
    }


    private JDialog createLoadingDialog(Easy_Travel app) {
        JFrame parentFrame = app.getFrame();
        JDialog loadingDialog = new JDialog(parentFrame, "Loading", true);
        loadingDialog.setUndecorated(true);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.setBackground(new Color(0, 0, 0, 0));

        ImageIcon loadingIcon = new ImageIcon(
                Objects.requireNonNull(getClass().getResource("/loader.gif"), "Image not found: /Loading.gif")
        );

        JLabel loadingLabel = new JLabel(loadingIcon);
        loadingLabel.setOpaque(false);

        loadingDialog.setSize(loadingIcon.getIconWidth(), loadingIcon.getIconHeight());
        loadingDialog.add(loadingLabel, BorderLayout.CENTER);
        loadingDialog.setLocationRelativeTo(parentFrame);
        loadingDialog.setAlwaysOnTop(true);

        return loadingDialog;
    }


    private JDialog createPopupForgotDialogue(Easy_Travel app) {
        JFrame parentFrame = app.getFrame();
        JDialog popupForgotDialogue = new JDialog(parentFrame, "Forgot Password", true);
        popupForgotDialogue.setUndecorated(true);
        popupForgotDialogue.setLayout(new BorderLayout());
        popupForgotDialogue.setBackground(Color.DARK_GRAY);

        // Main panel to hold components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Instruction label
        JLabel instructionLabel = new JLabel("Please enter your email address:");
        instructionLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        mainPanel.add(instructionLabel);

        // Spacing
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Email input field
        JTextField emailField = new JTextField();
        emailField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        emailField.setMaximumSize(new Dimension(250, 30));
        mainPanel.add(emailField);

        //ButtonPanel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.DARK_GRAY);

        // Reset button
        JButton resetButton = new JButton("Reset");
        resetButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        resetButton.addActionListener(e -> emailField.setText(""));
        buttonPanel.add(resetButton);

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("SansSerif", Font.PLAIN, 14));
        closeButton.addActionListener(e -> popupForgotDialogue.dispose());
        buttonPanel.add(closeButton);

        // Add main panel and button panel to dialog
        popupForgotDialogue.add(mainPanel, BorderLayout.CENTER);
        popupForgotDialogue.add(buttonPanel, BorderLayout.SOUTH);
        popupForgotDialogue.setSize(350, 180);
        popupForgotDialogue.setLocationRelativeTo(parentFrame);
        popupForgotDialogue.setAlwaysOnTop(true);

        return popupForgotDialogue;
    }




    public void showRegister(boolean show) {
        if (show) {
            register.setVisible(true);
            login.setVisible(false);
        } else {
            register.setVisible(false);
            login.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        login = new ImagePanel("/login.png");
        register = new ImagePanel("/signUp.jpg");

        setLayout(new java.awt.CardLayout());

        // Layout for login panel
        javax.swing.GroupLayout loginLayout = new javax.swing.GroupLayout(login);
        login.setLayout(loginLayout);
        loginLayout.setHorizontalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
        loginLayout.setVerticalGroup(
                loginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        add(login, "card3");

        // Layout for register panel
        javax.swing.GroupLayout registerLayout = new javax.swing.GroupLayout(register);
        register.setLayout(registerLayout);
        registerLayout.setHorizontalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 327, Short.MAX_VALUE)
        );
        registerLayout.setVerticalGroup(
                registerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 300, Short.MAX_VALUE)
        );

        add(register, "card2");
    }

    // Custom JPanel for displaying an image as the background
    public static class ImagePanel extends JPanel {
        private final Image backgroundImage;

        public ImagePanel(String imagePath) {
            // Load the image
            this.backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath))).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel login;
    private javax.swing.JPanel register;
    // End of variables declaration//GEN-END:variables
}
