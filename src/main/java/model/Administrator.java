package model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Administrator extends ProcessUser {
    private static List<ProcessUser> users = new ArrayList<>();

    public Administrator(String name, String password) {
        super(name, password);
    }

    public static void addUser(ProcessUser user) {
        users.add(user);
    }

    public static void deleteUser(ProcessUser user) {
        user.getProcesses()
                .forEach(user::deleteProcess);
        users.remove(user);
    }

    public void updateUser(String name, String password) {
        setName(name);
        setPassword(password);
    }

    public static List<ProcessUser> getUsers() {
        return users;
    }

    public static ProcessUser getUser(String name) {
        return getUsers().stream().filter(it -> it.getName().equals(name)).findFirst().orElse(null);
    }

    public static List<Process> getAllProcesses() {
        List<Process> processes = new ArrayList<>();
        for (var user : getUsers()) {
            for (var userProcess : user.getProcesses()) {
                processes.add(userProcess);
            }
        }
        return processes;
    }

    @Override
    public String toString() {
        return "Administrator{\n" +
                super.toString() +
                "}";
    }
}