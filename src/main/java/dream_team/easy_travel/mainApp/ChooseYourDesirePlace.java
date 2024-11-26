package dream_team.easy_travel.mainApp;
import dream_team.easy_travel.AdminPanel.UploadRestaurants;
import dream_team.easy_travel.DatabaseConnection.ConnectDB;
import dream_team.easy_travel.Easy_Travel;
import dream_team.easy_travel.swing.Button;
import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventObject;

public class ChooseYourDesirePlace extends JPanel {
    private Easy_Travel app;
    private JPanel placesPanel;
    private JTextField searchField;
    private JButton openModalButton;

    public ChooseYourDesirePlace(Easy_Travel app) {
        setLayout(null); // Absolute layout
        initializeComponents(app);
        this.app = app;
        fetchPlacesWithRestaurants(""); // Initially fetch all places
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

    private void initializeComponents(Easy_Travel app) {
        // Search field
        searchField = new JTextField("Search...");
        searchField.setBounds(50, 15, 300, 50);
        searchField.addActionListener(e -> fetchPlacesWithRestaurants(searchField.getText().trim()));
        add(searchField);

        // Upload Restaurants button
        openModalButton = new Button();
        openModalButton.setText("Upload Restaurants");
        openModalButton.setBounds(980, 15, 200, 50);
        openModalButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue button
        openModalButton.setForeground(Color.WHITE);
        openModalButton.addActionListener(e -> {
            UploadRestaurants uploadRestaurants = new UploadRestaurants(app.getFrame());
            uploadRestaurants.setVisible(true);
        });
        add(openModalButton);

        JButton refreshButton = new Button();
        refreshButton.setText("Refresh");
        refreshButton.setBounds(800, 15, 100, 50);
        refreshButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue button
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> fetchPlacesWithRestaurants(""));
        add(refreshButton);
        placesPanel = new JPanel();
        placesPanel.setLayout(new BoxLayout(placesPanel, BoxLayout.Y_AXIS));
        placesPanel.setBackground(new Color(255, 255, 255, 0)); // Transparent background

        JScrollPane scrollPane = new JScrollPane(placesPanel);
        scrollPane.setBounds(50, 70, 1100, 500);
        scrollPane.getViewport().setBackground(new Color(255, 255, 255, 0)); // Transparent background
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI());
        add(scrollPane);
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

            String searchPattern = "%" + searchTerm + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            ps.setString(4, searchPattern);
            ps.setString(5, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                placesPanel.removeAll();

                while (rs.next()) {
                    int placeId = rs.getInt("place_id");
                    String placeName = rs.getString("title");


                    JPanel placePanel = new JPanel();
                    placePanel.setLayout(new BoxLayout(placePanel, BoxLayout.Y_AXIS));
                    placePanel.setBorder(BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(Color.BLACK),
                            "Place ID: " + placeId + " - " + placeName
                    ));

                    DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Restaurant", "Price", "Action"}, 0);

                    JTable restaurantTable = new JTable(tableModel);
                    restaurantTable.setPreferredScrollableViewportSize(new Dimension(800, 150));
                    restaurantTable.setRowHeight(30);
                    restaurantTable.getColumn("Action").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
                        JButton button = new Button();
                        button.setText("Book");
                        button.setBackground(new Color(50, 205, 50)); // Green button
                        double payPrice = (double) table.getValueAt(row, 1);
                        button.addActionListener(e -> handleBooking(row, placeName, Double.parseDouble(String.valueOf(payPrice))));  // pass the row index and place name
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
                            JButton button = new Button();
                            button.setText("Book");
                            button.setBackground(new Color(50, 205, 50)); // Green button
                            double payPrice = (double) table.getValueAt(row, 1);
                            button.addActionListener(e -> handleBooking(row, placeName, Double.parseDouble(String.valueOf(payPrice))));  // Trigger booking on click
                            return button;
                        }

                        @Override
                        public boolean isCellEditable(EventObject anEvent) {
                            return true;  // Allow editing (button click)
                        }

                        @Override
                        public boolean shouldSelectCell(EventObject anEvent) {
                            return true;
                        }

                        @Override
                        public boolean stopCellEditing() {
                            return true;
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
    private void handleBooking(int row, String placeName, double price) {
        // Get the table from the correct container
        System.out.println(placeName);
        System.out.println(row);
        JPanel scrollPane = (JPanel) placesPanel.getComponent(0);
        JTable table = (JTable) ((JScrollPane) scrollPane.getComponent(0)).getViewport().getView();

        // Get the restaurant name from the clicked row (column 0 is the restaurant name)
        String restaurantName = (String) table.getValueAt(row, 0);
        String restaurantPrice = table.getValueAt(row, 1).toString();
        String priceString = String.valueOf(price);


        new PaymentModal(placeName, restaurantName, priceString, app).setVisible(true);

    }
}
