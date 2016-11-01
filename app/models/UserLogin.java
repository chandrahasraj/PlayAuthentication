package models;

/**
 * Created by chandra on 10/30/16.
 */
public class UserLogin {
    public String email;
    public String password;

    public UserLogin() {
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
