package com.example.iiitb_connects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class HomeFeedItemAdapter
        extends RecyclerView.Adapter<HomeFeedItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userDP;
        private TextView clubName;
        private ImageView postMedia;
        private TextView description;
        private ImageView likeButton, unlikedButton, commentButton, infoButton;
        private RelativeLayout descriptionLayout;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDP = itemView.findViewById(R.id.userDP);
            clubName = itemView.findViewById(R.id.clubName);
            postMedia = itemView.findViewById(R.id.postMedia);
            description = itemView.findViewById(R.id.description);
            likeButton = itemView.findViewById(R.id.likeButton);
            unlikedButton = itemView.findViewById(R.id.unlikedButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            infoButton = itemView.findViewById(R.id.infoButton);
            descriptionLayout = itemView.findViewById(R.id.descriptionLayout);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private List<HomeFeedItems> homeFeedItems;

    public HomeFeedItemAdapter(List<HomeFeedItems> homeFeedItems) {
        this.homeFeedItems = homeFeedItems;
    }

    @NonNull
    @Override
    public HomeFeedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_home_feed, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeFeedItemAdapter.ViewHolder holder, int position) {
        HomeFeedItems homeFeedItems = this.homeFeedItems.get(position);

        //Setting up item views
        new ImgLoader(holder.userDP).execute(homeFeedItems.getUserDP());
        holder.clubName.setText(homeFeedItems.getClubName());
        new ImgLoader(holder.postMedia, holder.progressBar).execute(homeFeedItems.getPostMedia());
        holder.description.setText(homeFeedItems.getDescription());
        //onClick for item buttons
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.likeButton.setVisibility(View.GONE);
                holder.unlikedButton.setVisibility(View.VISIBLE);
            }
        });
        holder.unlikedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.unlikedButton.setVisibility(View.GONE);
                holder.likeButton.setVisibility(View.VISIBLE);
            }
        });
        holder.infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.descriptionLayout.getVisibility() == View.GONE)
                    holder.descriptionLayout.setVisibility(View.VISIBLE);
                else
                    holder.descriptionLayout.setVisibility(View.GONE);
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takes to comment section
            }
        });
    }

    @Override
    public int getItemCount() {
        return homeFeedItems.size();
    }

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
