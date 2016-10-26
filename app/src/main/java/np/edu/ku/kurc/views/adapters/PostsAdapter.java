package np.edu.ku.kurc.views.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import np.edu.ku.kurc.PostActivity;
import np.edu.ku.kurc.R;
import np.edu.ku.kurc.common.Const;
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
