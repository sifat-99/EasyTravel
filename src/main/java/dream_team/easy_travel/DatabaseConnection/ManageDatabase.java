package dream_team.easy_travel.DatabaseConnection;
import dream_team.easy_travel.mainApp.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageDatabase {
    public void addNewUser(String name, String username, String password) {
        String query = "INSERT INTO Users (name, username, password) VALUES (?, ?, ?)";
        try {
            Connection conn = ConnectDB.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public User getUserByUsername(String username) throws SQLException {
        // SQL query to select user details based on the username
        String query = "SELECT * FROM Users WHERE username = ?";
        // Try-with-resources to ensure proper resource management
        try (Connection conn = ConnectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set the username parameter in the prepared statement
            stmt.setString(1, username);
            // Execute the query and store the result in ResultSet
            ResultSet rs = stmt.executeQuery();
            // Check if a result is found
            if (rs.next()) {
                // Return a new User object with the retrieved username and password
                return new User(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"));
            }
        } catch (SQLException e) {
            // Print the stack trace for debugging in case of SQL exception
            e.printStackTrace();
        }
        // Return null if no user is found or an exception occurs
        return null;
    }

}
