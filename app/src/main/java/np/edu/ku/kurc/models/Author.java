package np.edu.ku.kurc.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import np.edu.ku.kurc.database.schema.AuthorSchema;
import np.edu.ku.kurc.database.schema.SchemaFactory;
import np.edu.ku.kurc.models.collection.AuthorCollection;
import np.edu.ku.kurc.models.collection.BaseCollection;
import np.edu.ku.kurc.models.transformers.AuthorTransformer;
import np.edu.ku.kurc.models.transformers.BaseTransformer;
import np.edu.ku.kurc.models.transformers.TransformerFactory;

public class Author extends BaseModel<Author, AuthorSchema> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("slug")
    public String slug;

    @SerializedName("link")
    public String link;

    @SerializedName("avatar")
    public String avatar;

    @Override
    public AuthorSchema getSchema() {
        return SchemaFactory.getInstance(AuthorSchema.class);
    }

    @Override
    public BaseCollection<Author> getCollection(List<Author> list) {
        return new AuthorCollection(list);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public BaseTransformer<Author> getTransformer() {
        return TransformerFactory.getInstance(AuthorTransformer.class);
    }
}
