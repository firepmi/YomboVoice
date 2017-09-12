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
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends CustomActivity
{
	private EditText user;
	private EditText pwd;
	private EditText cpwd;
	private EditText phonenumber;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		setTouchNClick(R.id.btnReg);
		setTouchNClick(R.id.btnback);

		user = (EditText) findViewById(R.id.user);
		pwd = (EditText) findViewById(R.id.pwd);
		cpwd = (EditText) findViewById(R.id.confirmpwd);
		phonenumber = (EditText) findViewById(R.id.phonenumber);
	}

	@Override
	public void onClick(View v)
	{
		super.onClick(v);
		if(v.getId()==R.id.btnReg) {
			String u = user.getText().toString();
			String p = pwd.getText().toString();
			String cp = cpwd.getText().toString();
			String n = phonenumber.getText().toString();
			if (u.length() == 0 || p.length() == 0 || n.length() == 0 || cp.length() == 0 || n.length() == 0) {
				Utils.showDialog(this, R.string.err_fields_empty);
				return;
			}
			if (!p.equals(cp)) {
				Utils.showDialog(this, R.string.dont_match_password);
				return;
			}
			final ProgressDialog dia = ProgressDialog.show(this, null,
					getString(R.string.alert_wait));

			final ParseUser pu = new ParseUser();
			pu.setPassword(p);
			pu.setUsername(u);
			pu.signUpInBackground(new SignUpCallback() {

				@Override
				public void done(ParseException e) {
					dia.dismiss();
					if (e == null) {
						Globals.currentUser = pu;
						startActivity(new Intent(SignupActivity.this, MainActivity.class));
						setResult(RESULT_OK);
						finish();
					} else {
						Utils.showDialog(SignupActivity.this, getString(R.string.err_singup) + " " + e.getMessage());
						e.printStackTrace();
					}
				}
			});
		}else if (v.getId() == R.id.btnback) {
			finish();
		}
	}
}
