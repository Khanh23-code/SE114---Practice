package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.myapplication.Model.User;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class FriendSuggestion extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int READ_CONTACTS_REQUEST_CODE = 100;
    private static final int CONTACT_LOADER = 1;

    private ListView lvSuggestions;
    private FriendAdapter adapter;
    private List<User> suggestedUsers;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_suggestion);

        MaterialToolbar toolbar = findViewById(R.id.toolbarSuggestion);
        toolbar.setNavigationOnClickListener(v -> finish());
        lvSuggestions = findViewById(R.id.lvSuggestions);

        String currentUserEmail = getIntent().getStringExtra("CURRENT_USER_EMAIL");
        for (User u : Login.userList) {
            if (u.getEmail().equals(currentUserEmail)) {
                currentUser = u;
                break;
            }
        }

        suggestedUsers = new ArrayList<>();
        adapter = new FriendAdapter(this, suggestedUsers, currentUserEmail);
        lvSuggestions.setAdapter(adapter);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            LoaderManager.getInstance(this).restartLoader(CONTACT_LOADER, null, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CONTACTS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoaderManager.getInstance(this).restartLoader(CONTACT_LOADER, null, this);
            } else {
                Toast.makeText(this, "Permission denied! Cannot load friends.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == CONTACT_LOADER) {
            String[] SELECTED_FIELDS = new String[] {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            };
            return new CursorLoader(this, ContactsContract.CommonDataKinds.Phone.CONTENT_URI, SELECTED_FIELDS, null, null, null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == CONTACT_LOADER) {
            adapter.clear();

            if (data != null && currentUser != null) {
                while (!data.isClosed() && data.moveToNext()) {
                    String phone = data.getString(1);

                    String normalizedPhone = phone.replaceAll("[\\s\\-]", "").replace("+84", "0");

                    for (User appUser : Login.userList) {
                        if (appUser.getEmail().equals(currentUser.getEmail())) continue;

                        String appUserPhone = appUser.getPhoneNumber();
                        if (appUserPhone != null) {
                            String normAppPhone = appUserPhone.replaceAll("[\\s\\-]", "").replace("+84", "0");

                            if (normalizedPhone.equals(normAppPhone)) {
                                boolean isAlreadyFriend = currentUser.getFriends().contains(appUser.getEmail());
                                boolean isPending = currentUser.getPendingInvitations().contains(appUser.getEmail());
                                boolean isSent = currentUser.getSentRequests().contains(appUser.getEmail());

                                if (!isAlreadyFriend && !isPending && !isSent && !suggestedUsers.contains(appUser)) {
                                    adapter.add(appUser);
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                data.close();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.clear();
    }
}