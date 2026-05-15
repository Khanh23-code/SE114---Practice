package com.example.myapplication;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Model.User;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class FriendInvitation extends AppCompatActivity {

    private ListView lvInvitations;
    private InvitationAdapter adapter;
    private List<User> invitationList;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_invitation);

        MaterialToolbar toolbar = findViewById(R.id.toolbarInvitation);
        toolbar.setNavigationOnClickListener(v -> finish());

        lvInvitations = findViewById(R.id.lvInvitations);

        String currentUserEmail = getIntent().getStringExtra("CURRENT_USER_EMAIL");

        for (User u : Login.userList) {
            if (u.getEmail().equals(currentUserEmail)) {
                currentUser = u;
                break;
            }
        }

        invitationList = new ArrayList<>();
        if (currentUser != null) {
            List<String> pendingEmails = currentUser.getPendingInvitations();
            for (User u : Login.userList) {
                if (pendingEmails.contains(u.getEmail())) {
                    invitationList.add(u);
                }
            }
        }

        adapter = new InvitationAdapter(this, invitationList, currentUserEmail);
        lvInvitations.setAdapter(adapter);
    }
}