package np.edu.ku.kurc.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import np.edu.ku.kurc.database.DatabaseHelper;
import np.edu.ku.kurc.models.transformers.TransformerResolverContract;

public abstract class BaseModel implements TransformerResolverContract, ModelContract, SchemaResolver {

    @Override
    public long save(Context context) {
        SQLiteDatabase db = DatabaseHelper.getInstance(context).getWritableDatabase();

        return save(db);
    }

    @Override
    public long save(SQLiteDatabase db) {
        String tableName = getSchema().getTableName();

        return db.insertWithOnConflict(tableName,null, getTransformer().transform(this, getSchema()),SQLiteDatabase.CONFLICT_REPLACE);
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
}
