package com.chatapp.todoapp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.logging.SocketHandler;

public class SharedPreference {

    private static final String SharedPreference_name = "my_SharedPreference";

    private static SharedPreference instance;
    private Context context;

    SharedPreference(Context context) {
        this.context = context;
    }

    public static synchronized SharedPreference getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreference(context);

        }
        return instance;
    }

    public void save_flag(int flag){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("flag", flag);
        editor.apply();

    }
    public int get_flag(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);

        return sharedPreferences.getInt("flag", 0);
    }

    public void del_flag(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SharedPreference_name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("flag");
        editor.apply();


    }

}
