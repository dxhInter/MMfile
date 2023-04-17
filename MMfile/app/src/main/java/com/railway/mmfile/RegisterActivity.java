package com.railway.mmfile;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import com.railway.mmfile.file.User;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity{
	private EditText editTextR1;
	private EditText editTextR2;
	private EditText editTextR3;
	private EditText editTextR4;
	private Button buttonR1;
	private CheckBox checkBox1;
	private CheckBox checkBox2;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resgister);
		Bmob.initialize(this, "953755c4d41cc0e6e5b8f4ba35382dca");
		editTextR1=findViewById(R.id.editTextR1);
		editTextR2=findViewById(R.id.editTextX1);
		editTextR3=findViewById(R.id.editTextX2);
		editTextR4=findViewById(R.id.editTextR4);
		checkBox1=findViewById(R.id.CheckBox1);
		checkBox2=findViewById(R.id.CheckBox2);
		buttonR1=findViewById(R.id.buttonX1);
		buttonR1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Register();
			}
		});
		checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//如果选中，显示密码
					editTextR2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					//否则隐藏密码
					editTextR2.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//如果选中，显示密码
					editTextR3.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					//否则隐藏密码
					editTextR3.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
	}
	private void Register(){
		String username=editTextR1.getText().toString();
		String upwd=editTextR2.getText().toString();
		String upwdConfirm=editTextR3.getText().toString();
		String nickname=editTextR4.getText().toString();
		if(username.equals("") ||  upwd.equals("") || nickname.equals("")){
			Toast.makeText(RegisterActivity.this,"用户名、密码和昵称不能为空",Toast.LENGTH_SHORT).show();
			return;
		}
		if(!upwd.equals(upwdConfirm)){
			Toast.makeText(RegisterActivity.this,"两次密码输入不一致",Toast.LENGTH_SHORT).show();
			return;
		}
		User user=new User();
		user.setUsername(username);
		user.setPassword(upwd);
		user.setNickname(nickname);
		user.signUp(new SaveListener<User>(){
			@Override
			public void done(User user, BmobException e) {
				if (e == null) {
					Toast.makeText(RegisterActivity.this,"成功注册！您的用户名是"+username,Toast.LENGTH_SHORT).show();
					finish();
					Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
					startActivity(intent);
				} else {
					Log.e("注册失败", "原因: ",e );
					if(e.getErrorCode()==202) {
						Toast.makeText(RegisterActivity.this, "注册失败!该用户已被注册" , Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}
}
