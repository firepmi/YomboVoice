package com.app.yombovoice.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.yombovoice.R;
import com.app.yombovoice.common.CustomActivity;
import com.app.yombovoice.common.Globals;
import com.app.yombovoice.common.Utils;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends CustomActivity
{

    private EditText user;
    private EditText pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTouchNClick(R.id.btnLogin);
        setTouchNClick(R.id.btnReg);

        if(ParseUser.getCurrentUser()!=null){
            Globals.currentUser = ParseUser.getCurrentUser();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);

    }

    @Override
    public void onClick(View v)
    {
        super.onClick(v);
        if (v.getId() == R.id.btnReg)
        {
            startActivityForResult(new Intent(this, SignupActivity.class), 10);
        }
        else
        {
            String u = user.getText().toString();
            String p = pwd.getText().toString();
            if (u.length() == 0 || p.length() == 0)
            {
                Utils.showDialog(this, R.string.err_fields_empty);
                return;
            }
            final ProgressDialog dia = ProgressDialog.show(this, null,
                    getString(R.string.alert_wait));
            ParseUser.logInInBackground(u, p, new LogInCallback() {

                @Override
                public void done(ParseUser pu, ParseException e)
                {
                    dia.dismiss();
                    if (pu != null)
                    {
                        Globals.currentUser = pu;
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                    else
                    {
                        Utils.showDialog( LoginActivity.this, getString(R.string.err_login) + " "+ e.getMessage());
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK)
            finish();
    }
}