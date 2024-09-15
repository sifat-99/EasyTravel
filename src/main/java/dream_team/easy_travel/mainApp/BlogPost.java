package dream_team.easy_travel.mainApp;

public class BlogPost {
    private final int id;
    private final String title;
    private final String description;
    private final byte[] image;

    public BlogPost(int id, String title, String description, byte[] image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public byte[] getImage() { return image; }
}