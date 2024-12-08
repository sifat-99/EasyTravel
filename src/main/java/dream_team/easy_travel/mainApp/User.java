package dream_team.easy_travel.mainApp;

public class User {
    private final String username;
    private final String password;
    private final String name;
    private final int id;

    // Constructor
    public User(int id, String name, String username, String password) {
        this.id = id;
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
    public int getId() {
        return id;
    }
}
