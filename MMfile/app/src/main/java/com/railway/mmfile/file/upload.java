package com.railway.mmfile.file;


import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.common.AutoZone;
import com.qiniu.android.http.ResponseInfo;

import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.railway.mmfile.StartActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class upload {
    private String filepath;
    private Uri uri;
    private localFile temp;
    private BmobFile bmobFile;

    public upload(localFile file, Uri uri)
    {
        temp = file;
        filepath = file.getLocate();
        this.uri = uri;
    }

    public void start()
    {
        //        bmobFile = new BmobFile(new File(filepath));
        //        bmobFile.uploadblock(new UploadFileListener() {
        //
        //            @Override
        //            public void done(BmobException e) {
        //                if(e==null){
        //                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
        //                    Toast.makeText(StartActivity.getContext(),"上传文件成功:" + bmobFile
        //                    .getFileUrl(),
        //                            Toast.LENGTH_SHORT);
        //                    temp.setStates(localFile.state.offline);
        //                    finish();
        //                }else{
        //                    Toast.makeText(StartActivity.getContext(),"上传文件失败：" + e.getMessage(),
        //                            Toast.LENGTH_SHORT);
        //                }
        //            }
        //            @Override
        //            public void onProgress(Integer value) {
        //                //返回的上传进度（百分比）
        //            }
        //        });
        String token = new String();
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://47.94.153.10:8000/mmfile/gettoken.php").build();
            Response response = client.newCall(request).execute();

            token = response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Configuration config = new Configuration.Builder().zone(new AutoZone()).build();
        UploadManager uploadManager = new UploadManager(config);
        String data = temp.getLocate();
        String key = null;
        data = FileUtils.getFilePathByUri(StartActivity.getContext(), uri);


        uploadManager.put(data, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject res) {
                //res包含hash、key等信息，具体字段取决于上传策略的设置
                if (info.isOK()) {
                    Log.i("qiniu", "Upload Success");
                    try {
                        finish(info.response.getString("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i("qiniu", "Upload Fail");
                    //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                }
                Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
            }
        }, null);

    }

    public void finish(String key)
    {
        //        temp.setUrl(bmobFile.getFileUrl());
        //        Bitmap bitmap = BitmapFactory.decodeFile(temp.getLocate());
        //        temp.setUrl(bitmaptoString(bitmap,50));

        temp.setUrl("http://mmfile2.zhang669.com/" + key);
        UpFile upfile = new UpFile();
        upfile.setFileType(temp.getFileType());
        upfile.setName(temp.getName());
        upfile.setTime(temp.getTime());
        upfile.setUrl(temp.getUrl());
        upfile.setUser(StartActivity.user);
        upfile.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Toast.makeText(StartActivity.getContext(), "创建数据成功：" + objectId,
                            Toast.LENGTH_SHORT);
                    temp.setId(objectId);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        temp.setStates(localFile.state.offline);
        temp.save();
        StartActivity.localFileList.add(temp);
        StartActivity.adapter.notifyDataSetChanged();
    }

    public String bitmaptoString(Bitmap bitmap, int bitmapQuality) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, bitmapQuality, bStream);
        byte[] bytes = bStream.toByteArray();
        string = Base64.encodeToString(bytes, Base64.DEFAULT);
        return string;

    }
    static public void datachanges(localFile temp)
    {
        UpFile p2 = new UpFile();
        p2.setName(temp.getName());
        p2.update(temp.getId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Log.i("upload","更新成功:"+p2.getUpdatedAt());
                }else{
                    Log.e("upload","更新失败：" + e.getMessage());
                }
            }
        });
    }
}
