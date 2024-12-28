# ICDispatch
ICDispatch stands for InnocreateDispatch and is a framework designed to let android developers use similar syntax for executing tasks on background threads as Grand Central Dispatch (GCD) uses on iOS.

In order to use ICDispatch you must declare an application class that extends ICDispatchApplication. ICDispatch will then set itself up with default settings. 
App.java
```java
public class App extends ICDispatchApplication{
	
}
```
And please remember to modify the manifest to recognize the App.java as the application class ;)

After this the App.java class will have two important methods.
```java
public static boolean executeOn(int queue, ICBlock block)
public static boolean executeMethodOn(int queue, Object instance, String methodName, Object… args)
```
	
The `queue`parameter may have one of the following values:

- ICDispatch.LOW, for low priority blocks.
- ICDispatch.NORMAL, for normal priority blocks.
- ICDispatch.HIGH, for high priority blocks.
- ICDispatch.MAIN, for execution on the UI thread.
- ICDispatch.CONCURRENT, for concurrent execution of blocks.

Please note that all values for queue except for ICDispatch.CONCURRENT will guarantee that if block A is put on queue X before block B then block A will be fully executed before block B will be executed.

Example on how to use it:
```java
public void doStuff(){
	App.executeOn(ICDispatch.NORMAL, new ICBlock(){
		public void run(){
			int counter = 0;
			for(int i = 0; i < 10000; i++){
				counter += i % 2;
			}
			App.executeOn(ICDispatch.MAIN, new ICBlock(){
				change UI appropriately.
			});
		}
	});
}
```
You may queue new blocks inside a block on any queue. 

ICDispatch is under early development and will receive a lot of updates the coming months.

Future features include:

- support for fork-join queues.
- Automatic calibration in order to use optimal number of threads in the CONCURRENT queue per device.
- Possibility to customize settings. There are some up now but they are not tested.
- much more.

#License
ICDispatch uses the Apache 2.0 license, a copy of it can be found at:

<http://www.apache.org/licenses/LICENSE-2.0.html>
