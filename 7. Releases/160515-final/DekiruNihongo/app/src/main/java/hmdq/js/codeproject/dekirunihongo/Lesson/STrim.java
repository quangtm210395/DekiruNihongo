package hmdq.js.codeproject.dekirunihongo.Lesson;

/**
 * Created by Phan M Duong on 4/18/2016.
 */

/**
 * class sẽ xóa khoảng trắng ở đầu và cuối của 1 String
 */
public class STrim {
    private String s;

    public STrim(String s) {
        this.s = s;
    }

    public String trim() {
        int i;
        for (i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '　') break;
        }
        s = s.substring(i);
        for (i = s.length() - 1; i > 0; i--) {
            if (s.charAt(i) != '　') break;
        }
        s = s.substring(0, i + 1);
        return s;
    }
}
