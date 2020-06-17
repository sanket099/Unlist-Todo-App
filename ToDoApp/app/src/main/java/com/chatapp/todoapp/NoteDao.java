package com.chatapp.todoapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao { //either interface or abstract class as we dont provide method body we just annotate
    //multiple args or even a list

    @Insert
    void Insert(Note note);

    @Update //(onConflict = OnConflictStrategy.REPLACE)
    void Update(Note note);

    @Delete
    void Delete(Note note);

    @Query("DELETE FROM note_table")
    void DeleteAllNotes();

    @Query("SELECT * FROM note_table Order By date Asc ")
    LiveData<List<Note>> getAllNotes();  //updates and returns


}
