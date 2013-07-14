package ICDispatch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ICConCurrentQueue extends ICQueue {

    private ExecutorService mExecutor;

    public ICConCurrentQueue(BlockingQueue<ICBlock> queue) {
        super(queue);
        this.mExecutor = Executors.newCachedThreadPool();
    }
    
    @Override
    public void run(){
        ICBlock currentBlock = null;

        while (running) {
            try {
                currentBlock = mQueue.take();
                mExecutor.execute(currentBlock);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        
    }

}
