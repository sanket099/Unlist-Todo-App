package com.chatapp.todoapp;

import android.content.Context;
import android.graphics.Paint;
import android.icu.lang.UProperty;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class NoteAdapter extends ListAdapter<Note,NoteAdapter.ViewHolder> {
   // private List<Note> noteArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    OnNoteClick onNoteClick;
    private OnNoteClick listener;
    SharedPreference sharedPreference ;
    Context context;

    public NoteAdapter( Context context, OnNoteClick onNoteClick) {
        super(DIFF_CALLBACK);
        //*this.noteArrayList = notes;*//*
        inflater =LayoutInflater.from(context);
        this.onNoteClick = onNoteClick;
        this.context = context;

        sharedPreference = new SharedPreference(context);

    }
    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription())
                    && oldItem.getPriority() == newItem.getPriority();
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

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.ViewHolder holder, int position) {


        holder.title.setText(getItem(position).getTitle());
        holder.radioButton.setChecked(getItem(position).getDone());

        holder.date.setText(getItem(position).getDate() + ", " + getItem(position).getTime());
        holder.desc.setText(getItem(position).getDescription());
        holder.priority.setText(String.valueOf(getItem(position).getPriority()));

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



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, desc, priority, date;
        OnNoteClick onNoteClick;
        RadioButton radioButton;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView, OnNoteClick onNoteClick) {
            super(itemView);

            title = itemView.findViewById(R.id.tv_title);
            desc = itemView.findViewById(R.id.tv_desc);
            priority =itemView.findViewById(R.id.tv_pri);
            radioButton = itemView.findViewById(R.id.rb_done);
            imageView = itemView.findViewById(R.id.undo);



            date = itemView.findViewById(R.id.tv_date);


            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        radioButton.setVisibility(View.GONE);
                        imageView.setVisibility(View.VISIBLE);



                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    title.setPaintFlags(0);
                    radioButton.setVisibility(View.VISIBLE);
                    imageView.setVisibility(View.GONE);
                    radioButton.setChecked(false);
                }
            });

            this.onNoteClick = onNoteClick;
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if(getAdapterPosition() != -1) {
                onNoteClick.click(getItem(getAdapterPosition()));
            }
           /* int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.click(noteArrayList.get(position));
            }*/
        }

    }
    public  interface OnNoteClick{
            void click(Note note);
    }
   /* public void setOnItemClickListener(OnNoteClick listener) {
        this.listener = listener;
    }*/
}
