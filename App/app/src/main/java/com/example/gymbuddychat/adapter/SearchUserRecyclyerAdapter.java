package com.example.gymbuddychat.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gymbuddychat.ChatActivity;
import com.example.gymbuddychat.R;
import com.example.gymbuddychat.model.UserModel;
import com.example.gymbuddychat.utils.AndoidUtil;
import com.example.gymbuddychat.utils.FirebaseUtil;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclyerAdapter extends FirestoreRecyclerAdapter<UserModel, SearchUserRecyclyerAdapter.UserModelViewHolder> {



    Context context;


    public SearchUserRecyclyerAdapter(@NonNull FirestoreRecyclerOptions<UserModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int i, @NonNull UserModel model) {
        holder.usernameText.setText(model.getUsername());
        holder.phoneText.setText(model.getPhone());
if(model.getUserId().equals(FirebaseUtil.currentUserId()))
{

    holder.usernameText.setText(model.getUsername()+"(ME)");
}


        FirebaseUtil.getOtherProfilePicStorageRef(model.getUserId()).getDownloadUrl()
                .addOnCompleteListener(t -> {
                    if(t.isSuccessful()){
                        Uri uri  = t.getResult();
                        AndoidUtil.setProfilePic(context,uri,holder.profilePic);
                    }
                });
holder.itemView.setOnClickListener(v->{
Intent intent=new Intent(context, ChatActivity.class);
    AndoidUtil.passUserModelAsIntent(intent,model);
intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
context.startActivity(intent);

});




    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView phoneText;
        ImageView profilePic;
        public  UserModelViewHolder(@NonNull View itemView)
        {
            super(itemView);

            usernameText=itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.profile_pic_image_view);
        }
    }


}
