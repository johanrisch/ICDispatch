package ICDispatch;
/**
 * Copyright 2013 Johan Risch (johan.risch@gmail.com) and Simon Evertsson (simon.evertsson2@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by johanrisch on 6/18/13.
 */
public class ICDispatch {
    /**
     * The normal priority thread.
     */
    private static ICQueue mNormalThread;
    /**
     * The low priority thread
     */
    private ICQueue mLowThread;
    /**
     * the high priority thread.
     */
    private ICQueue mHighThread;

    private ICDispatachMainQueue mMainQueue;

    private ICConCurrentQueue mConcurrentThread;

    /**
     * Static int telling {@see ICDispatch} to execute on the high priority
     * thread Constant value = 0
     */
    public static final int HIGH = 0;
    /**
     * Static int telling {@see ICDispatch} to execute on the normal priority
     * thread Constant value = 1
     */
    public static final int NORMAL = 1;
    /**
     * Static int telling {@see ICDispatch} to execute on the low priority
     * thread Constant value = 2
     */
    public static final int LOW = 2;
    /**
     * Static int telling {@see ICDispatch} to execute on the UI thread Constant
     * value = 3
     */
    public static final int MAIN = 3;

    public static final int CONCURRENT = 4;
    /**
     * Handler used to execute on UI thread.
     */
    private Handler mMainHandler;
    /**
     * Map used to cache method mappings.
     */
    private HashMap<String, Method> mMethodMap;
    /**
     * This parameter sets the maximum messages to be queued per UI thread run
     * loop. Default is 10.
     */
    private int mMaxMessagesOnUILoop = 10;

    /**
     * This parameter sets the maximum number of hashed methods. Default is 250
     */
    private int mMaxMethodsHashed = 250;
    /**
     * This parameter sets the maximum number of threads in the concurrent
     * queue. The default (0) is number of cores.
     */
    private int mMaxThreadsInConcurrent = 0;

    private boolean isInitialized = false;

    /**
     * initiates ICDispatch. MUST be called at before scheduling blocks.
     */
    public void initICDispatch() {

        mConcurrentThread = new ICConCurrentQueue(new LinkedBlockingQueue<ICBlock>(),
                mMaxThreadsInConcurrent);
        mConcurrentThread.setPriority(Thread.NORM_PRIORITY);
        mConcurrentThread.start();

        mHighThread = new ICQueue(new LinkedBlockingQueue<ICBlock>());
        mHighThread.setPriority(Thread.MAX_PRIORITY);
        mHighThread.start();

        mMainHandler = new Handler();
        mMainQueue = new ICDispatachMainQueue(new LinkedBlockingQueue<ICBlock>(), mMainHandler,
                mMaxMessagesOnUILoop);
        mMainQueue.setPriority(Thread.MAX_PRIORITY);
        mMainQueue.start();

        mNormalThread = new ICQueue(new LinkedBlockingQueue<ICBlock>());
        mNormalThread.setPriority(Thread.NORM_PRIORITY);
        mNormalThread.start();

        mLowThread = new ICQueue(new LinkedBlockingQueue<ICBlock>());
        mLowThread.setPriority(Thread.MIN_PRIORITY);
        mLowThread.start();

        mMethodMap = new HashMap<String, Method>(mMaxMethodsHashed);

        isInitialized = true;
    }

    public int getMaxMessagesOnUILoop() {
        return mMaxMessagesOnUILoop;
    }
    /**
     * Sets the maximum number of messages per UI run loop. If more than max {@link ICBlock} is 
     * dispatched to the main thread in one run loop the blocks will wait on a separate thread until next run loop. 
     * @param max the maximum number of {@link ICBlock} in queue on the main thread. Default = 10
     * @throws RuntimeException if this method is called from the main thread OR before ICDispatch.initICDispatch has been called.
     */
    public void setMaxMessagesOnUILoop(int max) throws RuntimeException{
        checkForThreadException("setMaxMessagesOnUILoop");
        checkForInitException("setMaxMessagesOnUILoop");
        mMaxMessagesOnUILoop = max;
    }

