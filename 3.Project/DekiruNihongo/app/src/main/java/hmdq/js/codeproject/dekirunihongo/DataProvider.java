package hmdq.js.codeproject.dekirunihongo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DataProvider {
    public static final String HOST_NAME = "http://dekirunihongo.esy.es/cf.php";

    HttpURLConnection con;

    InputStream stream;

    InputStreamReader reader;

    SQLiteDatabase db;

    Cursor query;

    gettingData gd;

    public DataProvider(Context context) {
        super();
        gd = new gettingData();
        db = context.openOrCreateDatabase("jsdk.db", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS info (num integer, rev integer, dat text)");
        int num_row = db.rawQuery("SELECT * from info", null).getCount();
        if (num_row == 0) db.execSQL("insert into info (num, rev) values(1, 0)");
    }

    int getLocalRev() {
        query = db.rawQuery("SELECT rev FROM info", null);
        query.moveToFirst();
        return query.getInt(0);
    }

    String getLocalData() {
        query = db.rawQuery("SELECT dat FROM info",null);
        query.moveToFirst();
        return query.getString(0);
    }

    void updateData(String rev, String data) {
        db.execSQL("UPDATE info SET rev=" + rev + ", dat='" + data + "' where (num=1)");
    }

    public String rebuild(String s) {
        StringBuilder sb = new StringBuilder();
        char[] chars = s.toCharArray();
        for (char c : chars) {
            switch (c) {
                case '*':
                    sb.append(System.getProperty("line.separator"));
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }

    public TreeMap<String, String> getData(String book, String part, String lesson) {
        String tableName = part + book;
        String lessonName = "l" + lesson;
        TreeMap<String, String> result = new TreeMap<>();
        try {
            JSONObject root = new JSONObject(getLocalData());
            root = root.getJSONObject("data");
            JSONArray unit = root.getJSONObject(tableName).getJSONArray(lessonName);
            for (int i = 0; i< unit.length(); i++) {
                JSONObject o = unit.getJSONObject(i);
                result.put(o.getString("n"), o.getString("m"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> getListLesson(String book, String part) {
        String columnName = "b" + book;
        String tableName;
        if (part.equals("vocab")) tableName = "list1";
        else if (part.equals("gra")) tableName = "list2";
        else if (part.equals("quiz")) tableName = "list3";
        else tableName = "list4";
        List<String> result = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(getLocalData());
            root = root.getJSONObject("data");
            JSONArray table = root.getJSONObject(tableName).getJSONArray(columnName);
            for (int i = 0; i< table.length(); i++) result.add(table.getString(i));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TreeMap<String, String> find(String text, String book) {
        String tableName = "vocab" + book;
        TreeMap<String, String> result = new TreeMap<>();
        try {
            JSONObject root = new JSONObject(getLocalData());
            root = root.getJSONObject("data");
            JSONObject table = root.getJSONObject(tableName);
            int numLesson = table.length();
            Log.v("TAG", String.valueOf(numLesson));
            for (int i = 1;i <= numLesson;i++) {
                JSONArray unit = table.getJSONArray("l" + i);
                for (int j = 0; j < unit.length();j++) {
                    JSONObject o = unit.getJSONObject(j);
                    if (o.getString("n").contains(text)) result.put(o.getString("n"), o.getString("m"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    void requestData (String rq, OnDataReceived callback) {
        String url = HOST_NAME + "?r=" + rq;
        gd.execute(url, callback);
    }

    void closeDB() {
        db.close();
    }

    interface OnDataReceived {
        void onReceive (String result);
    }

    private class gettingData extends AsyncTask<Object, Void, String> {
        OnDataReceived callback;

        @Override
        protected String doInBackground(Object... params) {
            String u = (String) params[0];
            callback = (OnDataReceived) params[1];
            String result = "";
            try {
                URL url = new URL(u);
                con = (HttpURLConnection) url.openConnection();
                stream = con.getInputStream();
                reader = new InputStreamReader(stream);
                int temp;
                while ((temp = reader.read()) != -1) result += (char) temp;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) con.disconnect();
            }
            return result.trim();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            callback.onReceive(rebuild(s));
        }
    }

}
