package com.example.iiitb_connects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private ArrayList<ProfileInfo> mProfileList;
    private onItemClickListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mFullNameTV;
        TextView mUsernameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.userDP);
            mFullNameTV = itemView.findViewById(R.id.fullNameProfile);
            mUsernameTV = itemView.findViewById(R.id.usernameProfile);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }

    public ProfileAdapter(ArrayList<ProfileInfo> profileList) {
        mProfileList = profileList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card,parent,false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        ProfileInfo currentItem = this.mProfileList.get(position);
        Picasso.get().load(currentItem.getUserDP()).into(holder.mImageView);
        holder.mFullNameTV.setText(currentItem.getFullName());
        holder.mUsernameTV.setText(currentItem.getUsername());
    }

    @Override
    public int getItemCount() {
        return mProfileList.size();
    }

    //Ptaa nii kyu kia h...bss kia h!
    public class ImgLoader extends AsyncTask<String, Void, Bitmap> {

        ImageView iv;
        ProgressBar pb;
        Bitmap bmp;

        public ImgLoader(ImageView iv) {
            this.iv = iv;
        }
        public ImgLoader(ImageView iv, ProgressBar pb) {
            this.iv = iv;
            this.pb = pb;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            if(pb!=null)
                pb.setVisibility(View.VISIBLE);
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground(String... ImgUrl) {
            try {
                URL url = new URL(ImgUrl[0]);
                InputStream is = url.openStream();
                bmp = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iv.setImageBitmap(bitmap);
            if(pb !=null)
                pb.setVisibility(View.GONE);
            super.onPostExecute(bitmap);
        }
    }
}
