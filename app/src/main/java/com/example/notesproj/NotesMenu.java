package com.example.notesproj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class NotesMenu extends AppCompatActivity {

    Button createNotesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_menu);
    }

    public void createNotesWindow(View view) {
        Intent intent = new Intent(this, CreateNotes.class);
        startActivity(intent);
    }
}