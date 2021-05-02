package view;

import model.Process;

import java.util.List;

public class ProcessView {
    public static void printProcessInfo(Process process) {
        System.out.println(process.toString());
    }

    public static void printProcessesInfo(List<Process> processes) {
        processes.forEach(it -> System.out.println(it.toString()));
    }
}