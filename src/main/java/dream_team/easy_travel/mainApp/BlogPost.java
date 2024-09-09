package dream_team.easy_travel.mainApp;

public class BlogPost {
    private int id;
    private String title;
    private String description;
    private byte[] image;

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