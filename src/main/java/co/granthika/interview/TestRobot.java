package co.granthika.interview;

import java.util.ArrayList;
import java.util.List;

import co.granthika.interview.store.AbstractCommand;
import co.granthika.interview.store.Container;
import co.granthika.interview.store.ContainersPool;
import co.granthika.interview.store.Robot;
import co.granthika.interview.store.Store;

public class TestRobot {

	private static boolean DEBUG = true;
	
	public static void main(String[] args) {
		try {
			Store.DEBUG_MODE = DEBUG;
			TestRobot tr = new TestRobot();
			tr.test1();
//			tr.test2();
//			tr.test3();
//			tr.test4();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}
	void test1() {
		int numContainers=24;
		Robot robot = new Robot(numContainers, 3);
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

	void test2() {
		int numContainers=25;
		Robot robot = new Robot(numContainers, 3);
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
			
			
			for(int i=0; i<30;i++) {
				if(DEBUG) {
					System.out.println("i="+i);
					System.out.println(robot.toString());
				}
				double b = Math.random();
				if(DEBUG) {
					System.out.println("b="+b);
				}
				if(b>0.5) {
					command = robot.undo();
				}else {
					command = robot.redo();
				}
				if(DEBUG) {
					System.out.println(command.print());
				}
			}
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
		
		Robot robot = new Robot(pool, 3);

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
		
		Robot robot = new Robot(pool, 3);

		if(DEBUG) {
			System.out.println(robot.toString());
		}
		robot.move("b6", "b2");
		if(DEBUG) {
			System.out.println(robot.toString());
		}
	}
}
