package ui;

import controller.ProcessController;
import controller.ProcessUserController;
import model.*;
import model.Process;

import java.util.*;

public class ConsoleUi {
    private static Scanner scanner = new Scanner(System.in);

    public static void startMenu() {
        System.out.println("1. Вход в систему\n" +
                "2. Создание пользователя\n" +
                "3. Выход из программы");
        String command = enterCommand(Arrays.asList("1", "2", "3"));
        while (!command.equals("3")) {
            switch (command) {
                case "1":
                    signInUser();
                    break;
                case "2":
                    createUser();
                    startMenu();
                    break;
                default:
                    System.exit(1);
            }
        }
    }

    private static void signInUser() {
        ProcessUser user = null;
        while (user == null) {
            System.out.print("Логин: ");
            String login = scanner.nextLine();
            System.out.print("\nПароль: ");
            String password = scanner.nextLine();
            user = Administrator.getUsers().stream().filter(it ->
                    it.getName().equals(login) && it.getPassword().equals(password)
            ).findFirst().orElse(null);
            if (user == null) {
                System.out.println("Пользователя не существует");
                System.out.println("1. Продолжение\n" +
                        "2. Главное меню\n" +
                        "3. Выход из программы");
                String command = enterCommand(Arrays.asList("1", "2", "3"));
                switch (command) {
                    case "1":
                        signInUser();
                        break;
                    case "2":
                        startMenu();
                        break;
                    case "3":
                        System.exit(1);
                }
            }
        }
        System.out.println();
        chooseWork(user);
    }

    private static void createUser() {
        System.out.println("Роль пользователя:\n1. Администратор\n2. Обычный пользователь");
        String command = enterCommand(Arrays.asList("1", "2"));
        System.out.print("Логин: ");
        String login = scanner.nextLine();
        while (Administrator.getUser(login) != null) {
            System.out.println("Логин занят");
            System.out.print("Логин: ");
            login = scanner.nextLine();
        }
        System.out.print("\nПароль: ");
        String password = scanner.nextLine();
        if (command.equals("1")) {
            ProcessUserController.createProcessUser(new Administrator(login, password));
        } else if (command.equals("2")) {
            System.out.print("\nПапка пользователя: ");
            String homeDir = scanner.nextLine();
            ProcessUserController.createProcessUser(new User(login, password, homeDir));
        }
    }

    private static void chooseWork(ProcessUser user) {
        System.out.println("1. Работа с процессами\n" +
                "2. Работа с пользователями\n" +
                "3. Выход в главное меню\n" +
                "4. Выход из программы");
        String command = enterCommand(Arrays.asList("1", "2", "3", "4"));
        switch (command) {
            case "1":
                workWithProcesses(user);
                break;
            case "2":
                workWithUsers(user);
                break;
            case "3":
                startMenu();
                break;
            case "4":
                System.exit(1);
            default:
                chooseWork(user);
        }
    }

