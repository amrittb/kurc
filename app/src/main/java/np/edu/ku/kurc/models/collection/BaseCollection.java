package np.edu.ku.kurc.models.collection;

import java.util.ArrayList;

import android.content.Context;

import np.edu.ku.kurc.common.AsyncCallback;
import np.edu.ku.kurc.database.tasks.BulkSaveTask;
import np.edu.ku.kurc.models.BaseModel;

public abstract class BaseCollection<T extends BaseModel> extends ArrayList<T> implements CollectionContract<T> {

    /**
     * Saves all items of collection.
     *
     * @param context   Application Context.
     * @param callback  Async Callback for save callback.
     */
    @Override
    public void saveAll(Context context, AsyncCallback<T,Void,Void> callback) {
        BulkSaveTask<T> task = new BulkSaveTask<>(context, this, callback);

        task.execute();
    }
}
