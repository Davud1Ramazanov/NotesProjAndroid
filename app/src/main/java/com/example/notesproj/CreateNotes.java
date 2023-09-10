package com.example.notesproj;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateNotes extends AppCompatActivity {

    EditText headerInput, textInput, dateInput;
    Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_notes);

        headerInput = findViewById(R.id.headerInput);
        textInput = findViewById(R.id.textInput);
        dateInput = findViewById(R.id.dateInput);
        addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String header = headerInput.getText().toString();
                String text = textInput.getText().toString();
                String date = dateInput.getText().toString();

                if (TextUtils.isEmpty(header) || TextUtils.isEmpty(text) || TextUtils.isEmpty(date)) {
                    Toast.makeText(CreateNotes.this, "Data is empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Create JSON object
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("id", 0);
                    jsonObject.addProperty("userId", 0);
                    jsonObject.addProperty("header", header);
                    jsonObject.addProperty("text", text);
                    jsonObject.addProperty("date", date);

                    // Send JSON string to server
                    new CreateNoteTask().execute(jsonObject.toString());
                }
            }
        });
    }

    private class CreateNoteTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                String jsonData = params[0];
                int userId = userDataAuth();

                if (userId == -1) {
                    return false;
                }

                URL url = new URL("http://your-server-url.com/api/notes");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/json");

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(jsonData.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return true; // Note creation successful
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(CreateNotes.this, "Successful create note", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateNotes.this, "Note creation failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private int userDataAuth() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            URL urlReg = new URL("http://your-server-url.com/api/User/AuthroizationName"); // Replace with your server URL
            HttpURLConnection connection = (HttpURLConnection) urlReg.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                 BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                 StringBuilder response = new StringBuilder();
                 String line;
                 while ((line = reader.readLine()) != null) {
                     response.append(line);
                 }
                 reader.close();
                 int id = Integer.parseInt(response.toString());
                 return id;

            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return -1;
    }
}
