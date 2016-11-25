package np.edu.ku.kurc.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import np.edu.ku.kurc.posts.PostActivity;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.FeaturedMedia;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.Metrics;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.ViewHolder> {

    private Context context;

    private List<Post> stories;

    public int featuredImageWidth;
    public int featuredImageHeight;

    public TopStoriesAdapter(Context context,List<Post> stories) {
        this.context = context;
        this.stories = stories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_story,parent,false);

        ViewHolder viewHolder = new ViewHolder(item);

        if(featuredImageWidth <= 0 || featuredImageHeight <= 0) {
            populateDimensions(viewHolder);
        }

        return viewHolder;
    }

    /**
     * Populates Image Width and Image height dimensions.
     *
     * @param viewHolder    View Holder to populate dimension from.
     */
    private void populateDimensions(final ViewHolder viewHolder) {
        viewHolder.featureImage.post(new Runnable() {

            @Override
            public void run() {
                featuredImageWidth = viewHolder.featureImage.getWidth();
                featuredImageHeight = (int) ((9.0f / 16.0f) * featuredImageWidth);
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = stories.get(position);
        holder.postTitle.setText(post.title);
        holder.postDate.setText(post.getDateString(context));
        holder.postAuthor.setText((post.getAuthor() == null)?"":post.getAuthor().name);

        loadFeaturedImage(holder, post);
    }

    /**
     * Loads Featured image if it exists.
     *
     * @param holder ListView holder instance.
     * @param post Post for which the featured media is to be loaded.
     */
    private void loadFeaturedImage(ViewHolder holder, Post post) {
        Picasso.with(context).cancelRequest(holder.featureImage);

        String url = null;

        if(post.hasFeaturedMedia()) {
            FeaturedMedia media = post.getFeaturedMedia();
            if(featuredImageWidth <= 0 || featuredImageHeight <= 0) {
                featuredImageWidth = (int) Metrics.dipToPixels(context, context.getResources().getDimension(R.dimen.card_width_default));
                featuredImageHeight = (int) ((9.0/16.0) * featuredImageWidth);
            }
            url = media.getOptimalSourceUrl(featuredImageWidth,featuredImageHeight);
        }

        Picasso.with(context)
                .load(url)
                .fit()
                .centerCrop()
                .placeholder(R.drawable.ic_image_white_24dp)
                .error(R.drawable.ic_image_white_24dp)
                .into(holder.featureImage);
    }

    /**
     * Replaces Stories.
     *
     * @param posts     Stories to be updated.
     */
    public void replaceStories(List<Post> posts) {
        stories.clear();

        notifyDataSetChanged();

        stories.addAll(posts);

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView featureImage;
        public TextView postTitle, postDate, postAuthor;

        public ViewHolder(View itemView) {
            super(itemView);

            featureImage = (ImageView) itemView.findViewById(R.id.post_featured_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent i = new Intent(v.getContext(),PostActivity.class);
                    i.putExtra(Const.KEY_POST,new Gson().toJson(stories.get(pos)));

                    v.getContext().startActivity(i);
                }
            });
        }
    }
}
