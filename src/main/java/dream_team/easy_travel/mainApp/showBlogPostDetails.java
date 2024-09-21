package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class showBlogPostDetails extends JPanel {
    public showBlogPostDetails(int BlogId, Easy_Travel app) {
        JLabel title = new JLabel("Title");
        title.setBounds(100, 50, 100, 30);
        add(title);

    }



}
