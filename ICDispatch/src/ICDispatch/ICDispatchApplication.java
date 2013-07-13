package ICDispatch;

import android.app.Application;

/**
 * Created by johanrisch on 6/21/13.
 */
public class ICDispatchApplication extends Application{
    private static ICDispatch mICDispatch;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mICDispatch = new ICDispatch();
        this.mICDispatch.initICDispatch();
    }
    public static boolean executeOn(int queue, ICBlock block){
        return mICDispatch.executeOn(queue,block);
    }
    public static boolean executeMethodOn(int queue,Object instance, String methodName, Object... args){
        return mICDispatch.executeMethodOn(queue,instance,methodName,args);
    }
}
