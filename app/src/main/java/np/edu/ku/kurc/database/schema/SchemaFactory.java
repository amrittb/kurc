package np.edu.ku.kurc.database.schema;

import java.util.HashMap;

public abstract class SchemaFactory {

    private static HashMap<Class<? extends BaseSchema>,BaseSchema> instances = new HashMap<>();

    public static <T extends BaseSchema> T getInstance(Class<T> schema) {
        if( ! instances.containsKey(schema)) {
            try {
                instances.put(schema, schema.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (T) instances.get(schema);
    }
}
