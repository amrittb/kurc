package np.edu.ku.kurc.models.transformers.contracts;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.transformers.BaseTransformer;

public interface TransformerResolverContract<M extends BaseModel> {

    /**
     * Returns model transformer.
     *
     * @return Model transformer instance for model and schema.
     */
    BaseTransformer<M> getTransformer();
}
