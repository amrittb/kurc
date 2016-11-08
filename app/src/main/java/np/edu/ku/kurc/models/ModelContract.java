package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public interface ModelContract {

    long save(Context context);
    long save(SQLiteDatabase db);

    int count(Context context);

    int count(SQLiteDatabase db);
}
