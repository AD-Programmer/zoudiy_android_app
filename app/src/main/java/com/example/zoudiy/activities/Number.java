package com.example.zoudiy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zoudiy.R;
import com.example.zoudiy.utils.RetrofitClient;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Number extends AppCompatActivity {
    private EditText MobileNo;
    private String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_layout);

        MobileNo = (EditText) findViewById(R.id.editText3);
        MobileNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==10)
                {
                    mobileNo = "+91" + " " + MobileNo.getText().toString();
                    Call<ResponseBody> call = RetrofitClient
                            .getInstance()
                            .getApi()
                            .Sendotp(mobileNo);
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                String s = response.body().string();
                                Log.d("Success",s);
                                Intent intent= new Intent(Number.this,Login.class);
                                intent.putExtra("Mobile_no", mobileNo);
                                startActivity(intent);
                                //Toast.makeText(Login.this, s, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.d("Failure",t.toString());
                            //Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                }
                else if(s.length()<10)
                {
                    return;
                }
            }
        });
    }
}
