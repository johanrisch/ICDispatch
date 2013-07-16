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
import android.app.Application;

/**
 * Created by johanrisch on 6/21/13.
 */
public class ICDispatchApplication extends Application{
    protected static ICDispatch sICDispatch;

    @Override
    public void onCreate() {
        super.onCreate();
        sICDispatch = new ICDispatch();
        initICDispatch();
        sICDispatch.initICDispatch();
    }
    public static boolean executeOn(int queue, ICBlock block){
        return sICDispatch.executeOn(queue,block);
    }
    public static boolean executeMethodOn(int queue,Object instance, String methodName, Object... args) throws NoSuchMethodException{
        return sICDispatch.executeMethodOn(queue,instance,methodName,args);
    }
    /**
     * Override this method if you want to initialize {@link ICDispatch} with custom params.
     * 
     */
    protected void initICDispatch(){
        
    }
}
