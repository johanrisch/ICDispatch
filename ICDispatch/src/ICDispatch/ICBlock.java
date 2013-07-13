package ICDispatch;


import android.os.Handler;

/**
 * Created by johanrisch on 6/18/13.
 * <br/>
 * This is the smallest stone of ICDispatch. An ICBlock contains the code to be executed
 * on a selected Queue.
 */
public abstract class ICBlock {
    int[] iArgs;
    float[] fArgs;
    double[] dArgs;
    boolean[] bArgs;
    Object[] oArgs;
    Handler mHandler;
    private ICThread mThread;

    /**
     * Creates an ICBlock with a handler created from the calling thread.
     */
    public ICBlock(){
    }


    public void initOArgs(Object... args){
        int length = args.length;
        this.oArgs = new Object[length];
        for(int i = 0; i < length; i++){
            this.oArgs[i] = args[i];
        }
    }
    public void initBArgs(boolean... args){
        int length = args.length;
        this.bArgs = new boolean[length];
        for(int i = 0; i < length; i++){
            this.bArgs[i] = args[i];
        }
    }
    public void initdArgs(double... args){
        int length = args.length;
        this.dArgs = new double[length];
        for(int i = 0; i < length; i++){
            this.dArgs[i] = args[i];
        }
    }
    public void initfArgs(float... args){
        int length = args.length;
        this.fArgs = new float[length];
        for(int i = 0; i < length; i++){
            this.fArgs[i] = args[i];
        }
    }
    public void initiArgs(int... args){
        int length = args.length;
        this.iArgs = new int[length];
        for(int i = 0; i < length; i++){
            this.iArgs[i] = args[i];
        }
    }
    public abstract void doAction();
}
