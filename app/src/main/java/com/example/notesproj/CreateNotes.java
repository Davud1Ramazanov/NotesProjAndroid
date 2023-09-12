package com.example.notesproj;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.gson.JsonObject;
import java.io.IOException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateNotes extends AppCompatActivity {

    EditText headerInput, textInput;
    Button addBtn;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notes);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://javaandroid07-001-site1.etempurl.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);

        headerInput = findViewById(R.id.headerInput);
        textInput = findViewById(R.id.textInput);
        addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(view -> {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.create_win_anim);
            addBtn.setAnimation(animation);
            String header = headerInput.getText().toString();
            String text = textInput.getText().toString();

            if (TextUtils.isEmpty(header) || TextUtils.isEmpty(text)) {
                Toast.makeText(CreateNotes.this, "Data is empty", Toast.LENGTH_SHORT).show();
            } else {
                int userId = userDataAuth();
                if (userId != -1) {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("userId", userId);
                    jsonObject.addProperty("header", header);
                    jsonObject.addProperty("text", text);

                    createNote(jsonObject);
                }
            }
        });
    }

    private void createNote(JsonObject jsonObject) {
        Call<Void> call = apiService.createNote(jsonObject);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CreateNotes.this, "Successful create note", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateNotes.this, "Note creation failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("CreateNotes", "Failed to create note", t);
                Toast.makeText(CreateNotes.this, "Failed to create note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int userDataAuth() {
        Call<Integer> call = apiService.userDataAuth();

        try {
            Response<Integer> response = call.execute();

            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            Log.e("CreateNotes", "Failed to get user data", e);
        }

        return -1;
    }
}
