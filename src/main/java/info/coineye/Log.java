package info.coineye;

/**
 * Created by fefo on 23-Mar-16.
 */
public class Log {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void e(String message) {
        System.out.println(message);
    }

    public static void red(Object object) {
//        System.out.println(ANSI_RED + String.valueOf(object) + ANSI_RESET);
    }

    public static void blue(Object object) {
//        System.out.println(ANSI_BLUE + String.valueOf(object) + ANSI_RESET);
    }

    public static void purple(Object object) {
//        System.out.println(ANSI_PURPLE + String.valueOf(object) + ANSI_RESET);
    }

    public static void green(Object object) {
//        System.out.println(ANSI_GREEN + String.valueOf(object) + ANSI_RESET);
    }

    public static void cyan(Object object) {
//        System.out.println(ANSI_CYAN + String.valueOf(object) + ANSI_RESET);
        System.out.println(String.valueOf(object));
    }

}
