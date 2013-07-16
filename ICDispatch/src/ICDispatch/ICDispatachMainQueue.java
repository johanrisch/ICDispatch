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
import java.util.concurrent.LinkedBlockingQueue;

import android.os.Handler;
/**
 * 
 * @author johanrisch
 *  ICDispatachMainQueue is the queue that runs blocks on the main thread.
 *  In order to improve the FPS performance there is a parameter that limits the amount of new Blocks to be sent
 *  each iteration of the run loop. This is done by having a {@link MainRunnablePool} containing {@link MainRunnable} that 
 *  will add itself back to the pool once they've been executed.
 */
public class ICDispatachMainQueue extends ICQueue {
    private Handler mHandler;
    private MainRunnablePool mRunnablePool;
    private int mMaxNumberPerLoop;

    public ICDispatachMainQueue(BlockingQueue<ICBlock> queue, Handler mainThreadHandler, int maxNumberPerLoop) {
        super(queue);
        this.mMaxNumberPerLoop = maxNumberPerLoop;
        this.mHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        mRunnablePool = new MainRunnablePool(mMaxNumberPerLoop);
        ICBlock currentBlock = null;

        while (running) {
            try {
                currentBlock = mQueue.take();
                mHandler.post(mRunnablePool.getRunnable(currentBlock));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    
    private class MainRunnable implements Runnable {
        private ICBlock mBlock;
        private MainRunnablePool mPool;

        public MainRunnable(MainRunnablePool mainRunnablePool) {
            this.mPool = mainRunnablePool;
        }

        public void setBlock(ICBlock block) {
            this.mBlock = block;
        }

        @Override
        public void run() {
            mBlock.run();
            mPool.returnRunnable(this);

        }

    }

    private class MainRunnablePool {
        private LinkedBlockingQueue<MainRunnable> mPool;

        public MainRunnablePool(int poolSize) {
            this.mPool = new LinkedBlockingQueue<MainRunnable>();
            for (int i = 0; i < poolSize; i++) {
                mPool.add(new MainRunnable(this));
            }
        }

        public MainRunnable getRunnable(ICBlock block) {
            try {
                MainRunnable ret = mPool.take();
                ret.setBlock(block);
                return ret;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        public void returnRunnable(MainRunnable r) {
            mPool.add(r);
        }
    }

}
