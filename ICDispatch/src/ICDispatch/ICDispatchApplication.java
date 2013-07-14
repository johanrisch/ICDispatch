package ICDispatch;

import android.app.Application;

/**
 * Created by johanrisch on 6/21/13.
 */
public class ICDispatchApplication extends Application{
    private static ICDispatch sICDispatch;

    @Override
    public void onCreate() {
        super.onCreate();
        sICDispatch = new ICDispatch();
        sICDispatch.initICDispatch();
    }
    public static boolean executeOn(int queue, ICBlock block){
        return sICDispatch.executeOn(queue,block);
    }
    public static boolean executeMethodOn(int queue,Object instance, String methodName, Object... args) throws NoSuchMethodException{
        return sICDispatch.executeMethodOn(queue,instance,methodName,args);
    }
}
