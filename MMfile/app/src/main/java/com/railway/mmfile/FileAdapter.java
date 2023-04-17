package com.railway.mmfile;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.railway.mmfile.file.localFile;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    public static List<localFile> myFileList;
    public static int temppostion;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView fileImage;
        TextView fileName;
        Button fileButton;
        View fileView;

        public ViewHolder(View view)
        {
            super(view);
            fileView=view;
            fileImage = (ImageView) view.findViewById(R.id.file_image);
            fileName = (TextView) view.findViewById(R.id.file_name);
            fileButton = (Button) view.findViewById(R.id.file_setting);
        }
    }

    public FileAdapter(List<localFile> FileList)
    {
        this.myFileList=FileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from((parent.getContext())).inflate(R.layout.file_item,parent,
                false);
        final ViewHolder holder=new ViewHolder(view);
//        事件监听
        holder.fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= holder.getAdapterPosition();
                temppostion=position;
                localFile myfile=myFileList.get(position);
                Toast.makeText(v.getContext(),"you clicked button "+myfile.getName(),
                        Toast.LENGTH_SHORT).show();
                view.getContext().startActivity(new Intent(v.getContext(),
                        fileMorePopWindow.class));
            }
        });
        holder.fileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position= holder.getAdapterPosition();
                localFile myfile=myFileList.get(position);
                Toast.makeText(v.getContext(),"you clicked view "+myfile.getName(),
                        Toast.LENGTH_SHORT).show();
//                if (myfile.getUrl().compareTo("")!=0)
//                {
//                    Intent intent=new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(myfile.getUrl()));
//                    view.getContext().startActivity(intent);
//                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        localFile file=myFileList.get(position);
        holder.fileImage.setImageResource(getImageId(file.getFileType()));
        holder.fileName.setText(file.getName());
    }
    private int getImageId(int types)
    {
        if (types==localFile.type.imge)
        {
            return R.mipmap.pic;
        }
        else if (types==localFile.type.documents)
        {
            return R.mipmap.file;
        }
        return 0;
    }
    @Override
    public int getItemCount() {
        return myFileList.size();
    }

}
