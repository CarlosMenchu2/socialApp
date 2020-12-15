package corporation.app.menchus.com.socialmediagamer.models;

public class Post {

    private String id;
    private String idUser;
    private String title;
    private String description;
    private String image1;
    private String image2;
    private String category;

    public Post(){}

    public Post(String id, String idUser, String title, String description, String image1, String image2, String category) {
        this.id = id;
        this.idUser = idUser;
        this.title = title;
        this.description = description;
        this.image1 = image1;
        this.image2 = image2;
        this.category = category;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getId() {
        return id;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage1() {
        return image1;
    }

    public String getImage2() {
        return image2;
    }

    public String getCategory() {
        return category;
    }
}