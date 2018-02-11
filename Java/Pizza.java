import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Pizza {

	static final int S = 8;

	static int slices = S;

	static Lock mutex = new ReentrantLock();

	static Semaphore mutexS = new Semaphore(1);

	static Condition orderPizza = mutex.newCondition();

	static Condition deliverPizza = mutex.newCondition();

	static boolean first = true;

	static boolean havePizza = false;

	public static void main(String[] args) {
		int noOfStudents = 10;
		Student students[] = new Student[noOfStudents];
		for (int i = 0; i < noOfStudents; i++) {
			students[i] = new Student(i);
			students[i].start();
		}

		Kamal kamal = new Kamal();
		kamal.start();
	}

	private static class Kamal extends Thread {

		@Override
		public void run() {
			while (true) {
				mutex.lock();
				try {
					orderPizza.await();
					// make pizza
					System.out.println("Kamal brings a new pizza");
					slices = S;
					first = true;
					deliverPizza.signalAll();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				mutex.unlock();
			}   // while(true)
		}   // run()
	}

	private static class Student extends Thread {

		int id;
		Random r;

		public Student(int id) {
			this.id = id;
			r = new Random();
		}

		@Override
		public void run() {
			while (true) {
				while (!havePizza) {
					mutex.lock();
					if (slices > 0) {
						slices--;
						try {
							Thread.sleep(r.nextInt(1000));
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println("Student " + id + " eats pizza and studies");
						havePizza = true;
					} else {
						if (first) {
							orderPizza.signal();
							System.out.println("Student " + id + " orders a pizza");
							first = false;
						}
						System.out.println("Student " + id + " goes to sleep");
						try {
							deliverPizza.await();
						}
						catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					mutex.unlock();
				}   // while(!havePizza)
			}   // while(true)
		}   // run()
	}

}

