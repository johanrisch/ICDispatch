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

import java.util.Collection;
import java.util.concurrent.BlockingQueue;

/**
 * Created by johanrisch on 6/18/13.
 */

/**
 * The thread class used by {@see ICDispatch} to execute {@see ICBlock} on.
 */
public class ICQueue extends Thread{
    /**
     * The internal {@see BlockingQueue} for the thread.
     */
    protected BlockingQueue<ICBlock> mQueue;
    /**
     * Boolean used to determine if the thread should continue.
     */
    protected boolean running = true;

    /**
     * Creates a new ICThread with the specified Queue.
     * @param queue the queue to contain all{@see ICBlock}s.
     */
    public ICQueue(BlockingQueue<ICBlock> queue) {
        mQueue = queue;
    }

    /**
     * Puts a block into the Queue
     * @param block the block to push.
     */
    synchronized void putBlock(ICBlock block) {
        mQueue.add(block);
       
    }
    
    synchronized void putAll(Collection<ICBlock> blocks){
        mQueue.addAll(blocks);
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
                currentBlock.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }


    }

}
