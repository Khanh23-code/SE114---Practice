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

public class InvitationAdapter extends ArrayAdapter<User> {
    private String currentUserEmail;

    public InvitationAdapter(@NonNull Context context, @NonNull List<User> objects, String currentUserEmail) {
        super(context, 0, objects);
        this.currentUserEmail = currentUserEmail;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend_invitation, parent, false);
        }

        User inviter = getItem(position);
        ImageView imgAvatar = convertView.findViewById(R.id.imgAvatarInviter);
        TextView tvName = convertView.findViewById(R.id.tvNameInviter);
        TextView tvPhone = convertView.findViewById(R.id.tvPhoneInviter);
        Button btnAccept = convertView.findViewById(R.id.btnAccept);
        Button btnReject = convertView.findViewById(R.id.btnReject);

        if (inviter != null) {
            tvName.setText(inviter.getName());
            tvPhone.setText(inviter.getPhoneNumber());

            String avatarUrl = "https://friconix.com/png/fi-cnsuxx-user-circle.png";
            Glide.with(getContext()).load(avatarUrl).circleCrop().into(imgAvatar);

            User currentUser = null;
            for (User u : Login.userList) {
                if (u.getEmail().equals(currentUserEmail)) {
                    currentUser = u;
                    break;
                }
            }

            if (currentUser != null) {
                User finalCurrentUser = currentUser;

                btnAccept.setOnClickListener(v -> {
                    finalCurrentUser.getPendingInvitations().remove(inviter.getEmail());
                    inviter.getSentRequests().remove(finalCurrentUser.getEmail());

                    finalCurrentUser.getFriends().add(inviter.getEmail());
                    inviter.getFriends().add(finalCurrentUser.getEmail());

                    remove(inviter);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "You and " + inviter.getName() + " are now friends!", Toast.LENGTH_SHORT).show();
                });

                btnReject.setOnClickListener(v -> {
                    finalCurrentUser.getPendingInvitations().remove(inviter.getEmail());
                    inviter.getSentRequests().remove(finalCurrentUser.getEmail());

                    remove(inviter);
                    notifyDataSetChanged();
                    Toast.makeText(getContext(), "Request rejected", Toast.LENGTH_SHORT).show();
                });
            }
        }
        return convertView;
    }
}