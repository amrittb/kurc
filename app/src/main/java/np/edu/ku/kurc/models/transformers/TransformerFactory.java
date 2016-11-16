package np.edu.ku.kurc.models.transformers;

import java.util.HashMap;

public abstract class TransformerFactory {

    private static HashMap<Class<? extends BaseTransformer>,BaseTransformer> instances = new HashMap<>();

    public static <T extends BaseTransformer> T getInstance(Class<T> transformerClass) {
        if( ! instances.containsKey(transformerClass)) {
            try {
                instances.put(transformerClass, transformerClass.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return (T) instances.get(transformerClass);
    }
}
