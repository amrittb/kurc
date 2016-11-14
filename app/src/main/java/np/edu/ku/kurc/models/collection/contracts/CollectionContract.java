package np.edu.ku.kurc.models.collection.contracts;

import android.content.Context;

import np.edu.ku.kurc.common.AsyncCallback;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;

public interface CollectionContract<T> {

    /**
     * Saves all item of collection.
     *
     * @param context   Application Context.
     * @param callback  Async Callback for save callback.
     */
    void saveAll(Context context, AsyncCallback<T,Void,Void> callback);

    /**
     * Saves all collection entries into database synchronously.
     *
     * @param context   Application Context.
     * @throws DatabaseErrorException
     */
    void saveAllSync(Context context) throws DatabaseErrorException;

    /**
     * Finds a model for given id.
     *
     * @param id    Id to find.
     * @return      Model instance.
     */
    T findById(int id);
}
