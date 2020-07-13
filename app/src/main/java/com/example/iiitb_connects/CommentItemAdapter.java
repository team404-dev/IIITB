package com.example.iiitb_connects;

import android.content.Context;
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

import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class CommentItemAdapter
        extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userDP;
        private TextView username;
        private TextView comment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.userDP = itemView.findViewById(R.id.userDP);
            this.username = itemView.findViewById(R.id.username);
            this.comment = itemView.findViewById(R.id.comment);
        }
    }

    private List<CommentItems> commentItems;
    private Context context;

    public CommentItemAdapter(List<CommentItems> commentItems, Context context) {
        this.commentItems = commentItems;
        this.context = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public CommentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_comment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentItemAdapter.ViewHolder holder, int position) {
        CommentItems commentItems = this.commentItems.get(position);
        Picasso.with(context).load(commentItems.getUserDp()).into(holder.userDP);
        //new ImgLoader(holder.userDP).execute(commentItems.getUserDp());
        holder.username.setText(commentItems.getUsername());
        holder.comment.setText(commentItems.getComments());
    }

    @Override
    public int getItemCount() {
        return commentItems.size();
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
