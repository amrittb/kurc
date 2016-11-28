package np.edu.ku.kurc.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import np.edu.ku.kurc.posts.PostActivity;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.FeaturedMedia;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.Metrics;

public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_FOOTER = 1;

    private Context context;

    private List<Post> list;

    private View.OnClickListener reloadOlderPostsClickListener;

    private FooterViewHolder footerViewHolder;

    private CropCircleTransformation cropCircleTransformation;

    public PostsAdapter(Context context, List<Post> list, View.OnClickListener reloadOlderPostsClickListener) {
        this.context = context;
        this.list = list;
        this.reloadOlderPostsClickListener = reloadOlderPostsClickListener;

        cropCircleTransformation = new CropCircleTransformation();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Creating footer view at the very beginning to show footer instantly when needed.
        createFooterViewHolder(parent);

        if(viewType == VIEW_TYPE_ITEM) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list, parent, false);
            return new PostViewHolder(itemView);
        } else if(viewType == VIEW_TYPE_FOOTER) {
            return createFooterViewHolder(parent);
        }

        return null;
    }

    /**
     * Creates a footer view holder instance.
     *
     * @param parent    Parent view.
     * @return          Footer View Holder instance.
     */
    private FooterViewHolder createFooterViewHolder(ViewGroup parent) {
        if(footerViewHolder == null) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_list_footer, parent, false);

            footerViewHolder = new FooterViewHolder(itemView);
        }

        return footerViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        return (list.get(position) == null) ? VIEW_TYPE_FOOTER : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof PostViewHolder) {
            PostViewHolder viewHolder = (PostViewHolder) holder;

            Post post = list.get(position);

            viewHolder.postTitle.setText(post.title);
            viewHolder.postDate.setText(post.getDateString(context));
            viewHolder.postAuthor.setText(post.getAuthor().name);

            loadAuthorAvatar(viewHolder,post);
            loadFeaturedImage(viewHolder,post);
        } else if(holder instanceof FooterViewHolder) {
            FooterViewHolder viewHolder = (FooterViewHolder) holder;

            viewHolder.setLoadingIndicator(true);
            viewHolder.setLoadingError(false);
        }
    }

    /**
     * Loads Author Avatar.
     *
     * @param holder    PostViewHolder instance.
     * @param post      Post for which author avatar is to be loaded.
     */
    private void loadAuthorAvatar(PostViewHolder holder, Post post) {
        Picasso.with(context).cancelRequest(holder.authorAvatar);

        holder.authorAvatar.setImageBitmap(null);

        Picasso.with(context)
                .load(post.getAuthor().avatar)
                .resize(holder.avatarSize,holder.avatarSize)
                .centerCrop()
                .transform(cropCircleTransformation)
                .into(holder.authorAvatar);
    }

    /**
     * Loads Featured image if it exists.
     *
     * @param holder ListView holder instance.
     * @param post Post for which the featured media is to be loaded.
     */
    private void loadFeaturedImage(PostViewHolder holder, Post post) {
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

    /**
     * Returns post at position.
     *
     * @param pos   Post at position.
     * @return      Post reference.
     */
    public Post getItem(int pos) {
        return list.get(pos);
    }

    /**
     * Replace posts with given posts.
     *
     * @param posts     Posts to be replaced.
     */
    public void replacePosts(List<Post> posts) {
        list.clear();

        list.addAll(posts);

        notifyDataSetChanged();
    }

    /**
     * Adds Older Posts to list.
     *
     * @param posts     Older posts to be added.
     */
    public void addOlderPosts(List<Post> posts) {
        removeFooter();

        list.addAll(posts);

        notifyDataSetChanged();
    }

    /**
     * Adds Newer Posts to list.
     *
     * @param posts     Newer Posts to be added.
     */
    public void addNewerPosts(List<Post> posts) {
        posts.addAll(list);

        list.clear();

        list.addAll(posts);

        notifyDataSetChanged();
    }

    /**
     * Sets Older Posts Loading Indicator.
     *
     * @param active    Check if the loading indicator is active.
     */
    public void setOlderPostsLoadingIndicator(boolean active) {
        if(footerViewHolder != null) {
            footerViewHolder.setLoadingError(false);
            footerViewHolder.setLoadingIndicator(active);
        }

        if(active) {
            addFooter();
        }
    }

    /**
     * Shows Older posts loading error.
     */
    public void showOlderPostsLoadError() {
        if(footerViewHolder != null) {
            footerViewHolder.setLoadingIndicator(false);
            footerViewHolder.setLoadingError(true);
        }
    }

    /**
     * Shows Older posts not found.
     */
    public void showOlderPostsNotFound() {
        removeFooter();
    }

    /**
     * Adds a footer to recycler view.
     */
    private void addFooter() {
        if(list.get(list.size() - 1) != null) {
            list.add(null);
            notifyItemInserted(list.size() - 1);
        }
    }

    /**
     * Removes Footer.
     */
    private void removeFooter() {
        if(list.get(list.size() - 1) == null) {
            list.remove(list.size() - 1);
            notifyItemRemoved(list.size());
        }
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        public ImageView authorAvatar;
        public ImageView featureImage;
        public TextView postTitle, postDate, postAuthor;
        public ImageButton openInBrowserBtn;

        public int featuredImageWidth;
        public int featuredImageHeight;
        public int avatarSize;

        public PostViewHolder(View itemView) {
            super(itemView);

            authorAvatar = (ImageView) itemView.findViewById(R.id.post_author_avatar);
            featureImage = (ImageView) itemView.findViewById(R.id.post_featured_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            postDate = (TextView) itemView.findViewById(R.id.post_date);
            postAuthor = (TextView) itemView.findViewById(R.id.post_author);

            openInBrowserBtn = (ImageButton) itemView.findViewById(R.id.post_open_in_browser_btn);

            avatarSize = (int) Metrics.dipToPixels(context,context.getResources().getDimension(R.dimen.avatar_size));

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Intent i = new Intent(v.getContext(),PostActivity.class);
                    i.putExtra(Const.KEY_POST,(new Gson()).toJson(list.get(pos)));

                    v.getContext().startActivity(i);
                }
            });

            openInBrowserBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    list.get(pos).openInBrowser(context);
                }
            });
        }
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        private View container;
        private View progressBar;
        private View refreshImage;

        public FooterViewHolder(View itemView) {
            super(itemView);

            container = itemView;
            progressBar = itemView.findViewById(R.id.older_posts_progress_bar);
            refreshImage = itemView.findViewById(R.id.older_posts_refresh_img);

            itemView.setOnClickListener(reloadOlderPostsClickListener);
        }

        /**
         * Sets Loading Indicator.
         *
         * @param active    Flag to determine if there to show loading indicator.
         */
        private void setLoadingIndicator(boolean active) {
            container.setEnabled(false);

            if(active) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
        }

        /**
         * Sets Loading Error.
         *
         * @param active    Flag to determine if there is loading error.
         */
        private void setLoadingError(boolean active) {
            container.setEnabled(false);

            if(active) {
                container.setEnabled(true);
                refreshImage.setVisibility(View.VISIBLE);
            } else {
                refreshImage.setVisibility(View.GONE);
            }
        }
    }
}
