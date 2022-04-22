import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Collections;
import java.util.Objects;
import java.util.Scanner;
import java.util.Vector;
class Parser {
    String commandName;
    String[] args;
    boolean f= true;
    boolean l=true;
    public boolean parse(String input){
        String[] split = input.split("\\s+");
        commandName= split[0];
        if(split[0].equals("cp")&&split.length>=2&&split[1].equals("-r")) f = false;
        if(split[0].equals("ls")&&split.length>=2&&split[1].equals("-r")) l = false;
        if(split.length>1&&f&&l) {
            args=new String[split.length-1];
            System.arraycopy(split, 1, args, 0, split.length - 1);
            return true;
        }
        else if(!l){
            commandName=split[0]+" "+split[1];
            args=new String[split.length-2];
            System.arraycopy(split, 2, args, 0, split.length-2);
            l=true;
        }
        else if(!f){
            commandName=split[0]+" "+split[1];
            args=new String[split.length-2];
            System.arraycopy(split, 2, args, 0, split.length-2);
            f=true;
        }
        else {
            return false;
        }
        return true;
    }
    public String getCommandName(){
        return commandName;
    }
    public String[] getArgs(){
        return args;
    }
    public String print(){
        StringBuilder p= new StringBuilder();
        for (String arg : args) {
            p.append(arg);
            p.append(" ");
        }
        p.deleteCharAt(p.length()-1);
        return String.valueOf(p);
    }
}
public class Terminal {
    Parser parser = new Parser();
    String line;
    String currPath = System.getProperty("user.dir");
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_RESET = "\u001B[0m";
    private boolean parse;

    //line of input taken from user
    public void setLine(String line) {
        this.line = line;
        parse = parser.parse(line);
    }

    //for switching between commands
    public void chooseCommandAction() {

        //Takes 1 argument and prints it.
        if (Objects.equals(parser.getCommandName(), "echo")) {
            if (parse) {
                echo();
            } else System.out.println(" ");
        }

        //Takes no arguments and prints the current path.
        else if (Objects.equals(parser.getCommandName(), "pwd")) {
            System.out.println(pwd());
        }

        //Takes 1 argument which is either the full path or the relative, (short) path that ends with a file name and creates this file.
        else if (Objects.equals(parser.getCommandName(), "touch")) {
            if (parse) {
                touch(parser.print());
            } else System.out.println("Please enter arguments");
        }

        //Takes no arguments and lists the contents of the current directory sorted alphabetically.
        else if (Objects.equals(parser.getCommandName(), "ls")) {
            ls();
        }

        //Takes no arguments and lists the contents of the current directory in reverse order.
        else if (Objects.equals(parser.getCommandName(), "ls -r")) {
            lsr();
        }

        //Takes 1 argument which is a file name that exists in the current directory and removes this file.
        else if (Objects.equals(parser.getCommandName(), "rm")) {
            if (parse) {
                rm();
            } else System.out.println("Please enter arguments");
        }

        //Takes 2 arguments, both are files and copies the first onto the second.
        else if (Objects.equals(parser.getCommandName(), "cp")) {
            if (parse) {
                cp(parser.getArgs());
            } else System.out.println("Please enter arguments");
        }

        //
        else if (Objects.equals(parser.getCommandName(), "cp -r")) {
            if (parse) {
                cpr();
            } else System.out.println("Please enter arguments");
        }

        //
        else if (Objects.equals(parser.getCommandName(), "cat")) {
            if (parse) {
                cat(parser.getArgs());
            } else System.out.println("Please enter arguments");
        }

        //
        else if (Objects.equals(parser.getCommandName(), "cd")) {
            cd(parser.getArgs());
        }

        //
        else if (Objects.equals(parser.getCommandName(), "mkdir")) {
            if (parse) {
                mkdir(parser.getArgs());
            } else System.out.println("Please enter arguments");
        }

        //
        else if (Objects.equals(parser.getCommandName(), "rmdir")) {
            if (parse) {
                rmdir(parser.getArgs());
            } else System.out.println("Please enter arguments");
        }

        //
        else if (Objects.equals(parser.getCommandName(), "exit")) {
            exit();
        }

        //
        else {
            System.out.println("No such command");
        }
    }

