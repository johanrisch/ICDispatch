package ICDispatch;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author johanrisch Concurrent version of the ICQueue All ICBlocks sent to
 *         this queue will be scheduled for execution immediately. There is
 *         absolutely no guarantee that the blocks will be executed in the order
 *         they where added.
 */
public class ICConCurrentQueue extends ICQueue {

    private ExecutorService mExecutor;

    public ICConCurrentQueue(BlockingQueue<ICBlock> queue, int maxThreads) {
        super(queue);

        int cores = maxThreads;
        if (cores == 0) {
            Runtime.getRuntime().availableProcessors();
        }
        this.mExecutor = Executors.newFixedThreadPool(cores);
    }

    @Override
    public void run() {
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
