package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Profile extends AppCompatActivity {
    private final String DEFAULT_AVATAR = "https://friconix.com/png/fi-cnsuxx-user-circle.png";
    private TextView tvUserName;

    private ImageView imageView;
    private EditText etUrl;
    private Button btnSave;
    private EditText etName;
    private EditText etEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent loginIntent = getIntent();

        tvUserName = findViewById(R.id.tvUserName);
        imageView = findViewById(R.id.imgUser);
        etUrl = findViewById(R.id.etAvatarURL);
        btnSave = findViewById(R.id.btnSave);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);

        String userName = loginIntent.getStringExtra("KEY_NAME");
        String userEmail = loginIntent.getStringExtra("KEY_EMAIL");
        tvUserName.setText(userName);
        etName.setText(userName);
        etEmail.setText(userEmail);

        loadAvatar(DEFAULT_AVATAR);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputUrl = etUrl.getText().toString().trim();
                if (!inputUrl.isEmpty()) {
                    loadAvatar(inputUrl);
                    Toast.makeText(Profile.this, "Updating user avatar...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadAvatar(String url) {
        Glide.with(Profile.this).load(url).into(imageView);
    }
}