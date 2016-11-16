package np.edu.ku.kurc.models.contracts;

import android.content.Context;
import android.database.Cursor;
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
    long save(Context context) throws DatabaseErrorException;

    /**
     * Saves Model into given database.
     *
     * @param db    Database into which model is to be saved.
     * @throws DatabaseErrorException
     */
    long save(SQLiteDatabase db) throws DatabaseErrorException;

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
     * Returns latestPaginated rows.
     *
     * @param context   Application Context.
     * @param perPage   Rows per page.
     * @param page      Page of which posts are to be fetched.
     * @return          Collection of rows in table.
     */
    BaseCollection<M> paginated(Context context, int perPage, int page);

    /**
     * Returns primary key of model.
     *
     * @return  Primary key of model.
     */
    int getId();

    /**
     * Populates model object from database by id.
     *
     * @param context   Application Context.
     * @param id        Id for which the model is to be populated.
     * @return          Model for Id.
     */
    M findById(Context context, int id);

    /**
     * Returns collection of rows from cursor.
     *
     * @param cursor    Cursor to transform.
     * @return          Collection of rows.
     */
    BaseCollection<M> getCollection(Cursor cursor);

    /**
     * Converts a cursor into model.
     *
     * @param cursor    Cursor to transform.
     * @return          Model object from cursor.
     */
    M get(Cursor cursor);
}
