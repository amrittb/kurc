package np.edu.ku.kurc.database.tasks;

import android.content.Context;
import android.os.AsyncTask;

import np.edu.ku.kurc.common.AsyncCallback;
import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;

public class BulkSaveTask<U extends BaseModel> extends AsyncTask<Void,Void,Void> {

    private Context context;

    private BaseCollection<U> collection;

    private AsyncCallback<U, Void, Void> callback;

    /**
     * BulkSaveTask Constructor.
     *
     * @param context       Application context.
     * @param collection    Collection to save.
     * @param callback      AsyncCallback to call when saving is successful or failure.
     */
    public BulkSaveTask(Context context, BaseCollection<U> collection, AsyncCallback<U, Void, Void> callback) {
        this.context = context;
        this.collection = collection;
        this.callback = callback;
    }

    @Override
    protected void onPreExecute() {
        callback.onSetup();
    }

    @Override
    protected Void doInBackground(Void... list) {
        try {
            collection.saveAllSync(context);
        } catch (DatabaseErrorException e) {
            cancel(true);
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        callback.onSuccess(result);
    }

    @Override
    protected void onCancelled(Void result) {
        callback.onFailure();
    }
}