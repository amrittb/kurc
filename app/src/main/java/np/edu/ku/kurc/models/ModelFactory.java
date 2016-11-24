package np.edu.ku.kurc.models;

import java.util.HashMap;

public abstract class ModelFactory {

    private static HashMap<Class<? extends BaseModel>,BaseModel> instances = new HashMap<>();

    public static <T extends BaseModel> T getInstance(Class<T> modelClass) {
        if( ! instances.containsKey(modelClass)) {
            try {
                instances.put(modelClass, modelClass.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (T) instances.get(modelClass);
    }
}
