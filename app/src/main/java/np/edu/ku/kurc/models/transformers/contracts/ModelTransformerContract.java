package np.edu.ku.kurc.models.transformers.contracts;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.database.schema.BaseSchema;

public interface ModelTransformerContract<M extends BaseModel> {

    /**
     * Converts model into content values.
     *
     * @param model     Model to convert.
     * @return          Content values to insert or update the database.
     */
    ContentValues toContentValues(M model);

    /**
     * Converts a cursor instance to Model.
     *
     * @param c     Cursor to be converted.
     * @return      Model instance from cursor.
     */
    M toModel(Cursor c);

    /**
     * Populates a cursor instance into model.
     *
     * @param c     Cursor to be converted.
     * @param model Model instance from cursor.
     */
    void toModel(Cursor c, M model);
}
