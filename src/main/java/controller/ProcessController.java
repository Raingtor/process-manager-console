package controller;

import model.Process;
import model.ProcessUser;
import model.Status;
import view.ProcessView;

import java.util.List;

public class ProcessController {

    public static String createProcess(ProcessUser user, Status status, String type, int processTime,
                                       int priority) {
        Process process = new Process(user, status, type, processTime, priority);
        user.addProcess(process);
        return process.getPid();
    }

    public static void getProcess(Process process) {
        ProcessView.printProcessInfo(process);
    }

    public static void getProcesses(List<Process> processes) {
        ProcessView.printProcessesInfo(processes);
    }

    public static void deleteProcess(ProcessUser user, Process process) {
        user.deleteProcess(process);
    }

    public static void updateProcess(ProcessUser user, String pid, Status status, String type, int processTime,
                                     int priority) {
        user.updateProcess(pid, status, type, processTime, priority);
    }

    public static void startProcess(ProcessUser user, String pid) {
        user.startProcess(pid);
    }

    public static void stopProcess(ProcessUser user, String pid) {
        user.stopProcess(pid);
    }
}