    //1 done
    public void echo() {
        System.out.println(parser.print());
    }

    //2 done
    public String pwd() {
        return currPath;
    }

    //3 done
    public void touch(String p) {
        Path path;
        if (p.contains("/")) {
            Path resolvedPath = Path.of(currPath).resolve(p);
            path = resolvedPath.normalize();
        } else {
            path = Path.of(currPath + "/" + parser.print());
        }
        boolean flag = true;
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException x) {
            System.out.println("file already exists");
            flag = false;
        } catch (IOException x) {
            System.out.println("No such file or directory");
            flag = false;
        }
        if (flag) System.out.println("File created successfully");
    }

    //4 done
    public void ls() {
        Vector<String> content = new Vector<>();
        Path dir = Path.of(currPath);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                content.add(String.valueOf(file.getFileName()));
            }
            Collections.sort(content);
            for (String file : content) {
                int index = file.lastIndexOf('.');
                if (index > 0 && index < file.length()) {
                    System.out.println(ANSI_PURPLE + "File: " + file + ANSI_RESET);
                } else System.out.println(ANSI_BLUE + "Folder: " + file + ANSI_RESET);
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println(x.getMessage());
        }
    }

    //5 done
    public void lsr() {
        Vector<String> content = new Vector<>();
        Path dir = Path.of(pwd());
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file : stream) {
                content.add(String.valueOf(file.getFileName()));
            }
            Collections.sort(content);
            Collections.reverse(content);
            for (String file : content) {
                int index = file.lastIndexOf('.');
                if (index > 0 && index < file.length()) {
                    System.out.println(ANSI_PURPLE + "File: " + file + ANSI_RESET);
                } else System.out.println(ANSI_BLUE + "Folder: " + file + ANSI_RESET);
            }
        } catch (IOException | DirectoryIteratorException x) {
            System.err.println("Something went wrong, try again");
        }
    }

    //6 done
    public void rm() {
        String name = currPath + "/" + parser.print();
        Path path = Path.of(name);
        try {
            if(Files.deleteIfExists(path))
                System.out.println("File deleted successfully");
            else System.out.println("File doesn't exist");
        } catch (IOException e) {
            System.err.println("Something went wrong, try again");
        }
    }

    //7 done
    public void cp(String[] args) {
        if (args.length == 2) {
            Path from = Path.of(currPath + "/" + args[0]);
            Path to = Path.of(currPath + "/" + args[1]);
            try {
                Files.copy(from, to, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("success");
            } catch (IOException e) {
                System.err.println("Something went wrong, try again");
            }
        } else System.out.println("Something went wrong, try again");
    }

    //8
    public void cat(String[] args) {
        Path file = Path.of(pwd() + "/" + args[0]);
        Charset charset = StandardCharsets.US_ASCII;
        if (args.length == 1) {
            try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
                System.out.println("Something went wrong, try again");
            }
        }
        else {
            for (int i = 0; i < 2; i++) {
                try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException x) {
                    System.out.println("Something went wrong, try again");
                }
                file = Path.of(pwd() + "/" + args[1]);
            }
            System.out.println();
        }
    }

    //9 done
    public void cd(String[] args) {
        Path path = null;
        if (parse) {
            if (args[0].equals("..")) {
                try {
                    path = Path.of(currPath);
                    if (path.subpath(0, path.getNameCount()).toString().length() > 1) {
                        this.setCurrPath(String.valueOf(path.getRoot()) + path.subpath(0, path.getNameCount() - 1));
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("error: you reached root");
                }
            }
            else if (parser.print().contains("/")) {
                path = Path.of(parser.print()).toAbsolutePath();
                if (!new File(String.valueOf(path)).exists()) {
                    path = Path.of(currPath + "/" + parser.print());
                    if (!new File(String.valueOf(path)).exists()) {
                        System.out.println("invalid path");
                    } else {
                        this.setCurrPath(String.valueOf(path));
                    }
                } else {
                    this.setCurrPath(String.valueOf(path));
                }
            }
            //for one argument with no "/"
            else {
                path = Path.of(currPath + "/" + parser.print());
                if (!new File(String.valueOf(path)).getAbsoluteFile().exists()) {
                    System.out.println("invalid path");
                } else {
                    this.setCurrPath(String.valueOf(path));
                }
            }
            System.out.println(currPath);
        } else {
            setCurrPath(System.getProperty("user.home"));
        }
    }

    //10 done
    public void cpr() {
        String fromDirectory;
        fromDirectory = currPath+"/"+parser.args[0];
        String toToDirectory;
        toToDirectory = currPath+"/"+parser.args[1];
        String newDir = toToDirectory+"/"+parser.args[0];
        try {
            copyDirectoryLegacyIO(new File(fromDirectory), new File(newDir));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error");
        }
        System.out.println("Done");
    }

    //11
    public void mkdir(String[] args) {
        if (args.length == 1) {
            if (parser.print().contains("/")) {
                File f = new File(String.valueOf(Path.of(parser.print()).toAbsolutePath()));
                boolean status = f.mkdir();
                if (!status) {
                    f = new File(currPath + "/" + parser.print());
                    status = f.mkdir();
                }
                System.out.println("the status is " + status);
            } else {
                File f = new File(String.valueOf(Path.of(currPath + "/" + parser.print())));
                boolean status = f.mkdir();
                System.out.println("the status is " + status);
            }
        }
        else {
            for (String arg : args) {
                if (arg.contains("/")) {
                    File f = new File(String.valueOf(Path.of(arg).toAbsolutePath()));
                    boolean status = f.mkdir();
                    if (!status) {
                        f = new File(currPath + "/" + arg);
                        status = f.mkdir();
                    }
                    System.out.println("the status is " + status);
                } else {
                    File f = new File(String.valueOf(Path.of(currPath + "/" + arg)));
                    boolean status = f.mkdir();
                    System.out.println("the status is " + status);
                }
            }
        }
    }

    //12
    public void rmdir(String[] args) {
        if (args[0].equals("*")) {
            File folder = new File(pwd());
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        if (Objects.requireNonNull(file.list()).length == 0) {
                            file.delete();
                            System.out.println("Directory " + file.getName() + " deleted successfully!");
                        } else {
                            System.out.println("Not-empty! Couldn't be deleted!");
                        }
                    } catch (NullPointerException e) {
                        System.out.println("failed to remove: Not a directory");
                    }
                }
            }
        }
        //path
        else {
            File f;
            if (parser.print().contains("/")) {
                f = new File(String.valueOf(Path.of(parser.print()).toAbsolutePath()));
                boolean status = f.exists();
                if (!status) {
                    f = new File(currPath + "/" + parser.print());
                }
            } else {
                f = new File(String.valueOf(Path.of(currPath + "/" + parser.print())));
            }
            try {
                if (Objects.requireNonNull(f.list()).length == 0) {
                    f.delete();
                    System.out.println("Directory " + f.getName() + " deleted successfully!");
                } else {
                    System.out.println("Not-empty! Couldn't be deleted!");
                }
            } catch (NullPointerException e) {
                System.out.println("failed to remove: Not a directory");
            }

        }
    }


    //

    public void exit() {
        System.exit(0);
    }

    public void setCurrPath(String path) {
        this.currPath = path;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        String line;
        Terminal terminal = new Terminal();
        while (true) {
            System.out.println();
            System.out.print(">");
            line = input.nextLine();
            terminal.setLine(line);
            terminal.chooseCommandAction();
        }
    }

    //function for copying files
    public void copyDirectoryLegacyIO(File source, File target) throws IOException {
        if (source.isDirectory()) {
            if (!target.exists()) {
                if (target.mkdir()) {
                    System.out.println("Directory copied from " + source + "  to " + target);
                }
                else {
                    System.err.println("Unable to create directory : " + target);
                }
            }
            String[] files = source.list();
            if (files == null) {
                return;
            }
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(target, file);
                copyDirectoryLegacyIO(srcFile, destFile);
            }
        } else {
            try (InputStream in = new FileInputStream(source); OutputStream out = new FileOutputStream(target)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
                System.out.println("copying...");
                System.out.println(source.getName());
            } catch (IOException e) {
                System.err.println("IO errors : " + e.getMessage());
            }
        }
    }
}