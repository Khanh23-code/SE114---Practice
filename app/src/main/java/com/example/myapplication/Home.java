package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Collections;
import java.util.Comparator;

public class Home extends AppCompatActivity {

    private TextInputEditText etPostContent;
    private Button btnPost;
    private ListView lvPosts;

    public static List<Post> globalPostList = new ArrayList<>();

    private PostAdapter postAdapter;

    private String userName;
    private String avatarUrl;

    private boolean isSortedByDateAscending = false;
    private boolean isSortedByAuthorAscending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etPostContent = findViewById(R.id.etPostContent);
        btnPost = findViewById(R.id.btnPost);
        lvPosts = findViewById(R.id.lvPosts);

        Intent profileIntent = getIntent();
        if (profileIntent != null) {
            userName = profileIntent.getStringExtra("KEY_NAME");
            avatarUrl = profileIntent.getStringExtra("KEY_AVATAR_URL");
        }

        postAdapter = new PostAdapter(this, globalPostList);
        lvPosts.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etPostContent.getText().toString().trim();

                if (content.isEmpty()) {
                    Toast.makeText(Home.this, "Please write something!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

                Post newPost = new Post(userName, currentDateTime, content, avatarUrl);
                globalPostList.add(0, newPost);
                postAdapter.notifyDataSetChanged();
                etPostContent.setText("");

                lvPosts.smoothScrollToPosition(0);
                Toast.makeText(Home.this, "Posted successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull android.view.MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.profile) {
            Intent intent = new Intent(Home.this, Profile.class);

            intent.putExtra("KEY_NAME", userName);
            intent.putExtra("KEY_AVATAR_URL", avatarUrl);

            startActivity(intent);
            return true;

        } else if (id == R.id.sortByDate) {
            isSortedByDateAscending = !isSortedByDateAscending;

            Collections.sort(globalPostList, new Comparator<Post>() {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

                @Override
                public int compare(Post p1, Post p2) {
                    try {
                        Date date1 = format.parse(p1.getDate());
                        Date date2 = format.parse(p2.getDate());

                        if (isSortedByDateAscending) {
                            return date1.compareTo(date2);
                        } else {
                            return date2.compareTo(date1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });

            postAdapter.notifyDataSetChanged();

            return true;

        } else if (id == R.id.sortByAuthor) {
            isSortedByAuthorAscending = !isSortedByAuthorAscending;

            Collections.sort(globalPostList, new Comparator<Post>() {
                @Override
                public int compare(Post p1, Post p2) {
                    if (isSortedByAuthorAscending) {
                        return p1.getUserName().compareToIgnoreCase(p2.getUserName());
                    } else {
                        return p2.getUserName().compareToIgnoreCase(p1.getUserName());
                    }
                }
            });

            postAdapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}