package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.CategoryCollection;

public interface ModelContract<M extends BaseModel> {

    long save(Context context);

    long save(SQLiteDatabase db);

    int count(Context context);

    int count(SQLiteDatabase db);

    BaseCollection<M> all(Context context);

    int getId();
}
