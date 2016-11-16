package np.edu.ku.kurc.models.transformers;

import android.content.ContentValues;

import np.edu.ku.kurc.models.BaseModel;
import np.edu.ku.kurc.models.transformers.contracts.ModelTransformerContract;

public abstract class BaseTransformer<M extends BaseModel> implements ModelTransformerContract<M> {

    protected ContentValues transformed;

    public BaseTransformer() {
        transformed = new ContentValues();
    }
}
