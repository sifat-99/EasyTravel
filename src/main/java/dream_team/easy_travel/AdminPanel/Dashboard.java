package dream_team.easy_travel.AdminPanel;
import dream_team.easy_travel.DatabaseConnection.*;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import dream_team.easy_travel.swing.MyTextField;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Dashboard extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;

    public Dashboard(Easy_Travel easyTravel) throws SQLException {
        setLayout(null);
        setBackground(new Color(240, 248, 255)); // Light background color

        JButton label = new Button();
        label.setText("Booking Requests");
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setBounds(300, 20, 200, 50);
        label.setOpaque(false); // Transparent background
        label.setBackground(new Color(100, 149, 237)); // Cornflower Blue background
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the label
        add(label, BorderLayout.NORTH);

        JTextField searchField = new MyTextField();
        searchField.setBounds(550, 20, 200, 50);
        searchField.setBorder(BorderFactory.createLineBorder(new Color(47, 103, 198), 2, true));
        searchField.setBorder(BorderFactory.createCompoundBorder(searchField.getBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5))); // Padding inside the text field
        add(searchField);

        JButton searchButton = new Button();
        searchButton.setText("Search");
        searchButton.setBounds(770, 20, 100, 50);
        searchButton.addActionListener(e -> searchTableData(searchField.getText()));
        add(searchButton);


        // Table for displaying data
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "User Name", "Restaurant Name", "Amount", "Transaction ID", "Booking Date", "Status", "Action"}, 0
        );
        label.addActionListener(e -> {
            tableModel.setRowCount(0); // Clear existing rows
            loadTableData(); // Reload data into the table
        });
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 7) {
                    return new ActionRenderer();
                }
                return super.getCellRenderer(row, column);
            }

            @Override
            public TableCellEditor getCellEditor(int row, int column) {
                if (column == 7) {
                    return new ActionEditor(Dashboard.this);
                }
                return super.getCellEditor(row, column);
            }
        };

        table.setRowHeight(80);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(135, 206, 250));
        table.getTableHeader().setForeground(Color.BLACK);

        // Custom renderer for row styling
      DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
    ) {
        JTextArea textArea = new JTextArea(value != null ? value.toString() : "");
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createLineBorder(new Color(135, 206, 250)));

        if (isSelected) {
            textArea.setBackground(new Color(173, 216, 230)); // Light blue for selected rows
        } else if (row % 2 == 0) {
            textArea.setBackground(new Color(224, 255, 255)); // Light cyan for alternate rows
        } else {
            textArea.setBackground(Color.WHITE); // White for other rows
        }
        textArea.setForeground(Color.BLACK); // Black text

        return textArea;
    }
};
        // Apply renderer to all columns except the action column
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);

        }



        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 100, 1100, 500);
        add(scrollPane, BorderLayout.CENTER);

        // Load data into the table
        loadTableData();
    }

    private void loadTableData() {
        try {
            Connection connection = ConnectDB.getConnection();
            String query = "SELECT * FROM bookedUsersPayment";

            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int userId = rs.getInt("user_id");
                String restaurantName = rs.getString("restaurant_name");
                double amount = rs.getDouble("amount");
                String transactionId = rs.getString("transaction_id");
                String bookingDate = rs.getString("booking_date");
                String status = rs.getString("status");

                String userQuery = "SELECT name FROM users WHERE id = ?";
                PreparedStatement userStmt = connection.prepareStatement(userQuery);
                userStmt.setInt(1, userId);
                ResultSet userRs = userStmt.executeQuery();
                userRs.next();
                String userName = userRs.getString("name");

                tableModel.addRow(new Object[]{id, userName, restaurantName, amount, transactionId, bookingDate, status, ""});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void approveSelectedBooking(Object bookingIdObj) {
        int bookingId = (int) bookingIdObj;
        try {
            Connection connection = ConnectDB.getConnection();
            String query = "UPDATE bookedUsersPayment SET status = 'Approved' WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();

            int selectedRow = findRowById(bookingId);
            tableModel.setValueAt("Approved", selectedRow, 6);
            JOptionPane.showMessageDialog(this, "Booking Approved!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void declineSelectedBooking(Object bookingIdObj) {
        int bookingId = (int) bookingIdObj;
        try {
            Connection connection = ConnectDB.getConnection();
            String query = "UPDATE bookedUsersPayment SET status = 'Declined' WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();

            int selectedRow = findRowById(bookingId);
            tableModel.setValueAt("Declined", selectedRow, 6);
            JOptionPane.showMessageDialog(this, "Booking Declined!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int findRowById(int bookingId) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((int) tableModel.getValueAt(i, 0) == bookingId) {
                return i;
            }
        }
        return -1;
    }

    private static class ActionRenderer extends JPanel implements TableCellRenderer {
        private final JButton approveButton = new Button();
        private final JButton declineButton = new Button();

        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));

            approveButton.setText("Approve");
            declineButton.setText("Decline");

            // Styling the buttons
            approveButton.setBackground(new Color(88, 213, 88)); // Lime green for Approve
            approveButton.setForeground(Color.WHITE);
            declineButton.setBackground(new Color(220, 87, 38)); // Red for Decline
            declineButton.setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
        ) {
            removeAll();

            String status = (String) table.getValueAt(row, 6);

            // Enable or disable buttons based on status
            approveButton.setEnabled("Pending".equals(status));
            declineButton.setEnabled("Approved".equals(status));
            approveButton.setPreferredSize(new Dimension(100, 30));
            declineButton.setPreferredSize(new Dimension(100, 30));

            add(approveButton);
            add(declineButton);

            return this;
        }
    }

    private static class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        private final JButton approveButton = new Button();
        private final JButton declineButton = new Button();
        private int selectedRow;

        public ActionEditor(Dashboard dashboard) {
            approveButton.setText("Approve");
            declineButton.setText("Decline");
            approveButton.setPreferredSize(new Dimension(100, 30));
            declineButton.setPreferredSize(new Dimension(100, 30));

            // Styling the buttons
            approveButton.setBackground(new Color(50, 205, 50)); // Lime green for Approve
            approveButton.setForeground(Color.WHITE);
            declineButton.setBackground(new Color(255, 69, 0)); // Red for Decline
            declineButton.setForeground(Color.WHITE);
            approveButton.addActionListener(e -> {
                dashboard.approveSelectedBooking(dashboard.tableModel.getValueAt(selectedRow, 0));
                stopCellEditing();
            });
            declineButton.addActionListener(e -> {
                dashboard.declineSelectedBooking(dashboard.tableModel.getValueAt(selectedRow, 0));
                stopCellEditing();
            });
            panel.add(approveButton);
            panel.add(declineButton);
        }
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            selectedRow = row;

            String status = (String) table.getValueAt(row, 6);

            // Enable or disable buttons based on status
            approveButton.setEnabled("Pending".equals(status));
            declineButton.setEnabled("Approved".equals(status));

            return panel;
        }
        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }


private void searchTableData(String query) {
    tableModel.setRowCount(0); // Clear existing rows
    try {
        Connection connection = ConnectDB.getConnection();
        String sql = "SELECT * FROM bookedUsersPayment WHERE " +
                     "restaurant_name LIKE ? OR transaction_id LIKE ? OR status LIKE ?";
        PreparedStatement stmt = connection.prepareStatement(sql);
        String searchQuery = "%" + query + "%";
        stmt.setString(1, searchQuery);
        stmt.setString(2, searchQuery);
        stmt.setString(3, searchQuery);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            int userId = rs.getInt("user_id");
            String restaurantName = rs.getString("restaurant_name");
            double amount = rs.getDouble("amount");
            String transactionId = rs.getString("transaction_id");
            String bookingDate = rs.getString("booking_date");
            String status = rs.getString("status");

            String userQuery = "SELECT name FROM users WHERE id = ?";
            PreparedStatement userStmt = connection.prepareStatement(userQuery);
            userStmt.setInt(1, userId);
            ResultSet userRs = userStmt.executeQuery();
            userRs.next();
            String userName = userRs.getString("name");

            tableModel.addRow(new Object[]{id, userName, restaurantName, amount, transactionId, bookingDate, status, ""});
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
}
