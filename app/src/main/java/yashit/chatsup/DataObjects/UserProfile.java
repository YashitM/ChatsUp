package yashit.chatsup.DataObjects;

/**
 * Created by yashi on 02-Jul-17.
 */

public class UserProfile {
    private String fullName;
    private String email;
    private String imageURL;

    public UserProfile() {}

    public UserProfile(String fullName, String email, String imageURL) {
        this.fullName = fullName;
        this.email = email;
        this.imageURL = imageURL;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getImageURL() {
        return imageURL;
    }
}
