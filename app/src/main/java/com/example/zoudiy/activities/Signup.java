package com.example.zoudiy.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zoudiy.R;
import com.example.zoudiy.models.UpdateProfileResponse;
import com.example.zoudiy.utils.Preference;
import com.example.zoudiy.utils.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Signup extends AppCompatActivity {

    Button Done;
    EditText name,email;
    String token,emailid,fullname;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Done= (Button)findViewById(R.id.buttonRegister);

        name=(EditText) findViewById(R.id.name);
        email=(EditText) findViewById(R.id.email);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                token= Preference.getAccessToken(Signup.this);
                emailid=email.getText().toString();
                fullname=name.getText().toString();
                Call<UpdateProfileResponse> call=RetrofitClient
                        .getInstance()
                        .getApi()
                        .Updateprofile(emailid,fullname,token);
                Log.d("token",token);
                call.enqueue(new Callback<UpdateProfileResponse>() {
                    @Override
                    public void onResponse(Call<UpdateProfileResponse> call, Response<UpdateProfileResponse> response) {
                        UpdateProfileResponse responseBody = response.body();
                        Log.d("profile response",responseBody.getMessage()+" "+responseBody.getSuccess());
                        if (responseBody.getSuccess() == true){
                            Toast.makeText(Signup.this,"Profile Saved",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(Signup.this, HomeActivity.class);
                            startActivity(intent);
                            Log.d("yo here","jsanfsanfkasf");
                        }
                        else{
                            Toast.makeText(Signup.this,"Incorrect Data",Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<UpdateProfileResponse> call, Throwable t) {
                        Log.d("Error",t.toString());
                    }
                });
            }
        });


    }
    @Override
    public void onBackPressed(){

        return;
    }
}
