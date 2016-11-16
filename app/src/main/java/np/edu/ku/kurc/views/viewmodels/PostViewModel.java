package np.edu.ku.kurc.views.viewmodels;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import np.edu.ku.kurc.R;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.utils.Metrics;
import np.edu.ku.kurc.views.viewmodels.contracts.ViewModel;

public class PostViewModel extends ViewModel<Post> {

    private View postContainer;

    private TextView postTitle;
    private TextView postDate;
    private TextView postAuthor;
    private WebView postContent;
    private ImageView postAuthorAvatar;
    private int avatarSize;

    public PostViewModel(View root) {
        super(root);
    }

    public PostViewModel(View root, Post model) {
        super(root, model);
    }

    @Override
    public void onBindView(View root) {
        avatarSize = (int) Metrics.dipToPixels(context,context.getResources().getDimension(R.dimen.avatar_size));

        postContainer = root.findViewById(R.id.post_container);

        postTitle = (TextView) postContainer.findViewById(R.id.post_title);
        postDate = (TextView) postContainer.findViewById(R.id.post_date);
        postAuthor = (TextView) postContainer.findViewById(R.id.post_author);
        postContent = (WebView) postContainer.findViewById(R.id.post_content);
        postAuthorAvatar = (ImageView) postContainer.findViewById(R.id.post_author_avatar);

        postContent.setVisibility(View.GONE);
    }

    @Override
    public void onBindModel(Post model) {
        postContainer.setVisibility(View.VISIBLE);

        postTitle.setText(model.title);
        postDate.setText(model.getDateString(context));
        postAuthor.setText(model.getAuthor().name);
        postContent.loadData(model.content,"text/html",null);

        if(model.content != null && !model.content.contentEquals("")) {
            postContent.setVisibility(View.VISIBLE);
        }

        Picasso.with(context)
                .load(model.getAuthor().avatar)
                .resize(avatarSize,avatarSize)
                .centerCrop()
                .into(postAuthorAvatar);
    }
}
