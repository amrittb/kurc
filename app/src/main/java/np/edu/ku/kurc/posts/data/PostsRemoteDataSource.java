package np.edu.ku.kurc.posts.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import np.edu.ku.kurc.common.Const;
import np.edu.ku.kurc.services.PostSyncService;

public class PostsRemoteDataSource implements PostsRemoteDataSourceContract {

    private Context context;
    private LocalBroadcastManager localBroadcastManager;

    private int receiverReferenceCount = 0;

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

    private IntentFilter pageFilter = new IntentFilter(PostSyncService.ACTION_PAGE_SYNC);
    private IntentFilter postFilter = new IntentFilter(PostSyncService.ACTION_POST_SYNC);
    private IntentFilter postsFilter = new IntentFilter(PostSyncService.ACTION_POSTS_SYNC);
    private IntentFilter stickyPostFilter = new IntentFilter(PostSyncService.ACTION_STICKY_POST_SYNC);
    private IntentFilter postsAfterFilter = new IntentFilter(PostSyncService.ACTION_POSTS_AFTER_SYNC);
    private IntentFilter postsBeforeFilter = new IntentFilter(PostSyncService.ACTION_POSTS_BEFORE_SYNC);

    private HashMap<String,List<LoadFromRemoteCallback>> callbacks = new HashMap<>();

    public PostsRemoteDataSource(Context context) {
        this.context = context;
        localBroadcastManager = LocalBroadcastManager.getInstance(this.context);
    }

    /**
     * Registers Broadcast receivers.
     */
    @Override
    public void registerReceivers() {
        // Only register receivers when there are no references.
        receiverReferenceCount++;

        if(receiverReferenceCount == 1) {
            localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, pageFilter);
            localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, postFilter);
            localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, postsFilter);
            localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, stickyPostFilter);
            localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, postsAfterFilter);
            localBroadcastManager.registerReceiver(postsSyncBroadcastReceiver, postsBeforeFilter);
        }
    }

    /**
     * Unregisters Broadcast receivers.
     */
    @Override
    public void unregisterReceivers() {
        // Only unregister receivers when there are no references.
        receiverReferenceCount--;

        if(receiverReferenceCount < 0) {
            receiverReferenceCount = 0;
        }

        if (receiverReferenceCount == 0) {
            localBroadcastManager.unregisterReceiver(postsSyncBroadcastReceiver);
        }
    }

    @Override
    public void getPosts(int perPage, String category, String postsAfter, String postsBefore, LoadFromRemoteCallback callback) {
        if(postsAfter != null && postsBefore == null) {
            // Only postsAfter is not null
            PostSyncService.startPostsAfterSync(context, perPage, postsAfter);
            registerCallback(PostSyncService.ACTION_POSTS_AFTER_SYNC, callback);
        } else if(postsAfter == null && postsBefore != null) {
            // Only postsBefore is not null
            PostSyncService.startPostsBeforeSync(context, perPage, postsBefore);
            registerCallback(PostSyncService.ACTION_POSTS_BEFORE_SYNC, callback);
        } else if(postsAfter == null && postsBefore == null) {
            // When both are null.
            PostSyncService.startPostsSync(context, perPage);
            registerCallback(PostSyncService.ACTION_POSTS_SYNC, callback);
        }
    }

    @Override
    public void getPost(int id, LoadFromRemoteCallback callback) {
        PostSyncService.startPostSync(context, id);
        registerCallback(PostSyncService.ACTION_POST_SYNC, callback);
    }

    @Override
    public void getPage(int id, LoadFromRemoteCallback callback) {
        PostSyncService.startPageSync(context, id);
        registerCallback(PostSyncService.ACTION_PAGE_SYNC, callback);
    }

    @Override
    public void getStickyPost(LoadFromRemoteCallback callback) {
        PostSyncService.startStickyPostSync(context);
        registerCallback(PostSyncService.ACTION_STICKY_POST_SYNC, callback);
    }

    /**
     * Registers callback for remote posts load.
     *
     * @param action        Action Type of load.
     * @param callback      Callback stickyInstance.
     */
    private void registerCallback(String action, LoadFromRemoteCallback callback) {
        List<LoadFromRemoteCallback> callbackList = callbacks.get(action);

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
        List<LoadFromRemoteCallback> callbackList = callbacks.get(action);

        if(callbackList != null) {
            Iterator<LoadFromRemoteCallback> callbackIterator = callbackList.iterator();

            while(callbackIterator.hasNext()) {
                callbackIterator.next().onLoaded(action);
                callbackIterator.remove();
            }
        }
    }

    /**
     * Handles Post load error broadcast.
     *
     * @param action    Action of load type.
     */
    private void handlePostsLoadError(String action) {
        List<LoadFromRemoteCallback> callbackList = callbacks.get(action);

        if(callbackList != null) {
            Iterator<LoadFromRemoteCallback> callbackIterator = callbackList.iterator();

            while(callbackIterator.hasNext()) {
                callbackIterator.next().onLoadError(action);
                callbackIterator.remove();
            }
        }
    }
}
