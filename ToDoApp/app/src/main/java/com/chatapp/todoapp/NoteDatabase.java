package com.chatapp.todoapp;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Note.class,version = 2,exportSchema = false) //add as array if multiple
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance; //only one interface

    public abstract NoteDao noteDao();

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class , "note_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    } //synchronized : only one thread can access this method
    //fallbacktodestructivemigration : to handle versions

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

           // new PopulateDb(instance).execute();
        }
    };

  /*  private static class  PopulateDb extends AsyncTask<Note,Void,Void>{

        private NoteDao noteDao;

        private PopulateDb(NoteDatabase db){
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Note... notes) {
           // noteDao.Insert(new Note("title","description" , 1,"date","time",true));

            return null;
        }
    }
*/
}
