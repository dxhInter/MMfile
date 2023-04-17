package com.railway.mmfile;
import androidx.appcompat.app.AppCompatActivity;
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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.railway.mmfile.file.User;

import androidx.annotation.Nullable;
public class UpdatePwdActivity extends AppCompatActivity {
	private EditText editTextX1;
	private EditText editTextX2;
	private Button buttonX1;
	private CheckBox checkBox1;
	private CheckBox checkBox2;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_updatepwd);
		Bmob.initialize(this, "7c74094c3170d3c648b9df4bbec8b334");
		editTextX1 = findViewById(R.id.editTextX1);
		editTextX2 = findViewById(R.id.editTextX2);
		checkBox1 = findViewById(R.id.CheckBox1);
		checkBox2 = findViewById(R.id.CheckBox2);
		buttonX1 = findViewById(R.id.buttonX1);
		buttonX1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				UpdatePwd();
			}
		});
		checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//如果选中，显示密码
					editTextX1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					//否则隐藏密码
					editTextX1.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
		checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					//如果选中，显示密码
					editTextX2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				} else {
					//否则隐藏密码
					editTextX2.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
			}
		});
	}

	private void UpdatePwd() {
		String oldPwd = editTextX1.getText().toString();
		String newPwd = editTextX2.getText().toString();
		if (oldPwd.equals("") || newPwd.equals("")) {
			Toast.makeText(UpdatePwdActivity.this, "旧密码和新密码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		BmobUser.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
			@Override
			public void done(BmobException e) {
				if (e == null) {
					Toast.makeText(UpdatePwdActivity.this,"修改成功,请重新登录"
							,Toast.LENGTH_SHORT).show();
					finish();
					Intent intent=new Intent(UpdatePwdActivity.this,LoginActivity.class);
					startActivity(intent);
				} else {
					Log.e("修改失败", "原因: ",e );
					if(e.getErrorCode()==210) {
						Toast.makeText(UpdatePwdActivity.this, "修改失败,旧密码错误"
								, Toast.LENGTH_SHORT).show();
						finish();
					}
					else {
						Toast.makeText(UpdatePwdActivity.this, "修改失败!"
								, Toast.LENGTH_SHORT).show();
						finish();
					}
				}
			}
		});
	}
}