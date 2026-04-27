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
    private Button btnSave;
    private Button btnLogout;
    private Button btnHome;
    private EditText etName;
    private EditText etEmail;
    private EditText etUrl;

    private EditText etAddress;
    private EditText etDescription;

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
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        btnHome = findViewById(R.id.btnHome);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUrl = findViewById(R.id.etAvatarURL);
        etAddress = findViewById(R.id.etAddress);
        etDescription = findViewById(R.id.etDescription);

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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Register.class);
                startActivity(intent);
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, Home.class);
                intent.putExtra("KEY_NAME", userName);
                intent.putExtra("KEY_EMAIL", userEmail);
                String url = etUrl.getText().toString().trim().isEmpty() ? DEFAULT_AVATAR : etUrl.getText().toString().trim();
                intent.putExtra("KEY_AVATAR_URL", url);
                intent.putExtra("KEY_ADDRESS", etAddress.getText().toString().trim());
                intent.putExtra("KEY_DESCRIPTION", etDescription.getText().toString().trim());
                startActivity(intent);
            }
        });
    }

    private void loadAvatar(String url) {
        Glide.with(Profile.this).load(url).into(imageView);
    }
}