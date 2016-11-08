package np.edu.ku.kurc.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import np.edu.ku.kurc.database.schema.AuthorSchema;
import np.edu.ku.kurc.database.schema.CategoryPostSchema;
import np.edu.ku.kurc.database.schema.CategorySchema;
import np.edu.ku.kurc.database.schema.MediaSchema;
import np.edu.ku.kurc.database.schema.MediaSizeSchema;
import np.edu.ku.kurc.database.schema.PostSchema;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Database Name.
     */
    public static final String DB_NAME = "kurc.db";

    /**
     * Database Version Constant.
     */
    private static final int DB_VERSION = 1;

    /**
     * DatabaseHelper Instance.
     */
    private static DatabaseHelper instance;

    /**
     * Returns instance of DatabaseHelper.
     *
     * @param context   Application Context.
     * @return          DatabaseHelper instance.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if(instance == null) {
            instance  = new DatabaseHelper(context);
        }

        return instance;
    }

    /**
     * Database Helper Constructor.
     *
     * @param context Application Context.
     */
    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase database) {
        AuthorSchema.onCreate(database);
        MediaSchema.onCreate(database);
        MediaSizeSchema.onCreate(database);
        PostSchema.onCreate(database);
        CategorySchema.onCreate(database);
        CategoryPostSchema.onCreate(database);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        AuthorSchema.onUpgrade(database,oldVersion,newVersion);
        MediaSchema.onUpgrade(database,oldVersion,newVersion);
        MediaSizeSchema.onUpgrade(database,oldVersion,newVersion);
        PostSchema.onUpgrade(database,oldVersion,newVersion);
        CategorySchema.onUpgrade(database,oldVersion,newVersion);
        CategoryPostSchema.onUpgrade(database,oldVersion,newVersion);
    }
}
