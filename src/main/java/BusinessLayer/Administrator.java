package BusinessLayer;

import java.io.Serializable;

public class Administrator implements Serializable {
    private String username;
    private String password;

    public Administrator(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Administrator(){

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
