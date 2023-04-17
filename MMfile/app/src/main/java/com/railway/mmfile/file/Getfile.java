package com.railway.mmfile.file;

import android.util.Log;

import com.railway.mmfile.FileAdapter;
import com.railway.mmfile.StartActivity;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Getfile {
    public static void findfile() {
        BmobQuery<UpFile> query = new BmobQuery<UpFile>();
        query.addWhereEqualTo("user", StartActivity.user);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //执行查询方法
        query.findObjects(new FindListener<UpFile>() {
            @Override
            public void done(List<UpFile> object, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：共" + object.size() + "条数据。");
                    for (UpFile oneupfile : object) {
                        int judge = 0;
                        localFile onefile;
                        try {
                            List<localFile> localFiles = DataSupport
                                    .where("fileid = ?",oneupfile.getObjectId()).find(localFile.class);
                            if (localFiles.size() != 0) {
                                onefile = localFiles.get(0);
                                judge = 1;
                            }
                            else {
                                onefile = new localFile();
                            }
                        }catch (Exception newe)
                        {
                            onefile = new localFile();
                        }
                        onefile.setName(oneupfile.getName());
                        onefile.setUrl(oneupfile.getUrl());
                        onefile.setFileType(oneupfile.getFileType());
                        onefile.setId(oneupfile.getObjectId());
                        onefile.setTime(oneupfile.getTime());
                        if (judge!=1)
                        {
                            onefile.setStates(localFile.state.online);
                            StartActivity.localFileList.add(onefile);
                        }
                        onefile.save();
                    }
                    StartActivity.adapter.notifyDataSetChanged();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });


    }


}
