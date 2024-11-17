package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.Easy_Travel;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.Objects;

public class AboutUsPanel extends JPanel {
    public AboutUsPanel(Easy_Travel app) {
        setLayout(null);

        // Gradient background
        setOpaque(false);

        JLabel title = new JLabel("About Us");
        title.setBounds(100, 50, 200, 30);
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(new Color(0, 51, 102)); // Dark blue text
        add(title);

        JTextPane aboutUs = new JTextPane();
        aboutUs.setText("Easy Travel is a travel agency that provides the best travel experience to its customers. We provide the best travel packages at the best price. We have a team of experienced professionals who are always ready to help you. We have a wide range of travel packages to choose from. We also provide 24/7 customer support. So, what are you waiting for? Book your travel package now and enjoy the best travel experience of your life.");
        aboutUs.setBounds(100, 100, 1000, 200);
        aboutUs.setFont(new Font("Arial", Font.PLAIN, 18));
        aboutUs.setEditable(false);
        aboutUs.setOpaque(false);
        StyledDocument doc = aboutUs.getStyledDocument();
        SimpleAttributeSet left = new SimpleAttributeSet();
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_JUSTIFIED);
        doc.setParagraphAttributes(0, doc.getLength(), left, false);
        add(aboutUs);

      JLabel developersLabel = new JLabel("<html><u>Developers</u></html>");
developersLabel.setBounds(500, 220, 200, 30);
developersLabel.setFont(new Font("Arial", Font.BOLD, 34));
developersLabel.setForeground(new Color(0, 51, 102)); // Dark blue text
add(developersLabel);


        String[] developers = {
                "MD Abdur Rahman Sifat   - Department of CSE - ID: 22235103364",
                "Kanis Fatema            - Department of CSE - ID: 22235103406",
                "Myimuna Akter Suborna   - Department of CSE - ID: 22235103395",
                "Labiba Liaqute          - Department of CSE - ID: 22235103379",
                "Rabeya Akter Sadia      - Department of CSE - ID: 22235103370"
        };

        JPanel developersPanel = new JPanel();
        developersPanel.setLayout(new GridLayout(developers.length, 3));  // 3 columns: Name, Department, ID
        developersPanel.setBounds(100, 260, 1000, 150);
        developersPanel.setBackground(new Color(255, 255, 255, 0)); // Match panel background

        for (String developer : developers) {
            String[] parts = developer.split(" - ");  // Split by " - "

            // Ensure the labels are properly aligned
            JLabel nameLabel = new JLabel(parts[0].trim());
            nameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            nameLabel.setHorizontalAlignment(SwingConstants.LEFT);  // Align to the left

            JLabel departmentLabel = new JLabel(parts[1].trim());
            departmentLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            departmentLabel.setHorizontalAlignment(SwingConstants.LEFT);  // Align to the left

            JLabel idLabel = new JLabel(parts[2].trim());
            idLabel.setFont(new Font("Arial", Font.PLAIN, 18));
            idLabel.setHorizontalAlignment(SwingConstants.LEFT);  // Align to the left

            developersPanel.add(nameLabel);
            developersPanel.add(departmentLabel);
            developersPanel.add(idLabel);
        }

        add(developersPanel);



        CustomButton backButton = new CustomButton("Back", 10);
        backButton.setBounds(100, 600, 100, 30);
        backButton.addActionListener(e -> app.showPanel("Home"));
        add(backButton);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        Color color1 = new Color(255, 255, 255); // Light blue
        Color color2 = new Color(173, 216, 230); // Soft blue
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, width, height);
    }
}