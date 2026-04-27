package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class PostDetail extends AppCompatActivity {
    private int postPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        MaterialToolbar toolbar = findViewById(R.id.toolbarDetail);
        ImageView imgAvatar = findViewById(R.id.imgAvatarDetail);
        TextView tvUserName = findViewById(R.id.tvUserNameDetail);
        TextView tvDate = findViewById(R.id.tvDateDetail);
        TextInputEditText etContent = findViewById(R.id.etContentDetail);
        Button btnSave = findViewById(R.id.btnSaveEdit);

        postPosition = getIntent().getIntExtra("POST_POSITION", -1);
        String currentUserEmail = getIntent().getStringExtra("CURRENT_USER_EMAIL");
        Post post = Home.globalPostList.get(postPosition);

        tvUserName.setText(post.getUserName());
        tvDate.setText(post.getDate());
        etContent.setText(post.getContent());
        Glide.with(this).load(post.getAvatarUrl()).circleCrop().into(imgAvatar);

        if (post.getOwnerEmail().equals(currentUserEmail)) {
            etContent.setEnabled(true);
            btnSave.setVisibility(View.VISIBLE);
        } else {
            etContent.setEnabled(false); 
            btnSave.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> {
            String newContent = Objects.requireNonNull(etContent.getText()).toString().trim();
            if (!newContent.isEmpty()) {
                Home.globalPostList.get(postPosition).setContent(newContent);
                Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        toolbar.setNavigationOnClickListener(v -> finish());
    }
}