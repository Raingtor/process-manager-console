package view;

import model.ProcessUser;

import java.util.List;

public class ProcessUserView {
    public static void printProcessUserInfo(ProcessUser processUser) {
        System.out.println(processUser.toString());
    }

    public static void printProcessUsersInfo(List<ProcessUser> processUsers) {
        processUsers.forEach(it -> System.out.println(it.toString()));
    }
}