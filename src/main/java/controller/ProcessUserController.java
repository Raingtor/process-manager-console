package controller;

import model.Administrator;
import model.ProcessUser;
import model.User;
import view.ProcessUserView;

public class ProcessUserController {
    public static void createProcessUser(Administrator administrator) {
        Administrator.addUser(administrator);
    }

    public static void createProcessUser(User user) {
        Administrator.getUsers().add(user);
    }

    public static void getProcessUser(ProcessUser user) {
        ProcessUserView.printProcessUserInfo(user);
    }

    public static void deleteProcessUser(ProcessUser user) {
        Administrator.deleteUser(user);
    }

    public static void updateProcessUser(Administrator user, String name, String password) {
        user.updateUser(name, password);
    }

    public static void updateProcessUser(User user, String name, String password, String homeDir) {
        user.updateUser(name, password, homeDir);
    }

    public static void getProcessUsers() {
        ProcessUserView.printProcessUsersInfo(Administrator.getUsers());
    }
}
