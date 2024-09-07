package dream_team.easy_travel.mainApp;

public class User {
    private String username;
    private String password;
    private String name;

    // Constructor
    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    // Getter for username
    public String getUsername() {
        return username;
    }

    // Getter for password
    public String getPassword() {
        return password;
    }
}
