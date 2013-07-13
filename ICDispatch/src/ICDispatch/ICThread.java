package ICDispatch;

import java.util.concurrent.BlockingQueue;

/**
 * Created by johanrisch on 6/18/13.
 */

/**
 * The thread class used by {@see ICDispatch} to execute {@see ICBlock} on.
 */
public class ICThread extends Thread {
    /**
     * The internal {@see BlockingQueue} for the thread.
     */
    BlockingQueue<ICBlock> mQueue;
    /**
     * Boolean used to determine if the thread should continue.
     */
    protected boolean running = true;

    /**
     * Creates a new ICThread with the specified Queue.
     * @param queue the queue to contain all{@see ICBlock}s.
     */
    public ICThread(BlockingQueue<ICBlock> queue) {
        mQueue = queue;
    }

    /**
     * Puts a block into the Queue
     * @param block the block to push.
     */
    synchronized void putBlock(ICBlock block) {
        mQueue.add(block);
    }

    /**
     * Retrieves the next block to be executed from the queue.
     * @return the next {@see ICBlock} to be executed on this thread.
     */
    synchronized ICBlock getBlock() {
        return mQueue.poll();
    }

    /**
     * This method loops until running == false. Retrieving one block per loop, waiting if necessary.
     */
    @Override
    public void run() {
        ICBlock currentBlock = null;

        while (running) {
            try {
                currentBlock = mQueue.take();
                currentBlock.doAction();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }

}
