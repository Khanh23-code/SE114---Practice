package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.myapplication.Model.User;

import java.util.List;

public class FriendAdapter extends ArrayAdapter<User> {
    private String currentUserEmail;

    public FriendAdapter(@NonNull Context context, @NonNull List<User> objects, String currentUserEmail) {
        super(context, 0, objects);
        this.currentUserEmail = currentUserEmail;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend_suggestion, parent, false);
        }

        User targetUser = getItem(position);
        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatarSuggestion);
        TextView tvName = convertView.findViewById(R.id.tvNameSuggestion);
        TextView tvPhone = convertView.findViewById(R.id.tvPhoneSuggestion);
        Button btnAdd = convertView.findViewById(R.id.btnAddFriend);

        if (targetUser != null) {
            tvName.setText(targetUser.getName());
            tvPhone.setText(targetUser.getPhoneNumber());

            String avatarUrl = "https://friconix.com/png/fi-cnsuxx-user-circle.png";
            Glide.with(getContext()).load(avatarUrl).circleCrop().into(imgAvatar);

            btnAdd.setOnClickListener(v -> {
                User currentUser = null;
                for (User u : Login.userList) {
                    if (u.getEmail().equals(currentUserEmail)) {
                        currentUser = u;
                        break;
                    }
                }

                if (currentUser != null) {
                    currentUser.getSentRequests().add(targetUser.getEmail());
                    targetUser.getPendingInvitations().add(currentUser.getEmail());

                    remove(targetUser);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Friend request sent!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return convertView;
    }
}