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
        this.mExecutor = Executors.newFixedThreadPool(4);
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
