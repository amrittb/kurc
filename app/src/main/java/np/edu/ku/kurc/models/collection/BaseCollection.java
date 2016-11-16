package np.edu.ku.kurc.models.collection;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import np.edu.ku.kurc.common.AsyncCallback;
import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.database.tasks.BulkSaveTask;
import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.collection.contracts.CollectionContract;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;

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

    /**
     * Saves all collection entries into database synchronously.
     *
     * @param context   Application Context.
     * @throws DatabaseErrorException
     */
    @Override
    public void saveAllSync(Context context) throws DatabaseErrorException {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        saveAllSync(db);
    }

    /**
     * Saves all items synchronously.
     *
     * @param db    SQLiteDatabase instance.
     * @throws DatabaseErrorException
     */
    @Override
    public void saveAllSync(SQLiteDatabase db) throws DatabaseErrorException {
        try {
            db.beginTransaction();

            for(T model: this) {
                model.save(db);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Finds items by id.
     *
     * @param id    Id to find.
     * @return      Found item.
     */
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
