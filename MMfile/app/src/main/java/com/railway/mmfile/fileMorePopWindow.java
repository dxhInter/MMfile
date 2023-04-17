package com.railway.mmfile;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.railway.mmfile.file.localFile;
import com.railway.mmfile.file.upload;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;


public class fileMorePopWindow extends Activity implements View.OnClickListener {
    private Button btn_download,btn_preview,btn_more_info,btn_cancel,btn_delete,btn_rename;
    private LinearLayout layout;
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_more_popup);
        btn_cancel=(Button)this.findViewById(R.id.btn_cancel);
        btn_download=(Button)this.findViewById(R.id.btn_download);
        btn_more_info=(Button)this.findViewById(R.id.btn_more_info);
        btn_preview=(Button)this.findViewById(R.id.btn_preview);
        btn_delete=(Button)this.findViewById(R.id.btn_delete);
        btn_rename=(Button)this.findViewById(R.id.btn_rename);
        layout=(LinearLayout)findViewById(R.id.pop_layout);
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(), "提示：点击窗口外部关闭窗口！",
                        Toast.LENGTH_SHORT).show();
            }
        });
        //添加按钮监听
        btn_cancel.setOnClickListener(this);
        btn_download.setOnClickListener(this);
        btn_more_info.setOnClickListener(this);
        btn_preview.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_rename.setOnClickListener(this);
    }

    //实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event){
        finish();
        return true;
    }
    public void onClick(View v) {
        final localFile temp=FileAdapter.myFileList.get(FileAdapter.temppostion);
        switch (v.getId()) {
            case R.id.btn_download:
                Toast.makeText(v.getContext(),"you clicked button btn_download",
                        Toast.LENGTH_SHORT).show();
                finish();
                int state=temp.getStates();
                //用于判断文件的状态
                if(state==1) {
                    String name = temp.getName();
                    String url = temp.getUrl();
					BmobFile bmobFile = new BmobFile(name, "", url);
					downloadFile(bmobFile);
				}
                else if(state==2){
                	Toast.makeText(fileMorePopWindow.this,"您已经下载到/storage/emulated/0/Download文件夹下,无需重新下载",
							Toast.LENGTH_SHORT).show();
				}
                break;
            case R.id.btn_more_info:
                Toast.makeText(v.getContext(),"you clicked button btn_more_info",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_cancel:
                Toast.makeText(v.getContext(),"you clicked button btn_cancel",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_preview:
                Toast.makeText(v.getContext(),"you clicked button btn_preview",
                        Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btn_delete:
                FileAdapter.myFileList.remove(FileAdapter.temppostion);
                temp.delete();
                StartActivity.adapter.notifyDataSetChanged();
                finish();
                break;
            case R.id.btn_rename:
                final QMUIDialog.EditTextDialogBuilder builder =
                        new QMUIDialog.EditTextDialogBuilder(this);
                builder.setTitle("重命名")
                        .setSkinManager(QMUISkinManager.defaultInstance(this))
                        .setPlaceholder("在此输入新文件名")
                        .setInputType(InputType.TYPE_CLASS_TEXT)
                        .addAction("取消", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                dialog.dismiss();
                            }
                        })
                        .addAction("确定", new QMUIDialogAction.ActionListener() {
                            @Override
                            public void onClick(QMUIDialog dialog, int index) {
                                CharSequence text = builder.getEditText().getText();
                                if (text != null && text.length() > 0) {
                                    Toast.makeText(getApplicationContext(),  text, Toast.LENGTH_SHORT).show();
                                    temp.setName(text.toString());
                                    temp.save();
                                    StartActivity.adapter.notifyDataSetChanged();
                                    upload.datachanges(temp);
                                    dialog.dismiss();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "请填入文件名",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create(mCurrentDialogStyle).show();
                break;
            default:
                break;
        }
//        finish();
    }
    private void downloadFile(BmobFile file){
        File saveFile = new File(Environment.getExternalStorageDirectory().getPath()+"/Download",
                file.getFilename());
        file.download(saveFile, new DownloadFileListener() {
            @Override
            public void onStart() {
                Toast.makeText(fileMorePopWindow.this,"开始下载...",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void done(String savePath, BmobException e) {
                if(e==null){
                    Toast.makeText(fileMorePopWindow.this,"下载成功,保存路径:"+savePath,Toast.LENGTH_SHORT).show();
                }else{
					Log.i("bmob","错误："+e.getErrorCode());
                    Toast.makeText(fileMorePopWindow.this,"下载失败："+e.getErrorCode()+","+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }
        });
    }
}
