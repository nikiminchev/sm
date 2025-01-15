package co.granthika.interview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import co.granthika.interview.commands.AbstractCommand;
import co.granthika.interview.store.Container;
import co.granthika.interview.store.ContainersPool;
import co.granthika.interview.store.Store;
import co.granthika.interview.workers.Robot;

public class TestRobot {

	private static boolean DEBUG = false;
	private  int numContainers=100000;
	private static Store store = null;
	private int poolSize = 7;
	private static List<String> names = new ArrayList<String>();
	private static int threadCount;
	private static final int NUM_THREADS = 6;
	
	public static void main(String[] args) {
		try {
			Store.DEBUG_MODE = DEBUG;

			threadCount=0;

			ExecutorService executor = Executors.newFixedThreadPool(NUM_THREADS);

			TestRobot tr = new TestRobot();
			Robot r0 = tr.initStore(tr.numContainers);
			store = r0.getStore();
			executeTask(() -> {
				System.err.println("1 start");
				tr.test1(r0);
				System.err.println("1 end");
				synchronized(store) {
					threadCount++;
				}
	        }, executor);
			
			executeTask(() -> {
				Robot r = new Robot(store);
				System.err.println("2 start");
				tr.test1(r);
				System.err.println("2 end");
				synchronized(store) {
					threadCount++;
				}
	        }, executor);
			
			executeTask(() -> {
				Robot r = new Robot(store);
				System.err.println("3 start");
				tr.test1(r);
				System.err.println("3 end");
				synchronized(store) {
					threadCount++;
				}
	        }, executor);
			
			executeTask(() -> {
				Robot r = new Robot(store);
				System.err.println("4 start");
				tr.test1(r);
				System.err.println("4 end");
				synchronized(store) {
					threadCount++;
				}
	        }, executor);
			
			executeTask(() -> {
				Robot r = new Robot(store);
				System.err.println("5 start");
				tr.test1(r);
				System.err.println("5 end");
				synchronized(store) {
					threadCount++;
				}
	        }, executor);
			
			executeTask(() -> {
				while(threadCount<NUM_THREADS-1) {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				System.exit(0);
	        }, executor);

//			tr.test2();
//			tr.test3();
//			tr.test4();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
//		System.exit(0);
	}
	
	Robot initStore(int numContainers){
		Robot robot = null;
		boolean create = true;
		if(store==null) {
			store =  new Store(numContainers, poolSize);
		}
		
		robot = new Robot(store);
		

		numContainers = robot.getStore().getContainerRoom()[0].getPoolSize();
		if(DEBUG) {
			System.err.println("numPools="+numContainers);
			System.err.println("pool:"+robot.getStore().toString());
		}
		if(create) {
			create = false;
			for(int i=0; i<numContainers; i++) {
				names.add("b"+i);
			}
		}
		
		return robot;
	}
	void test1(Robot robot) {
		long start = System.currentTimeMillis();
		
		try {
			AbstractCommand command = null;
			
			numContainers = robot.getStore().getContainerRoom().length;

			for(int k=0; k<100; k++) {
				for(int i=0; i<100; i++) {

					if(DEBUG) {
						System.out.println("i="+i);
						System.out.println(robot.toString());
					}
					int a = (int)(Math.random()*(poolSize-1));
					int c = (int)(Math.random()*(poolSize-1));
					double b = Math.random();
					if(DEBUG) {
						System.err.println("a="+a);
						System.err.println("c="+c);
						System.err.println("b="+b);
					}
					if(b>0.5) {
						command = robot.fill(names.get(a), (int)(Math.random()*50));
					}else {
						if(a==c) {
							return;
						}
						command = robot.move(names.get(a), names.get(c));
					}
					if(DEBUG) {
						System.out.println(command.print());
					}
				}
				
				
//				for(int i=0; i<30;i++) {
//					if(DEBUG) {
//						System.out.println("i="+i);
//						System.out.println(robot.toString());
//					}
//					double b = Math.random();
//					if(DEBUG) {
//						System.out.println("b="+b);
//					}
//					if(b>0.5) {
//						command = robot.undo();
//					}else {
//						command = robot.redo();
//					}
//					if(DEBUG) {
//						System.out.println(command.print());
//					}
//				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		} finally {
			long end = System.currentTimeMillis();
			System.out.println("Number of Container Pools="+robot.getStore().getContainerRoom().length);
			printTime(start, end);
		}
	}

	void test2(int numContainers) {
		Store store = new Store(numContainers, poolSize);
		Robot robot = new Robot(store);
		List<String> names = new ArrayList<String>();
		for(int i=0; i<numContainers; i++) {
			names.add("b"+i);
		}
		AbstractCommand command = null;
		for(int k=0; k<100; k++) {
			for(int i=0; i<100; i++) {
				if(DEBUG) {
					System.out.println("i="+i);
					System.out.println(robot.toString());
				}
				int a = (int)(Math.random()*(numContainers-1));
				int c = (int)(Math.random()*(numContainers-1));
				double b = Math.random();
				if(DEBUG) {
					System.out.println("a="+a);
					System.out.println("c="+c);
					System.out.println("b="+b);
				}
				if(b>0.5) {
					command = robot.fill(names.get(a), (int)(Math.random()*50));
				}else {
					if(a==c) {
						continue;
					}
					command = robot.move(names.get(a), names.get(c));
				}
				if(DEBUG) {
					System.out.println(command.print());
				}
			}
			
			
//			for(int i=0; i<30;i++) {
//				if(DEBUG) {
//					System.out.println("i="+i);
//					System.out.println(robot.toString());
//				}
//				double b = Math.random();
//				if(DEBUG) {
//					System.out.println("b="+b);
//				}
//				if(b>0.5) {
//					command = robot.undo();
//				}else {
//					command = robot.redo();
//				}
//				if(DEBUG) {
//					System.out.println(command.print());
//				}
//			}
		}
	}
	
	void test3() {
		ContainersPool[] pool = new ContainersPool[10];

		pool[0] = new ContainersPool(3);
		pool[1] = new ContainersPool(3);
		pool[2] = new ContainersPool(3);
		pool[3] = new ContainersPool(3);
		pool[4] = new ContainersPool(3);
		pool[5] = new ContainersPool(3);
		pool[6] = new ContainersPool(3);
		pool[7] = new ContainersPool(3);
		pool[8] = new ContainersPool(3);
		pool[9] = new ContainersPool(3);
		
		pool[0].push(new Container("b0", 0));pool[0].push(new Container("b10", 0));pool[0].push(new Container("b20", 0));
		pool[1].push(new Container("b1", 0));pool[1].push(new Container("b11", 44));pool[1].push(new Container("b21", 0));
		pool[2].push(new Container("b2", 0));pool[2].push(new Container("b16", 0));pool[2].push(new Container("b6", 0));
		pool[3].push(new Container("b3", 0));pool[3].push(new Container("b13", 0));pool[3].push(new Container("b23", 0));
		pool[4].push(new Container("b4", 0));pool[4].push(new Container("b14", 0));
		pool[5].push(new Container("b5", 0));pool[5].push(new Container("b15", 10));
		pool[6].push(new Container("b17", 0));pool[6].push(new Container("b19", 0));
		pool[7].push(new Container("b7", 28));pool[7].push(new Container("b12", 7));
		pool[8].push(new Container("b8", 10));pool[8].push(new Container("b22", 38));
		pool[9].push(new Container("b9", 45));pool[9].push(new Container("b18", 0));
		
		Robot robot = new Robot(pool);

		if(DEBUG) {
			System.out.println(robot.toString());
		}
		robot.move("b6", "b2");
		if(DEBUG) {
			System.out.println(robot.toString());
		}
	}
	void test4() {
		ContainersPool[] pool = new ContainersPool[10];

		pool[0] = new ContainersPool(3);
		pool[1] = new ContainersPool(3);
		pool[2] = new ContainersPool(3);
		pool[3] = new ContainersPool(3);
		pool[4] = new ContainersPool(3);
		pool[5] = new ContainersPool(3);
		pool[6] = new ContainersPool(3);
		pool[7] = new ContainersPool(3);
		pool[8] = new ContainersPool(3);
		pool[9] = new ContainersPool(3);
		
		pool[0].push( new Container("b0", 0));pool[0].push( new Container("b10", 31));pool[0].push( new Container("b21", 0));
		pool[1].push( new Container("b1", 0));pool[1].push( new Container("b11", 47));
		pool[2].push( new Container("b2", 0));pool[2].push( new Container("b12", 0));pool[2].push( new Container("b22", 0));
		pool[3].push( new Container("b3", 0));pool[3].push( new Container("b13", 0));pool[3].push( new Container("b23", 0));
		pool[4].push( new Container("b4", 0));pool[4].push( new Container("b14", 0));pool[4].push( new Container("b20", 44));
		pool[5].push( new Container("b5", 0));pool[5].push( new Container("b15", 0));
		pool[6].push( new Container("b6", 0));pool[6].push( new Container("b16", 0));
		pool[7].push( new Container("b7", 0));pool[7].push( new Container("b17", 40));
		pool[8].push( new Container("b8", 0));pool[8].push( new Container("b18", 0));
		pool[9].push( new Container("b9", 0));pool[9].push( new Container("b19", 0));
		
		Robot robot = new Robot(pool);

		if(DEBUG) {
			System.out.println(robot.toString());
		}
		robot.move("b6", "b2");
		if(DEBUG) {
			System.out.println(robot.toString());
		}
	}
	
	void testPrintTime() {
		long start = -1000L;
		long end = System.currentTimeMillis();
		printTime(start, end);
	}
	
	protected void printTime(long start, long end) {
		long time = end - start;
		System.out.println(time);
		Long[] arr= {1000L, 60L, 60L, 24L, 30L, 365L, 100L, 1000L};
		String[] arr2 = {"", ".", ":", ":", " months ", " years ", "centy-years", " milleniums "};
		List<Long> periods = new ArrayList<Long>(arr.length);
		List<String> periodDelimiters = new ArrayList<String>(arr2.length);
		for(Long a: arr) {
			periods.add(a);
		}
		for(String a: arr2) {
			periodDelimiters.add(a);
		}
		List<Long> times = new ArrayList<Long>();
		Iterator<Long> arrIter = periods.iterator();
		long tail = time;
		long next = arrIter.next();
		do {
			arrIter.remove();
			time = tail/next; 
			tail = tail%next;

			times.add(0, tail);

			next = arrIter.next();
		}while(next<=time&&arrIter.hasNext());
		times.add(0, time);
		
		int i=-1;
		for(Long timeL: times) {
			i++;
//			System.out.print(timeL);
			System.out.print(periodDelimiters.get(times.size()-i-1));
		}
		System.out.println();
	}
	
	public static void executeTask(Runnable task, ExecutorService executor) {
        executor.submit(task);
    }
}