    public int getMaxMethodsHashed() {
        return mMaxMethodsHashed;
    }
    /**
     * Sets the maximum number of hashed methods.
     * @param max number of hashed methods. default = 250
     * @throws RuntimeException if this method is called from the main thread OR before ICDispatch.initICDispatch has been called.
     */
    public void setMaxMethodsHashed(int max) throws RuntimeException {
        checkForThreadException("setMaxMethodsHashed");
        checkForInitException("setMaxMethodsHashed");
        mMaxMethodsHashed = max;
    }

    public int getMaxThreadsInConcurrent() {
        return mMaxThreadsInConcurrent;
    }
    /**
     * Sets the maximum number of threads in the concurrent queue.
     * @param max number of threads in the concurrent queue. Default is the number of cores on the current device.
     * @throws RuntimeException if this method is called from the main thread OR before ICDispatch.initICDispatch has been called.
     */
    public void setMaxThreadsInConcurrent(int max) throws RuntimeException{
        checkForThreadException("setMaxThreadsInConcurrent");
        checkForInitException("setMaxThreadsInConcurrent");
        mMaxThreadsInConcurrent = max;
    }

    private void checkForInitException(String method) throws RuntimeException {
        if (isInitialized) {
            throw new RuntimeException("Method " + method + " called after initialisation");
        }
    }
    private void checkForThreadException(String method){
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new RuntimeException("Method setMaxMethodsHashed not called from UI thread.");
        }
    }

    /**
     * Schedules a block for execution on the given queue.
     * 
     * @param block
     *            the block to be executed on the chosen queue
     * @param queue
     *            the queue to execute the supplied block on.
     * @return true if the enqueueing was successfull.
     * @throws RuntimeException
     *             if the supplied queue does not exist.
     */
    public boolean executeOn(int queue, final ICBlock block) {
        switch (queue) {
        case HIGH:
            mHighThread.putBlock(block);
            break;
        case NORMAL:
            mNormalThread.putBlock(block);
            break;
        case LOW:
            mLowThread.putBlock(block);
            break;
        case MAIN:
            mMainQueue.putBlock(block);
            break;
        case CONCURRENT:
            mConcurrentThread.putBlock(block);
            break;
        default:
            throw new RuntimeException("Invalid thread ID, " + queue +
                    " please supply one of LOW, NORMAL, HIGH, MAIN or CONCURRENT");
        }
        return true;
    }

    public void executeAllOn(int queue, Collection<ICBlock> blocks) {
        switch (queue) {
        case HIGH:
            mHighThread.putAll(blocks);
            break;
        case NORMAL:
            mNormalThread.putAll(blocks);
            break;
        case LOW:
            mLowThread.putAll(blocks);
            break;
        case MAIN:
            mMainQueue.putAll(blocks);
            break;
        case CONCURRENT:
            mConcurrentThread.putAll(blocks);
            break;
        default:
            throw new RuntimeException("Invalid thread ID, " + queue +
                    " please supply one of LOW, NORMAL, HIGH, MAIN or CONCURRENT");
        }
    }

    /**
     * Schedules a method for execution on the supplied thread. ICDispatch keeps
     * an internal Hashmap of method name and containing class in order to speed
     * up consecutive calls to the method. At the moment polymorphism is not
     * supported.
     * 
     * @param queue
     *            that the method execution should be put in.
     * @param instance
     *            the instance of the object that the method should be executed
     *            on.
     * @param name
     *            the name of the method to execute.
     * @param args
     *            the arguments for the method.
     * @return true if the execution was successful.
     * @throws NoSuchMethodException
     */
    @SuppressWarnings("rawtypes")
    public boolean executeMethodOn(int queue, final Object instance, String name,
            final Object... args) throws NoSuchMethodException {
        Method m = mMethodMap.get(instance.getClass().getName() + name);
        if (m == null) {
            // Method is not cached find the method...
            // TODO- handle case when hashmap of methods is too large.
            Class[] classes = null;
            if (args.length > 0) {
                classes = new Class[args.length];
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = args[i].getClass();
                }
            }
            m = instance.getClass().getMethod(name, classes);

            mMethodMap.put(instance.getClass().getName() + name, m);
        }
        final Method finalM = m;
        executeOn(queue, new ICBlock() {
            @Override
            public void run() {
                try {
                    finalM.invoke(instance, args);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        });

        return true;
    }
}
