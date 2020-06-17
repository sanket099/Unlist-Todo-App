package com.chatapp.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.icu.lang.UProperty;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static android.media.CamcorderProfile.get;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.ViewHolder> {
   // private List<Note> noteArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    OnNoteClick onNoteClick;
    private OnNoteClick listener;
   // SharedPreference sharedPreference ;
   private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    int c = 0;
    Context context;

    NoteViewModel noteViewModel;



    public NoteAdapter( Context context, OnNoteClick onNoteClick) {
        super(DIFF_CALLBACK);
        //*this.noteArrayList = notes;*//*
        inflater =LayoutInflater.from(context);
        this.onNoteClick = onNoteClick;
        this.context = context;

       // sharedPreference = new SharedPreference(context);

        noteViewModel = new ViewModelProvider((DisplayTasksActivity) context).get(NoteViewModel.class); //init

    }
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority() && oldItem.getDone().equals(newItem.getDone()) && oldItem.getDate().equals(newItem.getDate()) &&
                    oldItem.getTime().equals(newItem.getTime());
        }
    };

    /*public NoteAdapter(){

    }*/
    @NonNull
    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(itemView, onNoteClick);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        date = dateFormat.format(calendar.getTime());
        System.out.println("date = " + date);
        holder.title.setText(getItem(position).getTitle());
        holder.radioButton.setChecked(getItem(position).getDone());
        System.out.println("heelp");
        System.out.println(getItem(position).getDate());
        if((getItem(position).getDate()).equals(date)){
            System.out.println("1");
            holder.date.setText("Today , " + getItem(position).getTime());
        }
        else {
            System.out.println("2");
            holder.date.setText(getItem(position).getDate() + ", " + getItem(position).getTime());
        }
        holder.desc.setText(getItem(position).getDescription());
        holder.priority.setText(String.valueOf(getItem(position).getPriority()));

        if(holder.radioButton.isChecked()){
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.radioButton.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
        }
        else{
            holder.title.setPaintFlags(0);
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
        }


      /*  for(Note note : getCurrentList()){

            System.out.println(note.getDone());
            if(note.getDone()){
                holder.radioButton.setChecked(true);
                holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                //getItem(getAdapterPosition()).setDone(true);
                holder.radioButton.setVisibility(View.GONE);
                holder.imageView.setVisibility(View.VISIBLE);

                System.out.println("here i am");

            }

        }
*/
       /* if(sharedPreference.get_flag() == getCurrentList().size()){
            System.out.println(getItem(position).getDone());
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.radioButton.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.radioButton.setChecked(true);
        }
*/


        System.out.println("-----------");
        if(isDone()){
            System.out.println("bind done");
        }






       /* if(holder.radioButton.isChecked()){
            holder.title.setPaintFlags(holder.title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            notifyDataSetChanged();
        }*/

    }


   /* public void setNotes(List<Note> notes) {
        this.noteArrayList = notes;
        notifyDataSetChanged();
    }*/

    public Note getNoteAt(int position){
        return getItem(position);
    }


    public Boolean isDone(){
        boolean done = false;
        for(Note note: getCurrentList()){
           if(note.getDone() && getCurrentList().size() != 0){

               done = true;
           }
           else{
               done = false;
               break;
           }
        }
        return done;


    }





    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, priority, date;
        OnNoteClick onNoteClick;
        RadioButton radioButton;
        ImageView imageView;

        public ViewHolder(@NonNull final View itemView, OnNoteClick onNoteClick) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.tv_desc);
            priority =itemView.findViewById(R.id.tv_pri);
            radioButton = itemView.findViewById(R.id.rb_done);
            imageView = itemView.findViewById(R.id.undo);
            this.onNoteClick = onNoteClick;
            itemView.setOnClickListener(this);



            date = itemView.findViewById(R.id.tv_date);
            /*if(radioButton.isChecked()){
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                radioButton.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                getItem(getAdapterPosition()).setDone(true);
                radioButton.setChecked(true);

            }*/

            /*if(getItem(getAdapterPosition()).getDone()){
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                radioButton.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                getItem(getAdapterPosition()).setDone(true);
                radioButton.setChecked(true);

            }
*/

            radioButton.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {



                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        getItem(getAdapterPosition()).setDone(true);
                        radioButton.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);

                        radioButton.setChecked(true);
                        System.out.println(getItem(getAdapterPosition()).getDone());
                        noteViewModel.update(getItem(getAdapterPosition()));

                        if(isDone()){
                            //Toast.makeText(context, "cv :all done", Toast.LENGTH_SHORT).show();
                            if (context instanceof DisplayTasksActivity) {
                                ((DisplayTasksActivity)context).showFinishedCard();}

                        }


                        //sharedPreference.save_flag(3);

                   /* for(Note note : getCurrentList()){
                        if (note.getDone()) {
                            c++;
                        }
                        else{
                            break;
                        }
                    }*/
                   /* sharedPreference.del_flag();
                    sharedPreference.save_flag(c);*/
                        //  System.out.println("here "+sharedPreference.get_flag());

                  /*  if(isDone()){


                        if (context instanceof DisplayTasksActivity) {
                            ((DisplayTasksActivity)context).cardView.setVisibility(View.VISIBLE);
                            ((DisplayTasksActivity)context).recyclerView.setVisibility(View.GONE);
                            ((DisplayTasksActivity)context).tv_good.setText("Good JOB");
                            ((DisplayTasksActivity)context).tv_desc.setText("Youve completed all your tasks");
                            ((DisplayTasksActivity)context).iv.setImageResource(R.drawable.ic_sitreadingdoodle);
                            ((DisplayTasksActivity)context).tv_view_completed.setVisibility(View.VISIBLE);

                            System.out.println("hey there");

                        }
                        else{
                            System.out.println("lmao");
                        }

                    }
*/




                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    title.setPaintFlags(0);
                    radioButton.setVisibility(View.VISIBLE);
                    getItem(getAdapterPosition()).setDone(false);
                    imageView.setVisibility(View.GONE);
                    radioButton.setChecked(false);
                    noteViewModel.update(getItem(getAdapterPosition()));
                    if(!isDone()){
                        System.out.println(isDone());
                        if (context instanceof DisplayTasksActivity) {
                            ((DisplayTasksActivity)context).hideCard();}


                        }


                }
            });


/*
           for(Note note : getCurrentList()){
                if (note.getDone()) {
                    c++;
                }
                else{
                    break;
                }
            }
            if(c == getCurrentList().size() && getCurrentList().size() != 0){
                System.out.println("count" + c);
                sharedPreference.del_flag();
                sharedPreference.save_flag(1);
                System.out.println("here "+sharedPreference.get_flag());





            }
            else{
                System.out.println("here2 "+sharedPreference.get_flag());
                sharedPreference.del_flag();
                sharedPreference.save_flag(2); //recycler show

            }
            *//*notifyDataSetChanged();
            System.out.println("changed");*//*

            this.onNoteClick = onNoteClick;
            ;*/


        }



        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != -1) {
                onNoteClick.click(getItem(getAdapterPosition()));
            }

        }

    }
    public  interface OnNoteClick{
            void click(Note note);
    }

   /* public void setOnItemClickListener(OnNoteClick listener) {
        this.listener = listener;
    }*/



}
