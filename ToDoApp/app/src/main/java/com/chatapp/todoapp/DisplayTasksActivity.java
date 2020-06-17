package com.chatapp.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Objects;
import java.util.Random;


public class DisplayTasksActivity extends AppCompatActivity implements NoteAdapter.OnNoteClick {
    NoteAdapter adapter;
    FloatingActionButton fab;

    private NoteViewModel noteViewModel;
    CoordinatorLayout coordinatorLayout;
    AlertDialog.Builder builder;
    ImageView iv;
    int c;

    RecyclerView recyclerView;
    CardView cardView;
    TextView tv_view_completed , tv_good, tv_desc;
   private int[] img_chill = new int[]{R.drawable.ic_layingdoodle, R.drawable.ic_doogiedoodle, R.drawable.ic_rollerskatingdoodle , R.drawable.ic_icecreamdoodle};
   private int[] img_welcome = new int[]{
           R.drawable.ic_readingsidedoodle,
           R.drawable.ic_doogiedoodle,
           R.drawable.ic_icecreamdoodle,
           R.drawable.ic_coffeedoddle
   };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


         recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
         tv_view_completed = findViewById(R.id.view_complete);
        cardView = findViewById(R.id.cv_good);
        builder = new AlertDialog.Builder(DisplayTasksActivity.this);
        recyclerView.setHasFixedSize(true);
        iv = findViewById(R.id.iv_rest);
        adapter = new NoteAdapter(this,this);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView.setAdapter(adapter);
        tv_desc = findViewById(R.id.tv_good_desc);
        tv_good = findViewById(R.id.tv_good);
        //sharedPreference = new SharedPreference(this);
        fab = findViewById(R.id.fab);

        ObjectAnimator animY = ObjectAnimator.ofFloat(fab, "translationY", -150f, 10f); //btn animation
        animY.setDuration(1000);//1sec
        animY.setInterpolator(new BounceInterpolator());
        animY.setRepeatMode(ValueAnimator.REVERSE);
        animY.setRepeatCount(2);
        animY.start();

        setTitle("Your Tasks");



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayTasksActivity.this,AddNoteActivity.class);

                startActivityForResult(intent,1);
            }
        });





        //retireve view model

         noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class); //init

      /*  noteViewModel = new ViewModelProvider(this
                      , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                        .get(NoteViewModel.class);*/


        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) { //every time data changes
                adapter.submitList(notes);

                System.out.println("notes.size() = " + notes.size());


                if(notes.isEmpty()){
                   // Toast.makeText(DisplayTasksActivity.this, "card HI", Toast.LENGTH_SHORT).show();
                    showWelcomeCard();//insert HI card
                }

                else if (heythere()){
                    // Toast.makeText(DisplayTasksActivity.this, "lmao done", Toast.LENGTH_SHORT).show();
                    showFinishedCard();
                }
                else{
                    hideCard();
                }










              //  adapter.notifyDataSetChanged();

                //update rv


            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
                return 0.5f; //speed
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


                final int position = viewHolder.getAdapterPosition();
                final Note item = adapter.getNoteAt(position);
                noteViewModel.delete(adapter.getNoteAt(position));
                Toast.makeText(DisplayTasksActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                System.out.println("adapter.getCurrentList().size() = " + adapter.getCurrentList().size());



                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        noteViewModel.insert(item);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();


            }
        }).attachToRecyclerView(recyclerView);


      tv_view_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noteViewModel.getAllNotes().getValue().isEmpty()){
                    hideCard();
                }
                else{
                    showWelcomeCard();
                }


            }
        });
    }

    public boolean heythere(){
        boolean done = false;
        for(Note note : noteViewModel.getAllNotes().getValue()){
            if(note.getDone()){
                done = true;
            }
            else{
                done = false;
                break;
            }
        }
        return done;
    }


    @Override
    public void click(Note note) {
        Toast.makeText(this,note.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(DisplayTasksActivity.this,AddNoteActivity.class);
        intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
        intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
        intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority());
        intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
        intent.putExtra(AddNoteActivity.EXTRA_DATE,note.getDate());
        intent.putExtra(AddNoteActivity.EXTRA_TIME,note.getTime());
        if(note.getDone() == null){
            note.setDone(false);

        }
        intent.putExtra(AddNoteActivity.EXTRA_DONE,note.getDone());
        System.out.println("idd" + note.getId());
        startActivityForResult(intent,2);




    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int pri = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY,1);
            String date = data.getStringExtra(AddNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddNoteActivity.EXTRA_TIME);
            Boolean done = data.getBooleanExtra(AddNoteActivity.EXTRA_DONE,false);


            Note note = new Note(title,desc,pri,date,time,done);
            noteViewModel.insert(note);
            //adapter.notifyItemInserted(adapter.getItemCount());
           // adapter.notifyDataSetChanged();
            hideCard();



            System.out.println("noteViewModel.getAllNotes().getValue().size() = " + noteViewModel.getAllNotes().getValue().isEmpty());


            //note saved

        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            adapter.notifyDataSetChanged();
          Intent intent = getIntent();
          System.out.println(intent);

            assert data != null;
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID,-1);
           // String string = getIntent().getStringExtra(AddNoteActivity.EXTRA_TITLE);

            if(id == -1){
                //invalid id
                System.out.println(id);
                return;
            }

            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int pri = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY,1);
            String date = data.getStringExtra(AddNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddNoteActivity.EXTRA_TIME);
            Boolean done = data.getBooleanExtra(AddNoteActivity.EXTRA_DONE,false);



            Note note = new Note(title,desc,pri,date,time,done);
            note.setId(id);
            noteViewModel.update(note);
            hideCard();





            //note updated
            System.out.println("2");

        }
        else{
            //note not saved
            Toast.makeText(this, "Note NOT saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFinishedCard(){
        int rnd = new Random().nextInt(4);


        System.out.println("img"+img_chill[rnd]+"nu"+rnd);
        iv.setImageResource(img_chill[rnd]);
        cardView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tv_good.setText(R.string.goodjob);
        tv_desc.setText(R.string.rest);
        tv_view_completed.setVisibility(View.VISIBLE);


    }
    public void showCardAfterDelete(){
        int rnd = new Random().nextInt(4);


        System.out.println("img"+img_chill[rnd]+"nu"+rnd);
        iv.setImageResource(img_chill[rnd]);
        cardView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tv_good.setText(R.string.hey_there);
        tv_desc.setText(R.string.after_del);
        tv_view_completed.setVisibility(View.VISIBLE);


    }

    private void showWelcomeCard(){
        int rnd = new Random().nextInt(4);


        System.out.println("img"+img_welcome[rnd]+"nu"+rnd);
        iv.setImageResource(img_welcome[rnd]);

        cardView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tv_good.setText(R.string.Welcome);
        tv_desc.setText(R.string.welcomegoahead);
        tv_view_completed.setVisibility(View.GONE);


    }
    public void hideCard(){
        cardView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.delete_All:

                if(Objects.requireNonNull(noteViewModel.getAllNotes().getValue()).isEmpty()){
                    Toast.makeText(this, "Nothing to delete", Toast.LENGTH_SHORT).show();
                }
                else{

                    builder.setMessage("Delete all Tasks?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    noteViewModel.deleteAllNotes();




                                    Toast.makeText(DisplayTasksActivity.this, "all notes deleted", Toast.LENGTH_SHORT).show();
                                    //showCardAfterDelete();


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();


                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("Delete");
                    alert.show();


                }



                //adapter.notifyDataSetChanged();



                return true;


           default:
               return super.onOptionsItemSelected(item);

       }

    }




    }
