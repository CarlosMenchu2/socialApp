package corporation.app.menchus.com.socialmediagamer.models;

public class User {

    private String Id;
    private String email;
    private String userName;
    private String phoneNumber;
    private String image_profile;
    private String image_cover;
    private long timestamp;


    public User() {

    }

    public User(String id, String email, String userName, String phoneNumber,String image_profile,String image_cover, long timestamp) {
        Id = id;
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.image_profile = image_profile;
        this.image_cover = image_cover;
        this.timestamp = timestamp;

    }

    public String getId() {
        return Id;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getImage_profile() {
        return image_profile;
    }

    public String getImage_cover() {
        return image_cover;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setImage_profile(String image_profile) {
        this.image_profile = image_profile;
    }

    public void setImage_cover(String image_cover) {
        this.image_cover = image_cover;
    }
}
