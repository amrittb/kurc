package np.edu.ku.kurc.views.viewmodels;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.views.viewmodels.contracts.ViewModel;

public class PostViewModel extends ViewModel<Post> {

    private View postContainer;

    private TextView postTitle;
    private TextView postDate;
    private TextView postAuthor;
    private TextView postContent;
    private ImageView postFeaturedImage;

    public PostViewModel(View root) {
        super(root);
    }

    public PostViewModel(View root, Post model) {
        super(root, model);
    }

    @Override
    public void onBindView(View root) {
        postContainer = root.findViewById(R.id.post_container);

        postTitle = (TextView) postContainer.findViewById(R.id.post_title);
        postDate = (TextView) postContainer.findViewById(R.id.post_date);
        postAuthor = (TextView) postContainer.findViewById(R.id.post_author);
        postContent = (TextView) postContainer.findViewById(R.id.post_content);
        postFeaturedImage = (ImageView) postContainer.findViewById(R.id.post_featured_image);
    }

    @Override
    public void onBindModel(Post model) {
        postContainer.setVisibility(View.VISIBLE);

        postTitle.setText(model.title);
        postDate.setText(model.getDateString());
        postContent.setText(model.content);
    }
}
