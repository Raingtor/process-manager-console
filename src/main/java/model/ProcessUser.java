package model;

import exception.ObjectNotFoundException;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class ProcessUser {
    private String name;

    private List<Process> processes;

    private String password;

    public ProcessUser(String name, String password) {
        this.name = name;
        this.processes = new ArrayList<>();
        this.password = password;
    }

    public Process getProcess(String pid) {
        return processes
                .stream()
                .filter(it -> it.getPid().equals(pid))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException(Process.class.getName()));
    }

    public void addProcess(Process process) {
        processes.add(process);
    }

    public void deleteProcess(Process process) {
        processes.remove(process);
    }

    public void updateProcess(String pid, Status status, String type, int processTime,
                              int priority) {
        Process process = getProcess(pid);
        process.setStatus(status);
        process.setType(type);
        process.setProcessTime(processTime);
        process.setPriority(priority);
    }

    public void startProcess(String pid) {
        getProcess(pid).setStatus(Status.RUNNING);
    }

    public void stopProcess(String pid) {
        getProcess(pid).setStatus(Status.STOPPED);
    }

    @Override
    public String toString() {
        return "name=" + name + ",\n" +
                "processes=" + processes + ",\n" +
                "password=" + password + ",\n";
    }
}