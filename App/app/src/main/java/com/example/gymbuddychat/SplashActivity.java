package com.example.gymbuddychat;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.os.Handler;
import android.view.WindowManager;

import com.example.gymbuddychat.model.UserModel;
import com.example.gymbuddychat.utils.AndoidUtil;
import com.example.gymbuddychat.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FirebaseUtil.isLoggedIn()&&getIntent().getExtras()!=null)
        {
            //from not
            String userId=getIntent().getExtras().getString("userId");
            FirebaseUtil.allUserCollectionReference().document(userId).get()
                    .addOnCompleteListener(task->{
                        if(task.isSuccessful()){
                            UserModel model=task.getResult().toObject(UserModel.class);

                            Intent mainIntent=new Intent(this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(mainIntent);
                            Intent intent = new Intent(this, ChatActivity.class);

                            AndoidUtil.passUserModelAsIntent(intent,model);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });
        }else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(FirebaseUtil.isLoggedIn())
                    {
//                      //  startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));

                    }

                    else
                    {
                        startActivity(new Intent(SplashActivity.this, LoginPhoneNumberActivity.class));
                    }
                    finish();
                }
            },1000);

        };
    }




}




