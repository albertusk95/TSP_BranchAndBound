package bin;
public class QueueTSP {
	
	private int[] arrIndexNode = new int[30];
	private double[] arrIndexCost = new double[30];
	private int size;
	private int head;
	private int tail;
	
	//constructor
	public QueueTSP() {
		size = 30;			//inisiasi ukuran queue
		head = -1;
		tail = -1;
		for (int i = 0; i < size; i++) {//inisiasi isi dan cost queue 
			arrIndexNode[i] = -1;
			arrIndexCost[i] = -1;
		}
	}
	
	//getter
	public int getQueueSize() {
		return size;
	}
	public int gethead() {
		return head;
	}
	public int gettail() {
		return tail;
	}
	public int[] getarrIndexNode() {
		return arrIndexNode;
	}
	public double[] getarrIndexCost() {
		return arrIndexCost;
	}
	
	//setter
	public void sethead(int h) {
		head = h;
	}
	public void settail(int t) {
		tail = t;
	}
	
	//checker
	public boolean isEmpty() {
		if (head == -1 && tail == -1) {
			return true;
		} else {
			return false;
		}
	}
	public boolean isOneElmt() {
		if (head == tail) {
			return true;
		} else {
			return false;
		}
	}
	
	//method
	public void printIsiQueue() {
		if (isEmpty()) {
			System.out.println("Queue kosong");
		} else {
			System.out.println("Queue - array of nodes");
			for (int i = 0; i <= tail; i++) {
				System.out.println(arrIndexNode[i]);
			}
			System.out.println("Queue - array of cost");
			for (int i = 0; i <= tail; i++) {
				System.out.println(arrIndexCost[i]);
			}
		}
	}
	
	public void push(int cityID, double costVal) {
		if (isEmpty()) {
			head++;
			tail++;
		} else {
			tail++;
		}
		arrIndexNode[tail] = cityID;
		arrIndexCost[tail] = costVal;
	}
	
	public void pop(int cityID) {
	
		//delete all elements
		if (isEmpty()) {
			System.out.println("QUEUE KOSONG TIDAK BISA DIHAPUS");
		} else {
			if (isOneElmt()) {
				arrIndexNode[head] = -1;
				arrIndexCost[head] = -1;
				head = -1;
				tail = -1;
			} else {
				for (int i = 0; i <= tail; i++) {
					arrIndexNode[i] = -1;
					arrIndexCost[i] = -1;
				}
				head = -1;
				tail = -1;
			}
		}
		
	}
	
}