package np.edu.ku.kurc.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import np.edu.ku.kurc.PostActivity;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.FeaturedMedia;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.Metrics;

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

        holder.postAuthor.setText(post.getAuthor().name);

        loadAuthorAvatar(holder,post);
        loadFeaturedImage(holder,post);
    }

    /**
     * Loads Author Avatar.
     *
     * @param holder    ViewHolder instance.
     * @param post      Post for which author avatar is to be loaded.
     */
    private void loadAuthorAvatar(ViewHolder holder, Post post) {
        Picasso.with(context).cancelRequest(holder.authorAvatar);

        holder.authorAvatar.setImageBitmap(null);

        Picasso.with(context)
                .load(post.getAuthor().avatar)
                .resize(holder.avatarSize,holder.avatarSize)
                .centerCrop()
                .into(holder.authorAvatar);
    }


    /**
     * Loads Featured image if it exists.
     *
     * @param holder View holder instance.
     * @param post Post for which the featured media is to be loaded.
     */
    private void loadFeaturedImage(ViewHolder holder, Post post) {
        String url = null;

        Picasso.with(context).cancelRequest(holder.featureImage);
        holder.featureImage.setImageResource(android.R.color.transparent);
        holder.featureImage.setVisibility(View.VISIBLE);

        if (! post.hasFeaturedMedia()) {
            holder.featureImage.setVisibility(View.GONE);
        } else {
            if(holder.featuredImageWidth <= 0 || holder.featuredImageHeight <= 0) {
                holder.featuredImageWidth = context.getResources().getDisplayMetrics().widthPixels;
                holder.featuredImageHeight = (int) ((9.0/16.0) * holder.featuredImageWidth);
            }

            FeaturedMedia media = post.getFeaturedMedia();
            url = media.getOptimalSourceUrl(holder.featuredImageWidth, holder.featuredImageHeight);
        }

        Picasso.with(context)
                .load(url)
                .fit()
                .centerCrop()
                .into(holder.featureImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView authorAvatar;
        public ImageView featureImage;
        public TextView postTitle, postDate, postAuthor;
        public int featuredImageWidth;
        public int featuredImageHeight;
        public int avatarSize;

        public ViewHolder(View itemView) {
            super(itemView);

            authorAvatar = (ImageView) itemView.findViewById(R.id.post_author_avatar);
            featureImage = (ImageView) itemView.findViewById(R.id.post_featured_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);

            avatarSize = (int) Metrics.dipToPixels(context,context.getResources().getDimension(R.dimen.avatar_size));

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
    }
}
