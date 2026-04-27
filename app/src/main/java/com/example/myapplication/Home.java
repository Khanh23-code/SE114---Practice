package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.User;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Comparator;
import java.util.Objects;

public class Home extends AppCompatActivity {

    private TextInputEditText etPostContent;
    private ListView lvPosts;

    public static List<Post> globalPostList = new ArrayList<>();

    private PostAdapter postAdapter;

    private String userName;
    private String avatarUrl;

    private boolean isSortedByDateAscending = false;
    private boolean isSortedByAuthorAscending = false;

    private List<Post> displayList;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etPostContent = findViewById(R.id.etPostContent);
        Button btnPost = findViewById(R.id.btnPost);
        lvPosts = findViewById(R.id.lvPosts);

        Intent profileIntent = getIntent();
        if (profileIntent != null) {
            userName = profileIntent.getStringExtra("KEY_NAME");
            avatarUrl = profileIntent.getStringExtra("KEY_AVATAR_URL");
        }

        postAdapter = new PostAdapter(this, globalPostList);
        lvPosts.setAdapter(postAdapter);
        postAdapter.notifyDataSetChanged();

        registerForContextMenu(lvPosts);

        assert profileIntent != null;
        String userEmail = profileIntent.getStringExtra("KEY_EMAIL");

        for (User u : Login.userList) {
            if (u.getEmail().equals(userEmail)) {
                currentUser = u;
                break;
            }
        }

        displayList = new ArrayList<>();
        postAdapter = new PostAdapter(this, displayList);
        lvPosts.setAdapter(postAdapter);

        refreshFeed();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = Objects.requireNonNull(etPostContent.getText()).toString().trim();

                if (content.isEmpty()) {
                    Toast.makeText(Home.this, "Please write something!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String currentDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());

                Post newPost = new Post(userName, currentDateTime, content, avatarUrl, userEmail);
                globalPostList.add(0, newPost);
                postAdapter.notifyDataSetChanged();
                refreshFeed();
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

            globalPostList.sort(new Comparator<Post>() {
                final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());

                @Override
                public int compare(Post p1, Post p2) {
                    try {
                        Date date1 = format.parse(p1.getDate());
                        Date date2 = format.parse(p2.getDate());

                        if (isSortedByDateAscending) {
                            assert date1 != null;
                            return date1.compareTo(date2);
                        } else {
                            assert date2 != null;
                            return date2.compareTo(date1);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return 0;
                    }
                }
            });
            refreshFeed();

            postAdapter.notifyDataSetChanged();

            return true;

        } else if (id == R.id.sortByAuthor) {
            isSortedByAuthorAscending = !isSortedByAuthorAscending;

            globalPostList.sort(new Comparator<Post>() {
                @Override
                public int compare(Post p1, Post p2) {
                    if (isSortedByAuthorAscending) {
                        return p1.getUserName().compareToIgnoreCase(p2.getUserName());
                    } else {
                        return p2.getUserName().compareToIgnoreCase(p1.getUserName());
                    }
                }
            });
            refreshFeed();

            postAdapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(android.view.ContextMenu menu, View v, android.view.ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.post_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@androidx.annotation.NonNull android.view.MenuItem item) {
        android.widget.AdapterView.AdapterContextMenuInfo info =
                (android.widget.AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        assert info != null;
        int position = info.position;
        Post selectedPost = globalPostList.get(position);

        int globalPosition = globalPostList.indexOf(selectedPost);
        int id = item.getItemId();

        if (id == R.id.menu_detail) {
            Intent intent = new Intent(Home.this, PostDetail.class);
            intent.putExtra("POST_POSITION", globalPosition);
            intent.putExtra("CURRENT_USER_EMAIL", currentUser.getEmail());
            startActivity(intent);
            return true;
        } else if (id == R.id.menu_hide) {
            if (currentUser != null) {
                currentUser.getHiddenPosts().add(selectedPost.getContent());

                refreshFeed();
            }
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void refreshFeed() {
        displayList.clear();
        for (Post p : globalPostList) {
            if (currentUser != null && !currentUser.getHiddenPosts().contains(p.getContent())) {
                displayList.add(p);
            }
        }
        postAdapter.notifyDataSetChanged();
    }
}