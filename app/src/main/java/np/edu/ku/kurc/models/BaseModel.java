package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.database.schema.BaseSchema;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.contracts.CollectionResolverContract;
import np.edu.ku.kurc.models.contracts.ModelContract;
import np.edu.ku.kurc.database.schema.contracts.SchemaResolver;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;
import np.edu.ku.kurc.models.transformers.contracts.TransformerResolverContract;

public abstract class BaseModel<M extends BaseModel,S extends BaseSchema> implements TransformerResolverContract<M,S>,
        ModelContract<M>,
        SchemaResolver<S>,
        CollectionResolverContract<M> {

    @Override
    public void save(Context context) throws DatabaseErrorException {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        save(db);
    }

    @Override
    public void save(SQLiteDatabase db) throws DatabaseErrorException {
        String tableName = getSchema().getTableName();

        long result = db.insertWithOnConflict(tableName,null, getTransformer().toContentValues((M) this, getSchema()),SQLiteDatabase.CONFLICT_REPLACE);

        if(result == -1) {
            throw new DatabaseErrorException("An error occurred while saving model");
        }
    }

    @Override
    public int count(Context context) {
        return count(DatabaseHelper.getInstance(context).getWritableDatabase());
    }

    @Override
    public int count(SQLiteDatabase db) {
        Cursor cursor = db.query(getSchema().getTableName(),new String[] { "count(*) as count" },null,null,null,null,null);

        cursor.moveToFirst();

        int count = cursor.getInt(0);

        cursor.close();

        return count;
    }

    @Override
    public BaseCollection<M> all(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(getSchema().getTableName(),null, null, null, null, null, null);

        ArrayList<M> list = new ArrayList<>();

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(getTransformer().toModel(cursor));

                cursor.moveToNext();
            }
        }

        return getCollection(list);
    }

}
