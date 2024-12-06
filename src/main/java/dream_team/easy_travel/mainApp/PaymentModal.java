package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PaymentModal extends JFrame {
    public int userId;
    public PaymentModal(String restaurantName, String restaurantTable, String restaurantPrice, Easy_Travel app) {
        setSize(400, 400);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Payment");
        JLabel restaurantNameLabel = new JLabel("Restaurant: " + restaurantName);
        restaurantNameLabel.setBounds(10, 50, 350, 30);
        add(restaurantNameLabel);
        JLabel tableLabel = new JLabel("Table: " + restaurantTable);
        tableLabel.setBounds(50, 100, 300, 30);
        add(tableLabel);
        JLabel amountLabel = new JLabel("Amount: ");
        amountLabel.setBounds(50, 150, 100, 30);
        add(amountLabel);
        JTextField amountField = new JTextField(restaurantPrice);
        amountField.setBounds(150, 150, 100, 30);
        add(amountField);
        JLabel enterCardLabel = new JLabel("Enter Card Details");
        enterCardLabel.setBounds(50, 190, 150, 30);
        add(enterCardLabel);
        JTextField cardNumber = new JTextField();
        cardNumber.setBounds(165, 190, 150, 30);
        add(cardNumber);
        JButton payButton = new Button();
        payButton.setText("Pay");
        payButton.setBounds(150, 270, 100, 30);
        add(payButton);


        payButton.addActionListener(e -> {
            if(app.getLoggedInUser() == null){
                JOptionPane.showMessageDialog(this, "Please login first");
                app.showPanel("LoginRunner");
            }
            if(cardNumber.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Please enter card number");
                return;
            }
            else{
                Connection conn = null;
                try {
                    conn = ConnectDB.getConnection();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String query = "INSERT INTO bookedUsersPayment (user_id, restaurant_name, amount,transaction_id,status) VALUES (?, ?, ?,?,?)";
                String getUserId = "SELECT id FROM users WHERE username = ?";
                String transactionId = UUID.randomUUID().toString();

                try {
                    PreparedStatement stmt = conn.prepareStatement(getUserId);
                    stmt.setString(1, app.getLoggedInUser().getUsername());
                    ResultSet rs = stmt.executeQuery();
                    if(rs.next()){
                        userId = rs.getInt("id");
                    }
                } catch (SQLException throwable) {
                    throwable.printStackTrace();
                }




                try {
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setInt(1, userId);
                    stmt.setString(2, restaurantName);
                    stmt.setString(3, restaurantPrice);
                    stmt.setString(4, transactionId);
                    stmt.setString(5, "Pending");
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Payment Successful");
                    this.dispose();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });



    }
}
