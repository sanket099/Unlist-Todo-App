package com.chatapp.todoapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClick {


    NoteAdapter adapter;
    FloatingActionButton fab;
    ArrayList<Note> arrayList;
    int c = 0;
    private NoteViewModel noteViewModel;
    CoordinatorLayout coordinatorLayout;
    ImageView iv;
    RadioButton radioButton;
    CardView cardView;
    private int[] img_chill = new int[]{R.drawable.ic_doogiedoodle, R.drawable.ic_icecreamdoodle, R.drawable.ic_coffeedoddle};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardView = findViewById(R.id.cv_good);
        recyclerView.setHasFixedSize(true);
        iv = findViewById(R.id.iv_rest);
        adapter = new NoteAdapter(this,this);
        coordinatorLayout = findViewById(R.id.coordinator_layout);
        recyclerView.setAdapter(adapter);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);

                startActivityForResult(intent,1);
            }
        });





        //retireve view model

         //noteViewModel = new ViewModelProvider(this).get(NoteViewModel.class); //init

        noteViewModel = new ViewModelProvider(this
                      , ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication()))
                        .get(NoteViewModel.class);


        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) { //every time data changes
                adapter.submitList(notes);
                for(Note note :notes){
                    if (note.getDone()) {
                        c++;
                    }
                    else{
                        break;
                    }
                }
                if(c == notes.size()){
                    cardView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);

                    int rnd = new Random().nextInt(img_chill.length);
                    iv.setImageResource(rnd);
                }
                adapter.notifyDataSetChanged();
                System.out.println("changed");



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
                Toast.makeText(MainActivity.this, "Deleted", Toast.LENGTH_SHORT).show();



                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        noteViewModel.insert(item);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();


            }
        }).attachToRecyclerView(recyclerView);

        //add undo snackbar
        //add image
        /*adapter.setOnItemClickListener(new NoteAdapter.OnNoteClick() {
            @Override
            public void click(Note note) {
                Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
                intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority());
                startActivityForResult(intent, 2);
            }
        });
*/
    }

    @Override
    public void click(Note note) {
        Toast.makeText(this,note.getTitle(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
        intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getTitle());
        intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getDescription());
        intent.putExtra(AddNoteActivity.EXTRA_PRIORITY, note.getPriority());
        intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());
        System.out.println("idd" + note.getId());
        startActivityForResult(intent,2);



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            int pri = data.getIntExtra(AddNoteActivity.EXTRA_PRIORITY,1);
            String date = data.getStringExtra(AddNoteActivity.EXTRA_DATE);
            String time = data.getStringExtra(AddNoteActivity.EXTRA_TIME);
          //  Boolean done = data.getBooleanExtra(AddNoteActivity.EXTRA_DONE);


            Note note = new Note(title,desc,pri,date,time,false);
            noteViewModel.insert(note);

            //note saved

        }
        else if(requestCode == 2 && resultCode == RESULT_OK){
            adapter.notifyDataSetChanged();
          Intent intent = getIntent();
          System.out.println(intent);

            assert data != null;
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID,-1);
            String string = getIntent().getStringExtra(AddNoteActivity.EXTRA_TITLE);

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
          //  Boolean done = data.getBooleanExtra(AddNoteActivity.EXTRA_DONE);



            Note note = new Note(title,desc,pri,date,time,false);
            note.setId(id);
            noteViewModel.update(note);



            //note updated
            System.out.println("2");

        }
        else{
            //note not saved
            Toast.makeText(this, "Note NOT saved", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       switch (item.getItemId()){
           case R.id.deleteall:
               noteViewModel.deleteAllNotes();
               Toast.makeText(this, "all notes deleted", Toast.LENGTH_SHORT).show();
               return true;

           default:
               return super.onOptionsItemSelected(item);

       }

    }
}