package com.railway.mmfile;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;
import com.railway.mmfile.file.FileUtils;
import com.railway.mmfile.file.Getfile;
import com.railway.mmfile.file.localFile;
import com.railway.mmfile.file.upload;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import com.railway.mmfile.file.User;

public class StartActivity extends AppCompatActivity {
    private static Context context;
    private static final int READ_REQUEST_CODE = 42;
    private QMUITopBarLayout topbar;
    private  List<myFile>myFileList=new ArrayList<>();
    public static List<localFile>localFileList= DataSupport.findAll(localFile.class);
    private RecyclerView myview;
    public static FileAdapter adapter;
    public static String user=new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=getApplicationContext();
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "7c74094c3170d3c648b9df4bbec8b334");
        //        连接数据库
        Connector.getDatabase();

        setContentView(R.layout.activity_start);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initFile();
        isLoginOrNot();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(this.getClass().toString(),"start add file");
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                //        intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
            }

        });
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        FileAdapter fileAdapter=new FileAdapter(localFileList);
        recyclerView.setAdapter(fileAdapter);
        myview=recyclerView;
        adapter=fileAdapter;

    }

    private  void initFile()
    {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        super.onActivityResult(requestCode,resultCode,resultData);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData()
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(this.getClass().toString(), "Uri: " + uri.toString());
                Toast.makeText(getApplicationContext(),"get uri:"+uri.toString(),
                        Toast.LENGTH_SHORT).show();
                localFile temp=new localFile();

                DocumentFile documentFile = DocumentFile.fromSingleUri(getApplicationContext(), uri);
//                final String filePath = getRealFilePath(getApplicationContext(),uri);
                final String filePath =FileUtils.getFilePathByUri(context,uri);
                temp.setLocate(filePath);
                temp.setStates(localFile.state.waitupload);
                temp.setName(documentFile.getName());
                temp.setTime(System.currentTimeMillis());
//                这里需要检查文件能否读取！！
                if(isImage(filePath.substring(filePath.lastIndexOf(".")+1)))
                {
                    temp.setFileType(localFile.type.imge);
                }
                else if (isDoc(filePath.substring(filePath.lastIndexOf(".")+1)))
                {
                    temp.setFileType(localFile.type.documents);
                }

                Log.i(this.getClass().toString(), "myFileList add!");

                Bitmap bitmap = BitmapFactory.decodeFile(temp.getLocate());
                upload upfiles=new upload(temp,uri);
//                异步实现文件的c上传（新建一个子线程）
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        upfiles.start();
                    }
                }).start();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Getfile.findfile();
            return true;
        }
        if(id==R.id.action_loginOut){
            BmobUser.logOut();
            Toast.makeText(StartActivity.this,"您已经登出当前帐号，请重新登录！"
                    ,Toast.LENGTH_SHORT).show();
            finish();
            Intent intent=new Intent(StartActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.action_updatePwd) {
            Intent intent=new Intent(StartActivity.this,UpdatePwdActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public static String getRealFilePath(final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
    public static boolean isImage(String type) {
        if (type != null
                && (type.equals("jpg") || type.equals("gif")
                || type.equals("png") || type.equals("jpeg")
                || type.equals("bmp") || type.equals("wbmp")
                || type.equals("ico") || type.equals("jpe"))) {
            return true;
        }
        return false;
    }
    public static boolean isDoc(String type) {
        if (type != null
                && (type.equals("doc") || type.equals("docx")
                || type.equals("ppt") || type.equals("pptx")
                || type.equals("xls") || type.equals("txt")
                || type.equals("xlsx") || type.equals("pdf"))) {
            return true;
        }
        return false;
    }
    public static Context getContext() {
                 return context;
           }
    private void isLoginOrNot(){

        //判断是否登录
        if (BmobUser.isLogin()) {
            User webuser = BmobUser.getCurrentUser(User.class);
            user=webuser.getObjectId();
            Log.i(this.toString(),"user:"+user.toString());
            Toast.makeText(StartActivity.this,"已经登录!您的用户名是："+webuser.getUsername(),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(StartActivity.this,"没有登录,请您先登陆!",Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
