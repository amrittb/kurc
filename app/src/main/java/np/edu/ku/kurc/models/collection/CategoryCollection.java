package np.edu.ku.kurc.models.collection;

import java.util.List;

import np.edu.ku.kurc.models.Category;

public class CategoryCollection extends BaseCollection<Category> {

    public CategoryCollection(){super();}

    public CategoryCollection(List<Category> list) {
        super(list);
    }
}
