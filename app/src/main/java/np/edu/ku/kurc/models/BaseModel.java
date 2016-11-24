package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.database.schema.BaseSchema;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.collection.contracts.CollectionResolverContract;
import np.edu.ku.kurc.models.contracts.ModelContract;
import np.edu.ku.kurc.database.schema.contracts.SchemaResolver;
import np.edu.ku.kurc.models.exception.DatabaseErrorException;
import np.edu.ku.kurc.models.transformers.contracts.TransformerResolverContract;

public abstract class BaseModel<M extends BaseModel,S extends BaseSchema> implements TransformerResolverContract<M>,
        ModelContract<M>,
        SchemaResolver<S>,
        CollectionResolverContract<M> {

    @Override
    public long save(Context context) throws DatabaseErrorException {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        return save(db);
    }

    @Override
    public long save(SQLiteDatabase db) throws DatabaseErrorException {
        String tableName = getSchema().getTableName();

        long result = db.insertWithOnConflict(tableName,null, getTransformer().toContentValues((M) this),SQLiteDatabase.CONFLICT_REPLACE);

        if(result == -1) {
            throw new DatabaseErrorException("An error occurred while saving model");
        }

        return result;
    }

    @Override
    public int count(Context context) {
        return count(DatabaseHelper.getInstance(context).getWritableDatabase());
    }

    @Override
    public int count(SQLiteDatabase db) {
        Cursor cursor = db.query(getSchema().getTableName(),new String[] { "count(*) as count" },null,null,null,null,null,null);

        cursor.moveToFirst();

        int count = cursor.getInt(0);

        cursor.close();

        return count;
    }

    @Override
    public BaseCollection<M> all(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(getSchema().getTableName(),null, null, null, null, null, null, null);
        return getCollection(cursor);
    }

    @Override
    public BaseCollection<M> paginated(Context context, int perPage, int page) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        int offset = (perPage * (page - 1));

        Cursor cursor = db.query(getSchema().getTableName(),
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    null,
                                    Integer.toString(offset) + "," + Integer.toString(perPage));
        return getCollection(cursor);
    }

    @Override
    public M findById(Context context, int id) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();
        Cursor cursor = db.query(getSchema().getTableName(),
                                    null,
                                    getSchema().getPrimaryKey() + "= ?",
                                    new String[] { Integer.toString(id) },
                                    null,
                                    null,
                                    null,
                                    "1");

        return get(cursor);
    }

    @Override
    public BaseCollection<M> getCollection(Cursor cursor) {
        ArrayList<M> list = new ArrayList<>();

        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                list.add(getTransformer().toModel(cursor));

                cursor.moveToNext();
            }
        }

        cursor.close();

        return getCollection(list);
    }

    @Override
    public M get(Cursor cursor) {
        if(cursor.moveToFirst()) {
            getTransformer().toModel(cursor,(M) this);
            return (M) this;
        }

        cursor.close();

        return null;
    }

    public BaseCollection<M> allIn(Context context, List<Integer> ids) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        String sql = "SELECT * FROM " + getSchema().getTableName() + " WHERE " + getSchema().COLUMN_ID + " IN (" + TextUtils.join(",", ids) + ")";

        Cursor cursor = db.rawQuery(sql,null);

        return getCollection(cursor);
    }
}
