package dream_team.easy_travel.mainApp;

import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;

public class ChooseYourDesirePlace extends JPanel {
    public Easy_Travel app;
    private JPanel placesPanel;
    private JTextField searchField;

    public ChooseYourDesirePlace(Easy_Travel app) {
        setLayout(new BorderLayout());
        initializeComponents();
        this.app = app;
        fetchPlacesWithRestaurants("");  // Initially fetch all places
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Define the gradient colors
        Color startColor = new Color(135, 206, 250); // Light Sky Blue
        Color endColor = new Color(70, 130, 180); // Steel Blue

        // Create a gradient from top to bottom
        int width = getWidth();
        int height = getHeight();
        GradientPaint gradient = new GradientPaint(0, 0, startColor, 0, height, endColor);

        // Fill the background with the gradient
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
    }

    private void initializeComponents() {
        // Search Panel
        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false); // Make the search panel background transparent
        searchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(30);
        searchField.setText("Search...");
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Trigger search when Enter is pressed
                fetchPlacesWithRestaurants(searchField.getText().trim());
            }
        });
        searchPanel.add(searchField);

        // Adding search panel at the top
        add(searchPanel, BorderLayout.NORTH);

        // Panel to hold places and restaurants
        placesPanel = new JPanel();
        placesPanel.setLayout(new BoxLayout(placesPanel, BoxLayout.Y_AXIS));
        placesPanel.setBackground(new Color(255, 255, 255, 0)); // Transparent background
        placesPanel.setOpaque(true); // Make the places panel background transparent
        JScrollPane scrollPane = new JScrollPane(placesPanel);
        scrollPane.getViewport().setBackground(new Color(255, 255, 255, 0)); // Transparent background
        scrollPane.setOpaque(true);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI());
        add(scrollPane, BorderLayout.CENTER);
    }

    private void fetchPlacesWithRestaurants(String searchTerm) {
        String query = """
                SELECT id AS place_id, 
                       title, 
                       restaurant_1, price_1, 
                       restaurant_2, price_2, 
                       restaurant_3, price_3, 
                       restaurant_4, price_4 
                FROM restaurants
                WHERE title LIKE ? OR restaurant_1 LIKE ? OR restaurant_2 LIKE ? OR restaurant_3 LIKE ? OR restaurant_4 LIKE ?
                """;

        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Setting the search term for each relevant column
            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                placesPanel.removeAll(); // Clear existing data before displaying new search results

                while (rs.next()) {
                    int placeId = rs.getInt("place_id");
                    String placeName = rs.getString("title");

                    // Create a panel for each place
                    JPanel placePanel = new JPanel();
                    placePanel.setOpaque(false); // Transparent background
                    placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.Y_AXIS));

                    placePanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.BLACK),
                            "Place ID: " + placeId + " - " + placeName
                    ));

                    placePanel.setPreferredSize(new Dimension(placesPanel.getWidth(), 200));
                    placePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

                    // Add a table for restaurants within the place panel
                    DefaultTableModel tableModel = new DefaultTableModel(new String[]{
                            "Restaurant", "Price", "Action"
                    }, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            return column == 2; // Only the "Action" column is editable for buttons
                        }
                    };

                    JTable restaurantTable = new JTable(tableModel);

                    restaurantTable.setRowHeight(30);

                    // Custom renderer for the "Action" column - adding a button
                    restaurantTable.getColumn("Action").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JButton button = new JButton("Book");
                        button.addActionListener(e -> handleBooking(row, placeName));  // pass the row index and place name
                        return button;
                    });

                    // Custom editor for the "Action" column - make the button interactive
                    restaurantTable.getColumn("Action").setCellEditor(new TableCellEditor() {
                        @Override
                        public Object getCellEditorValue() {
                            return null;  // No actual value to store since we're using a button
                        }

                        @Override
                        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                            JButton button = new JButton("Book");
                            button.addActionListener(e -> handleBooking(row, placeName));  // Trigger booking on click
                            return button;
                        }

                        @Override
                        public boolean isCellEditable(EventObject anEvent) {
                            return true;  // Allow editing (button click)
                        }

                        @Override
                        public boolean shouldSelectCell(EventObject anEvent) {
                            return false;
                        }

                        @Override
                        public boolean stopCellEditing() {
                            return false;
                        }

                        @Override
                        public void cancelCellEditing() {

                        }

                        @Override
                        public void addCellEditorListener(CellEditorListener l) {

                        }

                        @Override
                        public void removeCellEditorListener(CellEditorListener l) {

                        }
                    });

                    for (int i = 1; i <= 4; i++) {
                        String restaurantColumn = "restaurant_" + i;
                        String priceColumn = "price_" + i;

                        String restaurantName = rs.getString(restaurantColumn);
                        double price = rs.getDouble(priceColumn);

                        if (restaurantName != null && !restaurantName.trim().isEmpty()) {
                            tableModel.addRow(new Object[]{restaurantName, price, "Book"});
                        }
                    }

                    // Add the restaurant table to the place panel
                    JScrollPane tableScrollPane = new JScrollPane(restaurantTable);
                    placePanel.add(tableScrollPane);
                    placesPanel.add(placePanel);
                }

                placesPanel.revalidate();
                placesPanel.repaint();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch place and restaurant data.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBooking(int row, String placeName) {
        // Get the table from the correct container
        JPanel scrollPane = (JPanel) placesPanel.getComponent(0);
        JTable table = (JTable) ((JScrollPane) scrollPane.getComponent(0)).getViewport().getView();

        // Get the restaurant name from the clicked row (column 0 is the restaurant name)
        String restaurantName = (String) table.getValueAt(row, 0);
        String restaurantPrice = table.getValueAt(row, 1).toString();

        new PaymentModal(placeName, restaurantName, restaurantPrice,app).setVisible(true);

    }
}
