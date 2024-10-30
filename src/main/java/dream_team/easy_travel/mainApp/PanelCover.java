package dream_team.easy_travel.mainApp;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import dream_team.easy_travel.swing.ButtonOutLine;
import net.miginfocom.swing.MigLayout;

public class PanelCover extends javax.swing.JPanel {

    private final DecimalFormat df = new DecimalFormat("##0.###", DecimalFormatSymbols.getInstance(Locale.US));
    private ActionListener event;
    private MigLayout layout;
    private JLabel title;
    private JLabel description;
    private JLabel description1;
    private ButtonOutLine button;
    private boolean isLogin;
    private Image backgroundImage;

    public PanelCover() {
//        initComponents();
        setOpaque(false);
        layout = new MigLayout("wrap, h 20%", "left", "push[]10[]5[]5[]push");
        setLayout(layout);
        init();
        // Load the background image
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/LoginBG.gif"),"Image not found")).getImage();
    }

    private void init() {
        title = new JLabel("Welcome Back!");
        title.setFont(new java.awt.Font("SansSerif", java.awt.Font.BOLD, 30));
        title.setForeground(new java.awt.Color(0, 0, 0));
        add(title);
        description = new JLabel("To keep connected with us please");
        description.setForeground(new java.awt.Color(4, 4, 4));
        add(description);
        description1 = new JLabel("login with your personal info");
        description1.setForeground(new java.awt.Color(0, 0, 0));
        add(description1);
        button = new ButtonOutLine();
        button.setBackground(new java.awt.Color(0, 0, 0));
        button.setForeground(new java.awt.Color(0, 0, 0));
        button.setText("SIGN IN");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                event.actionPerformed(ae);
            }
        });
        add(button, "w 60%, h 40");
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        Graphics2D g2 = (Graphics2D) grphcs;
        // Draw the background image
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        super.paintComponent(grphcs);
    }

    public void addEvent(ActionListener event) {
        this.event = event;
    }

    public void registerLeft(double v) {
        v = Double.valueOf(df.format(v));
        login(false);
        layout.setComponentConstraints(title, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description1, "pad 0 -" + v + "% 0 0");
    }

    public void registerRight(double v) {
        v = Double.valueOf(df.format(v));
        login(false);
        layout.setComponentConstraints(title, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description, "pad 0 -" + v + "% 0 0");
        layout.setComponentConstraints(description1, "pad 0 -" + v + "% 0 0");
    }

    public void loginLeft(double v) {
        v = Double.valueOf(df.format(v));
        login(true);
        layout.setComponentConstraints(title, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description1, "pad 0 " + v + "% 0 " + v + "%");
    }

    public void loginRight(double v) {
        v = Double.valueOf(df.format(v));
        login(true);
        layout.setComponentConstraints(title, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description, "pad 0 " + v + "% 0 " + v + "%");
        layout.setComponentConstraints(description1, "pad 0 " + v + "% 0 " + v + "%");
    }

    public void login(boolean login) {
        if (this.isLogin != login) {
            if (login) {
                title.setText("Hello, Friend!");
                description.setText("Enter your personal details");
                description1.setText("and start journey with us");
                button.setText("SIGN UP");
            } else {
                title.setText("Welcome Back!");
                description.setText("To keep connected with us please");
                description1.setText("login with your personal info");
                button.setText("SIGN IN");
            }
            this.isLogin = login;
        }
    }

    // Variables declaration - do not modify
    // End of variables declaration
}