    private static void workWithProcesses(ProcessUser user) {
        System.out.println("1. Добавление процесса\n" +
                "2. Удаление процесса\n" +
                "3. Получение информации о процессе\n" +
                "4. Получения информации о процессах\n" +
                "5. Обновление процесса\n" +
                "6. Старт процесса\n" +
                "7. Остановка процесса\n" +
                "8. Выход в меню пользователя\n" +
                "9. Выход в главное меню\n" +
                "10. Выход из программы");
        String command = enterCommand(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));
        switch (command) {
            case "1":
                addProcess(user);
                break;
            case "2":
                deleteProcess(user);
                break;
            case "3":
                getProcess(user);
                break;
            case "4":
                getProcesses(user);
                break;
            case "5":
                updateProcess(user);
                break;
            case "6":
                startProcess(user);
                break;
            case "7":
                stopProcess(user);
                break;
            case "8":
                chooseWork(user);
                break;
            case "9":
                startMenu();
                break;
            case "10":
                System.exit(1);
            default:
                workWithProcesses(user);
        }
    }

    private static void addProcess(ProcessUser user) {
        List<Object> details = addProcessDetails(user);
        String pid = ProcessController.createProcess(user, (Status) details.get(0), details.get(1).toString(),
                (int) details.get(2), (int) details.get(3));
        System.out.println("Процесс добавлен");
        System.out.println("Pid: " + pid);
        workWithProcesses(user);
    }

    private static void deleteProcess(ProcessUser user) {
        Process process = getProcessByInputPid(user);
        if (process != null) {
            ProcessController.deleteProcess(user, process);
            System.out.println("Процесс удален");
        } else {
            System.out.println("Процесс не найден");
        }
        workWithProcesses(user);
    }

    private static void getProcess(ProcessUser user) {
        Process process = getProcessByInputPid(user);
        if (process != null) {
            System.out.println("Процесс:");
            ProcessController.getProcess(process);
        } else {
            System.out.println("Процесс не найден");
        }
        workWithProcesses(user);
    }

    private static void getProcesses(ProcessUser user) {
        List<Process> processes;
        if (user instanceof Administrator) {
            processes = Administrator.getAllProcesses();
        } else {
            processes = user.getProcesses();
        }
        ProcessController.getProcesses(processes);
        workWithProcesses(user);
    }

    private static void updateProcess(ProcessUser user) {
        Process process = getProcessByInputPid(user);
        if (process != null) {
            List<Object> details = addProcessDetails(user);
            ProcessController.updateProcess(user, process.getPid(), (Status) details.get(0), details.get(1).toString(),
                    (int) details.get(2), (int) details.get(3));
            System.out.println("Процесс изменен");
        } else {
            System.out.println("Процесс не найден");
        }
        workWithProcesses(user);
    }

    private static void startProcess(ProcessUser user) {
        Process process = getProcessByInputPid(user);
        if (process != null) {
            if (process.getStatus().equals(Status.RUNNING)) {
                System.out.println("Процесс уже запущен");
            } else {
                ProcessController.startProcess(user, process.getPid());
                System.out.println("Процесс запущен");
            }
        } else {
            System.out.println("Процесс не найден");
        }
        workWithProcesses(user);
    }

    private static void stopProcess(ProcessUser user) {
        Process process = getProcessByInputPid(user);
        if (process != null) {
            if (process.getStatus().equals(Status.STOPPED)) {
                System.out.println("Процесс уже остановлен");
            } else {
                ProcessController.stopProcess(user, process.getPid());
                System.out.println("Процесс остановлен");
            }
        } else {
            System.out.println("Процесс не найден");
        }
        workWithProcesses(user);
    }

    private static List<Object> addProcessDetails(ProcessUser user) {
        System.out.println("Статус:\n1. RUNNING,\n2. STOPPED,\n3. WAITING,\n4. ENDED");
        String statusInput = enterCommand(Arrays.asList("1", "2", "3", "4"));
        Status status = null;
        switch (statusInput) {
            case "1":
                status = Status.RUNNING;
                break;
            case "2":
                status = Status.STOPPED;
                break;
            case "3":
                status = Status.WAITING;
                break;
            case "4":
                status = Status.ENDED;
                break;
            default:
                workWithProcesses(user);
        }
        System.out.print("Тип: ");
        String type = scanner.nextLine();
        System.out.print("\nВремя процесса: ");
        int time;
        int priority;
        try {
            time = Integer.parseInt(scanner.nextLine());
            System.out.print("\nПриоритет: ");
            priority = Integer.parseInt(scanner.nextLine());
        } catch (Exception exception) {
            System.out.print("\nНеверный аргумент");
            workWithProcesses(user);
            return null;
        }
        return Arrays.asList(status, type, time, priority);
    }

    private static void workWithUsers(ProcessUser user) {
        System.out.println("1. Добавление пользователя\n" +
                "2. Удаление пользователя\n" +
                "3. Получение информации о пользователе\n" +
                "4. Обновление пользователя\n" +
                "5. Получение всех пользователей (только для администраторов)\n" +
                "6. Выход в меню пользователя\n" +
                "7. Выход в главное меню\n" +
                "8. Выход из программы");
        String command = enterCommand(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8"));
        switch (command) {
            case "1":
                createUser();
                workWithUsers(user);
                break;
            case "2":
                deleteUser(user);
                break;
            case "3":
                getUser(user);
                break;
            case "4":
                updateUser(user);
                break;
            case "5":
                getUsers(user);
                break;
            case "6":
                chooseWork(user);
                break;
            case "7":
                startMenu();
                break;
            case "8":
                System.exit(1);
            default:
                workWithUsers(user);
        }
    }

    private static void deleteUser(ProcessUser user) {
        if (user instanceof Administrator) {
            System.out.println("Введите имя пользователя");
            String username = scanner.nextLine();
            ProcessUser deletedUser = Administrator.getUsers().stream().filter(it -> it.getName().equals(username))
                    .findAny().orElse(null);
            if (deletedUser != null) {
                ProcessUserController.deleteProcessUser(deletedUser);
                System.out.println("Пользователь удален");
            } else {
                System.out.println("Пользователь не найден");
                workWithUsers(user);
            }
            if (deletedUser != null && user.getName().equals(deletedUser.getName())) {
                startMenu();
            }
        } else {
            ProcessUserController.deleteProcessUser(user);
            startMenu();
        }
        workWithUsers(user);
    }

    private static void getUser(ProcessUser user) {
        if (user instanceof Administrator) {
            System.out.print("Введите имя пользователя: ");
            String username = scanner.nextLine();
            ProcessUser foundUser = Administrator.getUsers().stream().filter(it -> it.getName().equals(username))
                    .findAny().orElse(null);
            if (foundUser != null) {
                System.out.print("\nПользователь:");
                ProcessUserController.getProcessUser(foundUser);
            } else {
                System.out.println("Пользователь не найден");
            }
        } else {
            ProcessUserController.getProcessUser(user);
        }
        workWithUsers(user);
    }

    private static void getUsers(ProcessUser user) {
        if (user instanceof Administrator) {
            System.out.println("Пользователи:");
            ProcessUserController.getProcessUsers();
        } else {
            System.out.println("Вы не являетесь администратором");
        }
        workWithUsers(user);
    }

    private static void updateUser(ProcessUser user) {
        if (user instanceof Administrator) {
            System.out.println("1. Редактирование своего профиля\n" +
                    "2. Редактирование профиля обычных пользователей\n");
            String command = enterCommand(Arrays.asList("1", "2"));
            if (command.equals("2")) {
                System.out.print("Введите логин пользователя: ");
                String username = scanner.nextLine();
                ProcessUser foundUser = Administrator.getUsers().stream().filter(it -> it.getName().equals(username))
                        .findAny().orElse(null);
                if (foundUser == null) {
                    System.out.println("Пользователь не найден");
                    workWithUsers(user);
                }
            }
        }
        updateUserParams(user);
        workWithUsers(user);
    }

    private static void updateUserParams(ProcessUser user) {
        System.out.print("\nЛогин: ");
        String login = scanner.nextLine();
        System.out.print("\nПароль: ");
        String password = scanner.nextLine();
        if (user instanceof Administrator) {
            ProcessUserController.updateProcessUser((Administrator) user, login, password);
        } else {
            System.out.print("\nДомашняя папка: ");
            String homeDir = scanner.nextLine();
            System.out.println();
            ProcessUserController.updateProcessUser((User) user, login, password, homeDir);
        }
        System.out.println("Пользователь изменен");
    }

    private static String enterCommand(List<String> availableCommands) {
        try {
            Scanner scanner = new Scanner(System.in);
            String command = scanner.nextLine();
            while (!availableCommands.contains(command)) {
                System.out.println("Неверная команда");
                command = scanner.nextLine();
            }
            return command;
        } catch (InputMismatchException exception) {
            System.out.println("Неверная команда");
            return "";
        }
    }

    private static Process getProcessByInputPid(ProcessUser user) {
        System.out.print("\nВведите pid: ");
        String pid = scanner.nextLine();
        Process process;
        if (user instanceof Administrator) {
            process = Administrator.getAllProcesses().stream().filter(it -> it.getPid().equals(pid)).findFirst().orElse(null);
        } else {
            process = user.getProcesses().stream().filter(it -> it.getPid().equals(pid)).findFirst().orElse(null);
        }
        return process;
    }
}