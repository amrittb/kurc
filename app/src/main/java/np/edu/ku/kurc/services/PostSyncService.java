package np.edu.ku.kurc.services;

import android.content.Intent;
import android.content.Context;

import np.edu.ku.kurc.models.Post;
import np.edu.ku.kurc.models.collection.PostCollection;
import np.edu.ku.kurc.network.api.ApiConstants;
import np.edu.ku.kurc.network.api.ServiceFactory;
import np.edu.ku.kurc.network.api.services.PostService;
import retrofit2.Call;

public class PostSyncService extends SyncService<Post, PostCollection> {

    public static final String ACTION_POST_SYNC = ACTION_PREFIX + "POST_SYNC";
    public static final String ACTION_PAGE_SYNC = ACTION_PREFIX + "PAGE_SYNC";
    public static final String ACTION_POSTS_SYNC = ACTION_PREFIX + "POSTS_SYNC";
    public static final String ACTION_STICKY_POST_SYNC = ACTION_PREFIX + "STICKY_POST_SYNC";
    public static final String ACTION_POSTS_AFTER_SYNC = ACTION_PREFIX + "POSTS_AFTER_SYNC";
    public static final String ACTION_POSTS_BEFORE_SYNC = ACTION_PREFIX + "POSTS_BEFORE_SYNC";

    private static final String EXTRA_PAGE_ID = EXTRA_PREFIX + "PAGE_ID";
    private static final String EXTRA_POST_ID = EXTRA_PREFIX + "POST_ID";
    private static final String EXTRA_POSTS_AFTER = EXTRA_PREFIX + "POSTS_AFTER";
    private static final String EXTRA_POSTS_BEFORE = EXTRA_PREFIX + "POSTS_BEFORE";
    private static final String EXTRA_POSTS_PER_PAGE = EXTRA_PREFIX + "POSTS_PER_PAGE";

    private static boolean isSyncingPost = false;
    private static boolean isSyncingPosts = false;
    private static boolean isSyncingPostsAfter = false;
    private static boolean isSyncingPostsBefore = false;


    public PostSyncService() {
        super("PostSyncService");
    }

    /**
     * Starts Post Sync Service.
     *
     * @param context   Service Context.
     * @param postId    Id of post which is to be synced.
     */
    public static void startPostSync(Context context, int postId) {
        Intent intent = new Intent(context, PostSyncService.class);
        intent.setAction(ACTION_POST_SYNC);
        intent.putExtra(EXTRA_POST_ID, postId);
        context.startService(intent);
    }

    /**
     * Starts Page Sync Service.
     *
     * @param context   Service Context.
     * @param id    Id of page which is to be synced.
     */
    public static void startPageSync(Context context, int id) {
        Intent intent = new Intent(context, PostSyncService.class);
        intent.setAction(ACTION_PAGE_SYNC);
        intent.putExtra(EXTRA_PAGE_ID, id);
        context.startService(intent);
    }

    /**
     * Starts Sticky Post Sync.
     *
     * @param context   Service context.
     */
    public static void startStickyPostSync(Context context) {
        Intent intent = new Intent(context, PostSyncService.class);
        intent.setAction(ACTION_STICKY_POST_SYNC);
        context.startService(intent);
    }

    /**
     * Starts post sync.
     *
     * @param context   Service Context.
     * @param perPage   Posts per page.
     */
    public static void startPostsSync(Context context, int perPage) {
        Intent intent = new Intent(context, PostSyncService.class);
        intent.setAction(ACTION_POSTS_SYNC);
        intent.putExtra(EXTRA_POSTS_PER_PAGE, perPage);
        context.startService(intent);
    }

    /**
     * Starts Posts before sync Service.
     *
     * @param context   Service Context.
     * @param before     Posts published before this date.
     */
    public static void startPostsBeforeSync(Context context, String before) {
        startPostsBeforeSync(context, ApiConstants.POSTS_PER_PAGE_DEFAULT, before);
    }

    /**
     * Starts Posts before sync Service.
     *
     * @param context   Service Context.
     * @param before     Posts published before this date.
     */
    public static void startPostsBeforeSync(Context context, int perPage, String before) {
        Intent intent = new Intent(context, PostSyncService.class);
        intent.setAction(ACTION_POSTS_BEFORE_SYNC);
        intent.putExtra(EXTRA_POSTS_BEFORE, before);
        intent.putExtra(EXTRA_POSTS_PER_PAGE, perPage);
        context.startService(intent);
    }

    /**
     * Starts Posts after Sync Service.
     * @param context   Service Context.
     * @param after     Posts published after this date.
     */
    public static void startPostsAfterSync(Context context, String after) {
        startPostsAfterSync(context, ApiConstants.POSTS_PER_PAGE_DEFAULT, after);
    }

