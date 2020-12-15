package corporation.app.menchus.com.socialmediagamer.models;

public class User {

    private String Id;
    private String email;
    private String userName;
    private String phoneNumber;
    private long timestamp;

    public User() {

    }

    public User(String id, String email, String userName, String phoneNumber, long timestamp) {
        Id = id;
        this.email = email;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
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
}
