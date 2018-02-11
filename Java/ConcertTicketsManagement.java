import java.util.Random;
import java.util.concurrent.Semaphore;

public class ConcertTicketsManagement {

	private static final int N = 1000;

	private static final int MAX_TICKETS_PER_FAN = 4;

	private static final int PRICE_PER_TICKET = 800;

	private static Random r = new Random();

	private static int ticketsRemaining = N;

	private static int income = 0;

	private static Semaphore mutex = new Semaphore(1);

	public static void main(String[] args) {
		Fan[] fans = new Fan[100];
		for (int i = 1; i < fans.length; i++) {
			fans[i] = new Fan(i);
			fans[i].start();
		}
	}

	private static class Fan extends Thread {

		int id;

		Fan(int id) {
			this.id = id;
		}

		@Override
		public void run() {
			while (true) {
				try {
					mutex.acquire();
					int rand = r.nextInt(4) + 1;
					if (ticketsRemaining >= rand) {
						ticketsRemaining -= rand;
						System.out.println("Fan " + id + " purchased " + rand + " tickets");
						System.out.println("Tickets remaining: " + ticketsRemaining);
						income += rand * PRICE_PER_TICKET;
						System.out.println("Income: " + income);
					} else {
						System.out.println("\nExiting... \n Tickets Remaining: " + ticketsRemaining);
						System.exit(1);
					}
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
				finally {
					mutex.release();
				}
			}
		}
	}

}

