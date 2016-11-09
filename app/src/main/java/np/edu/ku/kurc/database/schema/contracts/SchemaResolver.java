package np.edu.ku.kurc.database.schema.contracts;

import np.edu.ku.kurc.database.schema.BaseSchema;

public interface SchemaResolver<T extends BaseSchema> {

    /**
     * Returns schema instance.
     *
     * @return Schema instance.
     */
    T getSchema();
}
