package np.edu.ku.kurc.database.schema;

import android.database.sqlite.SQLiteDatabase;

public class CategoryPostSchema extends BaseSchema {

    public static final String TABLE_NAME = "category_post";

    public static final String COLUMN_POST_ID = "post_id";
    public static final String COLUMN_CATEGORY_ID = "category_id";

    /**
     * Creates Table.
     *
     * @param database  Database instance.
     */
    public static void onCreate(SQLiteDatabase database) {
        String createSql = TABLE_CREATE + " " + TABLE_IF_NOT_EXISTS + " " + TABLE_NAME + " (" +
                COLUMN_POST_ID + " " + TYPE_INT + "," +
                COLUMN_CATEGORY_ID + " " + TYPE_INT + "," +
                CONSTRAINT_PK + "(" + COLUMN_POST_ID + "," + COLUMN_CATEGORY_ID + ")," +
                CONSTRAINT_FK + "(" + COLUMN_POST_ID + ") " +
                CONSTRAINT_REFERENCES + " " + PostSchema.TABLE_NAME + "(" + PostSchema.COLUMN_ID + ")," +
                CONSTRAINT_FK + "(" + COLUMN_CATEGORY_ID + ") " +
                CONSTRAINT_REFERENCES + " " + CategorySchema.TABLE_NAME + "(" + CategorySchema.COLUMN_ID + ")" + " )";

        database.execSQL(createSql);
    }

    /**
     * Upgrades Table.
     *
     * @param database      Database instance.
     * @param oldVersion    Old Version database.
     * @param newVersion    New Version database.
     */
    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL(TABLE_DROP + " " + TABLE_IF_EXISTS + " " + TABLE_NAME);
        onCreate(database);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
