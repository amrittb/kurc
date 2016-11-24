package np.edu.ku.kurc.posts.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.services.PostSyncService;

public class PostsRemoteDataSource implements PostsRemoteDataSourceContract {

    private Context context;

    private BroadcastReceiver postsSyncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(Const.SERVICE_KEY_RESULT_CODE,Const.SERVICE_RESULT_FAILURE);
            String action = intent.getAction();

            if(resultCode != Const.SERVICE_RESULT_OK) {
                handlePostsLoadError(action);
            } else {
                handlePostsLoaded(action);
            }
        }
    };

    private IntentFilter postsFilter = new IntentFilter(PostSyncService.ACTION_POSTS_SYNC);
    private IntentFilter postsAfterFilter = new IntentFilter(PostSyncService.ACTION_POSTS_AFTER_SYNC);
    private IntentFilter postsBeforeFilter = new IntentFilter(PostSyncService.ACTION_POSTS_BEFORE_SYNC);

    private HashMap<String,List<LoadPostsFromRemoteCallback>> callbacks = new HashMap<>();

    public PostsRemoteDataSource(Context context) {
        this.context = context;
    }

    /**
     * Registers Broadcast receivers.
     */
    public void registerReceivers() {
        this.context.registerReceiver(postsSyncBroadcastReceiver, postsFilter);
        this.context.registerReceiver(postsSyncBroadcastReceiver, postsAfterFilter);
        this.context.registerReceiver(postsSyncBroadcastReceiver, postsBeforeFilter);
    }

    /**
     * Unregisters Broadcast receivers.
     */
    public void unregisterReceivers() {
        this.context.unregisterReceiver(postsSyncBroadcastReceiver);
    }

    @Override
    public void getPosts(int perPage, String category, String postsAfter, String postsBefore, LoadPostsFromRemoteCallback callback) {
        if(postsAfter != null && postsBefore == null) {
            // Only postsAfter is not null
            if (!PostSyncService.isSyncingPostsAfter()) {
                PostSyncService.startPostsAfterSync(context, perPage, postsAfter);
                registerCallback(PostSyncService.ACTION_POSTS_AFTER_SYNC, callback);
            }
        } else if(postsAfter == null && postsBefore != null) {
            // Only postsBefore is not null
            if(! PostSyncService.isSyncingPostsBefore()) {
                PostSyncService.startPostsBeforeSync(context, perPage, postsBefore);
                registerCallback(PostSyncService.ACTION_POSTS_BEFORE_SYNC, callback);
            }
        } else if(postsAfter == null && postsBefore == null) {
            // When both are null.
            if(! PostSyncService.isSyncingPosts()) {
                PostSyncService.startPostsSync(context, perPage);
                registerCallback(PostSyncService.ACTION_POSTS_SYNC, callback);
            }
        }
    }

    /**
     * Registers callback for remote posts load.
     *
     * @param action        Action Type of load.
     * @param callback      Callback instance.
     */
    private void registerCallback(String action, LoadPostsFromRemoteCallback callback) {
        List<LoadPostsFromRemoteCallback> callbackList = callbacks.get(action);

        if(callbackList == null) {
            callbackList = new ArrayList<>();
            callbacks.put(action, callbackList);
        }

        callbackList.add(callback);
    }

    /**
     * Handles Post loaded broadcast.
     *
     * @param action    Action of load type.
     */
    private void handlePostsLoaded(String action) {
        List<LoadPostsFromRemoteCallback> callbackList = callbacks.get(action);

        if(callbackList != null) {
            for(LoadPostsFromRemoteCallback callback : callbackList) {
                callback.onPostsLoaded(action);
                callbackList.remove(callback);
            }
        }
    }

    /**
     * Handles Post load error broadcast.
     *
     * @param action    Action of load type.
     */
    private void handlePostsLoadError(String action) {
        List<LoadPostsFromRemoteCallback> callbackList = callbacks.get(action);

        if(callbackList != null) {
            for(LoadPostsFromRemoteCallback callback : callbackList) {
                callback.onPostsLoadError(action);
                callbackList.remove(callback);
            }
        }
    }
}
