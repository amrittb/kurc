package np.edu.ku.kurc.models.transformers.contracts;

import np.edu.ku.kurc.database.schema.BaseSchema;
import np.edu.ku.kurc.models.BaseModel;

public interface TransformerResolverContract<M extends BaseModel,S extends BaseSchema> {

    /**
     * Returns model transformer.
     *
     * @return Model transformer instance for model and schema.
     */
    ModelTransformerContract<M,S> getTransformer();
}
