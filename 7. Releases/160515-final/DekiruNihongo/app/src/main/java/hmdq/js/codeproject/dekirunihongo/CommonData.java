package hmdq.js.codeproject.dekirunihongo;

/**
 * Created by Minnn on 5/4/2016.
 */
public class CommonData {
    public static String noLesson;
    public static String noBook;
    private static CommonData ourInstance = new CommonData();

    public static CommonData getInstance() {
        if (ourInstance == null){
            ourInstance = new CommonData();
        }
        return ourInstance;
    }

    private CommonData() {
    }
}
