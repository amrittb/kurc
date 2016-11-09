package np.edu.ku.kurc.models.collection;

import android.content.Context;

import np.edu.ku.kurc.common.AsyncCallback;

public interface CollectionContract<T> {

    /**
     * Saves all item of collection.
     *
     * @param context   Application Context.
     * @param callback  Async Callback for save callback.
     */
    void saveAll(Context context, AsyncCallback<T,Void,Void> callback);

    /**
     * Finds a model for given id.
     *
     * @param id    Id to find.
     * @return      Model instance.
     */
    T findById(int id);
}
