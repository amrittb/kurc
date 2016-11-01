package np.edu.ku.kurc.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import np.edu.ku.kurc.PostActivity;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.FeaturedMedia;
import np.edu.ku.kurc.models.Post;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private List<Post> list;

    private Context context;

    public PostsAdapter(Context context, List<Post> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = list.get(position);

        holder.postTitle.setText(post.title);
        holder.postDate.setText(post.getDateString(context));

        loadFeaturedImage(holder,post);
    }


    /**
     * Loads Featured image if it exists.
     *
     * @param holder View holder instance.
     * @param post Post for which the featured media is to be loaded.
     */
    private void loadFeaturedImage(PostsAdapter.ViewHolder holder, Post post) {
        Picasso.with(context).cancelRequest(holder.featureImage);
        holder.featureImage.setImageBitmap(null);
        holder.featureImage.setVisibility(View.VISIBLE);

        if ( ! post.hasFeaturedMedia()) {
            holder.featureImage.setVisibility(View.GONE);
        } else {
            int width = holder.featuredImageWidth;
            int height = holder.featuredImageHeight;

            if(width <= 0 || height <= 0) {
                width = context.getResources().getDisplayMetrics().widthPixels;
                height = (int) ((9.0/16.0) * width);
            }

            FeaturedMedia media = post.getFeaturedMedia();
            String url = media.getOptimalSize(width, height).sourceUrl;
            Picasso.with(context)
                    .load(url)
                    .fit()
                    .centerCrop()
                    .tag(context)
                    .into(holder.featureImage);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView featureImage;
        public TextView postTitle, postDate, postAuthor;
        public int featuredImageWidth;
        public int featuredImageHeight;

        public ViewHolder(View itemView) {
            super(itemView);

            featureImage = (ImageView) itemView.findViewById(R.id.post_featured_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);

            resizeImageView(featureImage);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent i = new Intent(v.getContext(),PostActivity.class);
                    i.putExtra(Const.KEY_POST,new Gson().toJson(list.get(pos)));

                    v.getContext().startActivity(i);
                }
            });
        }

        private void resizeImageView(final ImageView featureImage) {
            featureImage.post(new Runnable() {

                @Override
                public void run() {
                    featuredImageWidth = featureImage.getWidth();

                    featuredImageHeight = (int) ((9.0f / 16.0f) * featuredImageWidth);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) featureImage.getLayoutParams();
                    params.width = featuredImageWidth;
                    params.height = featuredImageHeight;
                    featureImage.setLayoutParams(params);
                }
            });
        }
    }
}
