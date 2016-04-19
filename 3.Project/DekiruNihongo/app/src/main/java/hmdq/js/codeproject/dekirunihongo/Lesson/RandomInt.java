package hmdq.js.codeproject.dekirunihongo.Lesson;

import java.util.Random;
import java.util.Vector;

/**
 * Created by Phan M Duong on 4/18/2016.
 */

/**
 * class này sẽ random tạo ra 1 số  và không có trùng lặp
 */
public class RandomInt {
    private Random rd = new Random();
    private Vector v = new Vector();
    private int index;

    public RandomInt(int index) {
        this.index = index;
    }

    public int Random() {
        int iRandom;
        if (v.size() == index) return -1;
        for (int i = 0; i < index;) {
            iRandom = rd.nextInt(index);
            if (!v.contains(iRandom)) {
                v.add(iRandom);
                i++;
                return iRandom;
            }
        }
        return -1;
    }

    public void remove(int iRemove) {
        v.remove(iRemove);
    }
}
