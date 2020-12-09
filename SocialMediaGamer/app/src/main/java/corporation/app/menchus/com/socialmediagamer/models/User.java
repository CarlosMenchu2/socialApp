package corporation.app.menchus.com.socialmediagamer.models;

public class User {

    private String Id;
    private String email;
    private String userName;

    public User() {

    }

    public User(String id, String email, String userName, String password) {
        Id = id;
        this.email = email;
        this.userName = userName;

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


    public void setId(String id) {
        Id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
