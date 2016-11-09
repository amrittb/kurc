package np.edu.ku.kurc.models.collection;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import np.edu.ku.kurc.common.AsyncCallback;
import np.edu.ku.kurc.database.tasks.BulkSaveTask;
import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.collection.contracts.CollectionContract;

public abstract class BaseCollection<T extends BaseModel> extends ArrayList<T> implements CollectionContract<T> {

    public BaseCollection() {super(); }

    public BaseCollection(List<T> list) {
        super(list);
    }

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

    @Override
    public T findById(int id) {
        for(T m: this) {
            if(m.getId() == id) {
                return m;
            }
        }

        return null;
    }
}
