package np.edu.ku.kurc.models.contracts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;

public interface ModelContract<M extends BaseModel> {

    /**
     * Saves model into database.
     *
     * @param context   Application context.
     * @throws DatabaseErrorException
     */
    void save(Context context) throws DatabaseErrorException;

    /**
     * Saves Model into given database.
     *
     * @param db    Database into which model is to be saved.
     * @throws DatabaseErrorException
     */
    void save(SQLiteDatabase db) throws DatabaseErrorException;

    /**
     * Counts number of rows in table.
     *
     * @param context   Application context.
     * @return          Number of rows in table.
     */
    int count(Context context);

    /**
     * Counts number of rows in table.
     *
     * @param db    Database to fetch table rows count from.
     * @return      Number of rows in table.
     */
    int count(SQLiteDatabase db);

    /**
     * Returns all rows in model table.
     *
     * @param context   Application Context.
     * @return          Collection of rows in table.
     */
    BaseCollection<M> all(Context context);

    /**
     * Returns primary key of model.
     *
     * @return  Primary key of model.
     */
    int getId();
}
