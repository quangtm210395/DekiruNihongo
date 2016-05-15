package hmdq.js.codeproject.dekirunihongo;

import android.content.Context;

import java.util.TreeMap;

/**
 * Created by Minnn on 5/10/2016.
 */
public class GetData {
    public static DataProvider dp;
    public static Context context;
    public static TreeMap<String, String> mapVocab = new TreeMap<>();
    public static TreeMap<String, String> mapGram = new TreeMap<>();
    public static TreeMap<String, String> mapKanji = new TreeMap<>();
    public static TreeMap<String, String> mapQuiz = new TreeMap<>();
    public static String book;
    public static String lesson;

    public GetData(Context context, String book, String lesson){
        this.context = context;
        this.book = book;
        this.lesson = lesson;
        dp = new DataProvider(context);
        if (dp != null){
            mapVocab = dp.getData(book, "vocab", lesson);
            mapGram = dp.getData(book, "gra", lesson);
            mapKanji = dp.getData(book, "kan", lesson);
            mapQuiz = dp.getData(book, "quiz", lesson);
        }

    }

    public String[] getsTu(){
        String[] array = new String[mapVocab.size()];
        mapVocab.keySet().toArray(array);

        return array;
    }

    public String[] getsNghia(){
        String[] array = new String[mapVocab.size()];
        mapVocab.values().toArray(array);

        return array;
    }

    public String[] getsNameGram(){
        String[] array = new String[mapGram.size()];
        mapGram.keySet().toArray(array);

        return array;
    }

    public String[] getsGram(){
        String[] array = new String[mapGram.size()];
        mapGram.values().toArray(array);

        return array;
    }

    public String[] getsNameKanji(){
        String[] array = new String[mapKanji.size()];
        mapKanji.keySet().toArray(array);

        return array;
    }

    public String[] getsKanji(){
        String[] array = new String[mapKanji.size()];
        mapKanji.values().toArray(array);

        return array;
    }

    public String[] getsQuestion(){
        String[] array = new String[mapQuiz.size()];
        mapQuiz.keySet().toArray(array);

        return array;
    }

    public String[] getsAnswer(){
        String[] array = new String[mapQuiz.size()];
        mapQuiz.values().toArray(array);

        return array;
    }


}
