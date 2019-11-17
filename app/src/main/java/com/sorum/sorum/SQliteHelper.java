package com.sorum.sorum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQliteHelper extends SQLiteOpenHelper {
    private static final int dbVersion = 1;
    private static final String dbName = "SorumDB";
    private static final String tableLessons = "lessons";
    private static final String tableExams = "exams";
    private static final String tableUser = "user";
    private static final String id = "id";
    private static final String lessonName = "lessonName";
    private static final String examName = "examName";
    private static final String userName = "username";
    private static final String name = "name";
    private static final String exam = "exam";
    private static final String CREATE_LESSON_TABLE = "CREATE TABLE `"+tableLessons+"` (" +
            "`"+id+"` INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "`"+lessonName+"` TEXT"
            + ");";
    private static final String CREATE_USER_TABLE = "CREATE TABLE `"+tableUser+"` (" +
            "`"+userName+"` TEXT,"+
            "`"+name+"` TEXT,"+
            "`"+exam+"` TEXT"
            + ");";
    private static final String CREATE_EXAM_TABLE = "CREATE TABLE `"+tableExams+"` (" +
            "`"+id+"` INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "`"+examName+"` TEXT"
            + ");";

    public SQliteHelper(@Nullable Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_LESSON_TABLE);
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_EXAM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addLesson(String fbLessonName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put(lessonName, fbLessonName);
        db.insert(tableLessons,null,degerler);
        db.close();
    }
    public ArrayAdapter<String> getLesson(Context Main, int stylelayout){
        String query = "SELECT * FROM "+tableLessons;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayAdapter<String> question_names = null;
        question_names = new ArrayAdapter<String>(Main, stylelayout);
        if(cursor.moveToFirst()){
            do {
                question_names.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return question_names;
    }
    public void addUser(String fbname, String fbusername, String fbexam){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put(userName, fbusername);
        degerler.put(name, fbname);
        degerler.put(exam, fbexam);
        db.insert(tableUser,null, degerler);
    }
    public String getUser(String column){
        String query = "SELECT * FROM "+tableUser;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String data =null;
        int columnindex;
        switch (column){
            case "username":
                columnindex = 0;
                break;
            case "name":
                columnindex = 1;
                break;
            case  "exam":
                columnindex = 2;
                break;
            default:
                columnindex = 0;
                break;
        }

        if(cursor.moveToFirst()){
            do {
                data=cursor.getString(columnindex);
            }while (cursor.moveToNext());
        }
        return data;
    }
    public void addExam(String fbExamName){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues degerler = new ContentValues();
        degerler.put(examName, fbExamName);
        db.insert(tableExams,null,degerler);
        db.close();
    }
    public ArrayList<String> getExam(){
        String query = "SELECT * FROM "+tableExams;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayList<String> examNames = null;
        examNames = new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                examNames.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return examNames;
    }
    public ArrayAdapter<String> istatisticGetExam(Context Main, int stylelayout){
        String query = "SELECT * FROM "+tableExams;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        ArrayAdapter<String> question_names = null;
        question_names = new ArrayAdapter<String>(Main, stylelayout);
        if(cursor.moveToFirst()){
            do {
                question_names.add(cursor.getString(1));
            }while (cursor.moveToNext());
        }
        return question_names;
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. Veritabını resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(tableLessons, null, null);
        db.delete(tableExams, null, null);
        db.delete(tableUser, null, null);
        db.close();
    }



}
