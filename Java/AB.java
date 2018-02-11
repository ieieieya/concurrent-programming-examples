import java.util.concurrent.Semaphore;

public class AB {
	static Semaphore s1 = new Semaphore(1);
	static Semaphore s2 = new Semaphore(0);

	public static void main(String[] args) throws InterruptedException {
		A a = new A();
		B b = new B();

		a.start();
		b.start();
	}
	
	private static class A extends Thread{

		@Override
		public void run() {
			while(true) {
				try {
					s1.acquire();
					System.out.print("A");
					s2.release();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static class B extends Thread {

		@Override
		public void run() {
			while (true) {
				try {
					s2.acquire();
					System.out.print("B");
					s1.release();
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

