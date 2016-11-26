package np.edu.ku.kurc.posts.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.models.ModelFactory;
import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.models.collection.PostCollection;

public class PostsLocalDataSource implements PostsDataSourceContract {

    private Context context;

    public PostsLocalDataSource(Context context) {
        this.context = context;
    }

    @Override
    public void refreshPosts() {
        // Not needed because refreshing is done by remote.
    }

    @Override
    public void getPosts(int perPage, String category, String postsAfter, String postsBefore, LoadPostsCallback callback) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getReadableDatabase();

        String sql = getPostsQuery(perPage, category, postsAfter, postsBefore);

        Cursor cursor = db.rawQuery(sql, null);

        PostCollection posts = (PostCollection) ModelFactory.getInstance(Post.class).getCollection(cursor);

        // Loads Metadata for posts. Metadata include author, featured media etc.
        posts.loadMetadata(context);

        callback.onPostsLoaded(posts);
    }

    @Override
    public void refreshPost() {
        // No implementation needed.
    }

    @Override
    public void getPost(int id, LoadPostCallback callback) {
        Post post = ModelFactory.getInstance(Post.class).findById(context,id);

        if(post != null) {
            callback.onPostLoaded(post);
        } else {
            callback.onPostLoadError();
        }
    }

    @Override
    public void getStickyPost(LoadPostCallback callback) {
        Post post = ModelFactory.getInstance(Post.class).getLatestPinned(context);

        if(post != null) {
            callback.onPostLoaded(post);
        } else {
            callback.onPostLoadError();
        }
    }

    /**
     * Returns posts query for given arguments.
     *
     * @param perPage   Posts per page.
     * @param category  Posts in this category.
     * @param postsAfter     Posts after this date.
     * @param postsBefore    Posts before this date.
     * @return          Returns query string.
     */
    @NonNull
    private String getPostsQuery(int perPage, String category, String postsAfter, String postsBefore) {
        String sql = "SELECT posts.* " +
                "FROM category_post " +
                "JOIN categories ON categories._id = category_post.category_id " +
                "JOIN posts ON posts._id = category_post.post_id ";

        if(category != null) {
            sql += "WHERE categories.slug = '" + category + "' ";
        }

        if(postsAfter != null) {
            sql += ((category != null)?"AND":"WHERE") + " created_at > '" + postsAfter + "' ";
        }

        if(postsBefore != null) {
            sql += ((category != null || postsAfter != null)?"AND":"WHERE") + " created_at < '" + postsBefore + "' ";
        }

        sql += "ORDER BY created_at DESC ";

        sql += "LIMIT " + Integer.toString(perPage);

        return sql;
    }
}
