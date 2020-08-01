package com.example.iiitb_connects;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private ArrayList<ProfileInfo> mProfileList;
    private onItemClickListener listener;
    Activity context;

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView mImageView;
        TextView mFullNameTV;
        TextView mUsernameTV;
        RelativeLayout cardRelativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.userDP);
            mFullNameTV = itemView.findViewById(R.id.fullNameProfile);
            mUsernameTV = itemView.findViewById(R.id.usernameProfile);
            cardRelativeLayout = itemView.findViewById(R.id.cardRelativeLayout);

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

    public ProfileAdapter(ArrayList<ProfileInfo> profileList , Activity context) {
        mProfileList = profileList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_card,parent,false);
        ViewHolder pvh = new ViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfileAdapter.ViewHolder holder, final int position) {
        final ProfileInfo currentItem = this.mProfileList.get(position);
        if (currentItem.getUserDP() == null || currentItem.getUserDP().isEmpty() || currentItem.getUserDP().equals("")){
            holder.mImageView.setImageResource(R.drawable.person);
        } else {
            Glide.with(context).load(Uri.parse(currentItem.getUserDP())).placeholder(R.drawable.person).into(holder.mImageView);
            /*Picasso.get().load(currentItem.getUserDP()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.mImageView, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(currentItem.getUserDP()).into(holder.mImageView);
                }
            });*/
        }
        holder.mFullNameTV.setText(currentItem.getFullName());
        holder.mUsernameTV.setText(currentItem.getUsername());

        holder.cardRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uid = currentItem.getUserUid();
                Intent intent = new Intent(context,StalkingActivity.class);
                intent.putExtra("User Uid",Uid);

                Pair[] pairs = new Pair[3];
                pairs[0] = new Pair<View, String>(holder.mImageView, "DPTransition");
                pairs[1] = new Pair<View, String>(holder.mFullNameTV, "nameTransition1");
                pairs[2] = new Pair<View, String>(holder.mUsernameTV, "nameTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(context, pairs);
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProfileList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
