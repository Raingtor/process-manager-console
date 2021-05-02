package model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends ProcessUser {
    private String homeDir;

    public User(String name, String password, String homeDir) {
        super(name, password);
        this.homeDir = homeDir;
    }

    public void updateUser(String name, String password, String homeDir) {
        setName(name);
        setPassword(password);
        setHomeDir(homeDir);
    }

    @Override
    public String toString() {
        return "User{\n" + super.toString() +
                "homeDir='" + homeDir + "}";
    }
}