package np.edu.ku.kurc.views.viewmodels.contracts;

import android.content.Context;
import android.view.View;

abstract public class ViewModel<T> {

    protected Context context;

    /**
     * Creates a view model instance.
     *
     * @param root View root instance.
     */
    public ViewModel(View root) {
        this.context = root.getContext();
        this.onBindView(root);
    }

    /**
     * Creates a view model instance.
     *
     * @param root View root instance.
     * @param model Model to be bound.
     */
    public ViewModel(View root, T model) {
        this.onBindView(root);
        this.onBindModel(model);
    }

    /**
     * Binds view to view model instance.
     *
     * @param root View Root to be bound.
     */
    public abstract void onBindView(View root);

    /**
     * Binds Model to view model instance.
     *
     * @param model Model instance to be bound.
     */
    public abstract void onBindModel(T model);
}