    /**
     * Starts Posts after Sync Service.
     *
     * @param context   Service Context.
     * @param after     Posts published after this date.
     */
    public static void startPostsAfterSync(Context context, int perPage, String after) {
        Intent intent = new Intent(context, PostSyncService.class);
        intent.setAction(ACTION_POSTS_AFTER_SYNC);
        intent.putExtra(EXTRA_POSTS_AFTER, after);
        intent.putExtra(EXTRA_POSTS_PER_PAGE, perPage);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_POST_SYNC.equals(action)) {
                final int postId = intent.getIntExtra(EXTRA_POST_ID, 0);
                handlePostSync(postId);
            } else if(ACTION_PAGE_SYNC.equals(action)) {
                final int pageId = intent.getIntExtra(EXTRA_PAGE_ID,0);
                handlePageSync(pageId);
            } else if(ACTION_STICKY_POST_SYNC.equals(action)) {
                handleStickyPostSync();
            } else if(ACTION_POSTS_SYNC.equals(action)) {
                final int perPage = intent.getIntExtra(EXTRA_POSTS_PER_PAGE,ApiConstants.POSTS_PER_PAGE_DEFAULT);
                handlePostsSync(perPage);
            } else if(ACTION_POSTS_BEFORE_SYNC.equals(action)) {
                final String before = intent.getStringExtra(EXTRA_POSTS_BEFORE);
                final int perPage = intent.getIntExtra(EXTRA_POSTS_PER_PAGE,ApiConstants.POSTS_PER_PAGE_DEFAULT);
                handlePostsBeforeSync(before, perPage);
            } else if(ACTION_POSTS_AFTER_SYNC.equals(action)) {
                final String after = intent.getStringExtra(EXTRA_POSTS_AFTER);
                final int perPage = intent.getIntExtra(EXTRA_POSTS_PER_PAGE,ApiConstants.POSTS_PER_PAGE_DEFAULT);
                handlePostsAfterSync(after, perPage);
            }
        }
    }

    /**
     * Handles Post Sync Service.
     *
     * @param postId    Post Id of post to be synced.
     */
    private void handlePostSync(int postId) {
        isSyncingPost = true;
        Call<Post> call = ServiceFactory.makeService(PostService.class).getPost(postId);
        handleModelSync(call, ACTION_POST_SYNC);
    }

    /**
     * Handles Page Syncing.
     *
     * @param pageId    Syncs Page for given page id.
     */
    private void handlePageSync(int pageId) {
        Call<Post> pageCall = ServiceFactory.makeService(PostService.class).getPage(pageId);
        handleModelSync(pageCall, ACTION_PAGE_SYNC);
    }

    /**
     * Handles Sticky Post Sync.
     */
    private void handleStickyPostSync() {
        Call<PostCollection> call = ServiceFactory.makeService(PostService.class).getStickyPost();
        handleCollectionSync(call, ACTION_STICKY_POST_SYNC);
    }

    /**
     * Handles Posts Sync Service.
     *
     * @param perPage   Posts per page.
     */
    private void handlePostsSync(int perPage) {
        isSyncingPosts = true;
        Call<PostCollection> postsCall = ServiceFactory.makeService(PostService.class).getPosts(perPage);
        handleCollectionSync(postsCall,ACTION_POSTS_SYNC);
    }

    /**
     * Handles Posts before sync service.
     *
     * @param before    Posts before this time to be synced.
     * @param perPage   Posts per page.
     */
    private void handlePostsBeforeSync(String before, int perPage) {
        isSyncingPostsBefore = true;
        Call<PostCollection> call = ServiceFactory.makeService(PostService.class).getPostsBefore(before, perPage);
        handleCollectionSync(call,ACTION_POSTS_BEFORE_SYNC);
    }

    /**
     * Handles Posts after sync service.
     *
     * @param after    Posts after this time to be synced.
     * @param perPage   Posts per page.
     */
    private void handlePostsAfterSync(String after, int perPage) {
        isSyncingPostsAfter = true;
        Call<PostCollection> call = ServiceFactory.makeService(PostService.class).getPostsAfter(after, perPage);
        handleCollectionSync(call,ACTION_POSTS_AFTER_SYNC);
    }

    /**
     * Determines if service is syncing post.
     *
     * @return  isSyncingPosts flag.
     */
    public static boolean isSyncingPost() {
        return isSyncingPost;
    }

    /**
     * Determines if service is syncing posts.
     *
     * @return  isSyncingPosts flag.
     */
    public static boolean isSyncingPosts() {
        return isSyncingPosts;
    }

    /**
     * Determines if service is syncing posts after.
     *
     * @return  isSyncingPosts flag.
     */
    public static boolean isSyncingPostsAfter() {
        return isSyncingPostsAfter;
    }

    /**
     * Determines if service is syncing posts before.
     *
     * @return  isSyncingPosts flag.
     */
    public static boolean isSyncingPostsBefore() {
        return isSyncingPostsBefore;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        isSyncingPost = false;
        isSyncingPosts = false;
        isSyncingPostsAfter = false;
        isSyncingPostsBefore = false;
    }

    @Override
    protected void onBroadcast(String action, int resultCode, String message) {
        if(ACTION_POST_SYNC.equals(action)) {
            isSyncingPost = false;
        } else if(ACTION_POSTS_SYNC.equals(action)) {
            isSyncingPosts = false;
        } else if(ACTION_POSTS_BEFORE_SYNC.equals(action)) {
            isSyncingPostsBefore = false;
        } else if(ACTION_POSTS_AFTER_SYNC.equals(action)) {
            isSyncingPostsAfter = false;
        }
    }
}
