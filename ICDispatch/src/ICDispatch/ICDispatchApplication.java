package ICDispatch;

import android.app.Application;

/**
 * Created by johanrisch on 6/21/13.
 */
public class ICDispatchApplication extends Application{
    protected static ICDispatch sICDispatch;

    @Override
    public void onCreate() {
        super.onCreate();
        sICDispatch = new ICDispatch();
        initICDispatch();
        sICDispatch.initICDispatch();
    }
    public static boolean executeOn(int queue, ICBlock block){
        return sICDispatch.executeOn(queue,block);
    }
    public static boolean executeMethodOn(int queue,Object instance, String methodName, Object... args) throws NoSuchMethodException{
        return sICDispatch.executeMethodOn(queue,instance,methodName,args);
    }
    /**
     * Override this method if you want to initialize {@link ICDispatch} with custom params.
     * 
     */
    protected void initICDispatch(){
        
    }
}
