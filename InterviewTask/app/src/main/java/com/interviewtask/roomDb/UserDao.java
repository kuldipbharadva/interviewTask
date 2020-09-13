package com.interviewtask.roomDb;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.interviewtask.models.GetUserRe;
import com.interviewtask.utils.Constants;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM " + Constants.TABLE_NAME_USER)
    List<GetUserRe> getAllData();

    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    void insert(GetUserRe data);

    @Insert
    void insertAll(ArrayList<GetUserRe> addAll);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void update(GetUserRe data);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void delete(GetUserRe data);

    /*
     * delete list of objects from database
     * @param note, array of objects to be deleted
     */
    @Delete
    void delete(GetUserRe... data);      // NoteModel... is varargs, here note is an array

    @Delete
    void deleteAll(List<GetUserRe> deleteAll);

    @Query("DELETE FROM " + Constants.TABLE_NAME_USER)
    public void deleteUserTable();
}