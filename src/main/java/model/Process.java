package model;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class Process {
    private String pid;

    private ProcessUser creator;

    private Status status;

    private String type;

    private int processTime;

    private int priority;

    public Process(ProcessUser creator, Status status, String type, int processTime,
                   int priority) {
        this.pid = UUID.randomUUID().toString();
        this.creator = creator;
        this.status = status;
        this.type = type;
        this.processTime = processTime;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Process{" +
                "pid=" + pid + ",\n" +
                "creator=" + creator.getName() + ",\n" +
                "status=" + status + ",\n" +
                "type=" + type + ",\n" +
                "processTime=" + processTime + ",\n" +
                "priority=" + priority + "}";
    }
}