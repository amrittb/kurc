package np.edu.ku.kurc.models.transformers.contracts;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.database.schema.BaseSchema;

public interface ModelTransformerContract<M extends BaseModel,S extends BaseSchema> {

    /**
     * Converts model into content values.
     *
     * @param model     Model to convert.
     * @param schema    Schema of model for which the content values are to be generated.
     * @return          Content values to insert or update the database.
     */
    ContentValues toContentValues(M model, S schema);

    /**
     * Converts a cursor instance to Model.
     *
     * @param c     Cursor to be converted.
     * @return      Model instance from cursor.
     */
    M toModel(Cursor c);
}
