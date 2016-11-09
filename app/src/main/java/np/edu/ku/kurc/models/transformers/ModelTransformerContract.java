package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;
import android.database.Cursor;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.database.schema.BaseSchema;

public interface ModelTransformerContract<M extends BaseModel,S extends BaseSchema> {

    ContentValues toContentValues(M model, S schema);

    M toModel(Cursor c);
}
