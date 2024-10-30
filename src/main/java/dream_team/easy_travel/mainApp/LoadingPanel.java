package dream_team.easy_travel.mainApp;

import javax.swing.*;
import java.awt.*;

public class LoadingPanel extends JLayeredPane {
    public LoadingPanel() {
        initComponents();
        setLayer(this, JLayeredPane.POPUP_LAYER);
    }

    private void initComponents() {
        setLayout(new java.awt.BorderLayout());

        javax.swing.JLabel jLabel1 = new javax.swing.JLabel();
        jLabel1.setFont(new java.awt.Font("Tahoma", Font.BOLD, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Loading...");
        add(jLabel1, java.awt.BorderLayout.CENTER);
    }
}
