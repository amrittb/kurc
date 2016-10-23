package np.edu.ku.kurc.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.models.Post;

public class TopStoriesAdapter extends RecyclerView.Adapter<TopStoriesAdapter.ViewHolder> {

    private List<Post> stories;

    public TopStoriesAdapter(List<Post> stories) {
        this.stories = stories;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_story,parent,false);

        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Post post = stories.get(position);
        holder.postTitle.setText(post.title);
        holder.postDate.setText(post.getDateString());
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

            resizeImageView(featureImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),"Clicked on item",Toast.LENGTH_LONG).show();
                }
            });
        }

        private void resizeImageView(final ImageView featureImage) {
            featureImage.post(new Runnable() {

                @Override
                public void run() {
                    int width = featureImage.getWidth();

                    int newHeight = (int) ((9.0f / 16.0f) * width);

                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) featureImage.getLayoutParams();
                    params.width = width;
                    params.height = newHeight;
                    featureImage.setLayoutParams(params);
                }
            });
        }
    }
}
