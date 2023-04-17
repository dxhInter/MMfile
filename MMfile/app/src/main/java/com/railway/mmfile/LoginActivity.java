package com.railway.mmfile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.content.Intent;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.railway.mmfile.file.User;

public class LoginActivity extends AppCompatActivity {
	private EditText editText1;
	private EditText editText2;
	private Button buttonDL;
	private CheckBox checkBox;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Bmob.initialize(this, "953755c4d41cc0e6e5b8f4ba35382dca");
		editText1=(EditText) findViewById(R.id.editText1);
		editText2=(EditText)findViewById(R.id.editText2);
		buttonDL=(Button)findViewById(R.id.buttonDL);
		checkBox=(CheckBox)findViewById(R.id.CheckBox);
		buttonDL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Login();
			}
		});
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//如果选中，显示密码
					editText2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					//否则隐藏密码
					editText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
	}
	private void Login(){
		String username=editText1.getText().toString();
		String upwd=editText2.getText().toString();
		if(username.equals("") ||  upwd.equals("")){
			Toast.makeText(LoginActivity.this,"用户名、密码不能为空",Toast.LENGTH_SHORT).show();
			return;
		}
		final User user=new User();
		user.setUsername(username);
		user.setPassword(upwd);
		user.login(new SaveListener<User>() {
			@Override
			public void done(User user, BmobException e) {
				if (e == null) {
					User user1 = User.getCurrentUser(User.class);
					Toast.makeText(LoginActivity.this,"成功登录！",Toast.LENGTH_SHORT).show();
					finish();
					Intent intent=new Intent(LoginActivity.this,StartActivity.class);
					startActivity(intent);

				} else {
					Log.e("登录失败", "原因: ",e);
					if(e.getErrorCode()==101){
						Toast.makeText(LoginActivity.this,"登录失败！用户名或者密码不正确",Toast.LENGTH_SHORT).show();
					}
					else {
						Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
					}
					//finish();
				}
			}
		});
	}
	public void Register(View view){
		Intent intent=new Intent(this,RegisterActivity.class);
		startActivity(intent);
	}
}
