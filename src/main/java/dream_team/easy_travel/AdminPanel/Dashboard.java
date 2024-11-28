package dream_team.easy_travel.AdminPanel;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

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
        setLayout(new BorderLayout());
        setBackground(new Color(240, 248, 255)); // Light background color

        JLabel label = new JLabel("Admin Dashboard", JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setOpaque(true);
        label.setBackground(new Color(100, 149, 237)); // Cornflower Blue background
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the label
        add(label, BorderLayout.NORTH);

        // Table for displaying data
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "User Name", "Restaurant Name", "Amount", "Transaction ID", "Booking Date", "Action"}, 0
        );
        table = new JTable(tableModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 6) {
                    return new ActionRenderer();
                }
                return super.getCellRenderer(row, column);
            }
        };

        table.getColumnModel().getColumn(6).setCellEditor(new ActionEditor(this));

        table.setRowHeight(30);
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
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (isSelected) {
                    c.setBackground(new Color(173, 216, 230)); // Light blue for selected rows
                } else if (row % 2 == 0) {
                    c.setBackground(new Color(224, 255, 255)); // Light cyan for alternate rows
                } else {
                    c.setBackground(Color.WHITE); // White for other rows
                }
                c.setForeground(Color.BLACK); // Black text
                setBorder(BorderFactory.createLineBorder(new Color(135, 206, 250))); // Add border around cells
                return c;
            }
        };

        // Apply renderer to all columns
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(table);
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

                String userQuery = "SELECT name FROM users WHERE id = ?";
                PreparedStatement userStmt = connection.prepareStatement(userQuery);
                userStmt.setInt(1, userId);
                ResultSet userRs = userStmt.executeQuery();
                userRs.next();
                String userName = userRs.getString("name");

                tableModel.addRow(new Object[]{id, userName, restaurantName, amount, transactionId, bookingDate, "Pending"});
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
        public ActionRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER));
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column
        ) {
            removeAll();

            JButton approveButton = new JButton("Approve");
            JButton declineButton = new JButton("Decline");

            approveButton.setEnabled("Pending".equals(value));
            declineButton.setEnabled("Pending".equals(value));

            add(approveButton);
            add(declineButton);

            return this;
        }
    }

    private static class ActionEditor extends AbstractCellEditor implements TableCellEditor {
        private final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        private final JButton approveButton = new JButton("Approve");
        private final JButton declineButton = new JButton("Decline");
        private final Dashboard dashboard;
        private int selectedRow;

        public ActionEditor(Dashboard dashboard) {
            this.dashboard = dashboard;

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
            approveButton.setEnabled("Pending".equals(value));
            declineButton.setEnabled("Pending".equals(value));
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}
