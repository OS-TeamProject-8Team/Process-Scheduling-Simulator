import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Queue;

public class FCFS {
	int bt = 0;		 //����ð� ����
	int ar = 0;		// ���� �ð� ����
	private int time = 0; 	// �ð� ���� 
	int startTime = 0; 		// ���μ����� ������ ������ �ð� ����
	int processes = 0;		// ���μ����� ��� ���� ������ ����
	int pass; 				// ù bt�� ������ �ֱ����� ���� ����
	int processors = 0;		// ���μ����� � ���� ������ ����
	int arrivalTime[];		// �� ���μ������� arrivaltime�� ���� �迭
	int burstTime[];		// �� ���μ������� bursttime�� ���� �迭
	int arraySize = 0;			// ����Ʈ�� ����� ��� ����
	boolean isExit;			// bt������ ���� �� ���Ǵ� ����
	String[] name;			// �� �������� �̸��� ���� ����
	
	LinkedList<FCFSProcess> FCFSprocess = new LinkedList<>();		// ���μ����� ������ ����Ʈ
	
	FCFSProcess PresentArrivalFCFS;			// arrivaltime�� ���� �Ҷ� ���� Process����
	FCFSProcess PresentBurstFCFS;			// bursttime�� ���� �Ҷ� ���� Process����
	
	Queue<FCFSProcess> queue = new LinkedList<>();		// ��� ������ ���� ť ����
	Scanner scanner = new Scanner(System.in);		// scanf�� �ϱ� ���� ����
	
	public FCFS() {
		base();		//�Է� �ϴ� �Լ�
		arraySize = FCFSprocess.size();	// ���μ��� ����Ʈ�� �̸� �ѹ� pop�� �ϱ⿡ ó�� ���μ��� ����Ʈ�� ����� ����
		PresentArrivalFCFS = FCFSprocess.pop();	// �̸� �ѹ� ���� ��Ų ������ �ؿ��� ���� �ع����� ����ؼ� ó���� ���� �Ǽ� ������ �Ǳ⿡ ó���� ���� ����
		startTime = PresentArrivalFCFS.arrivalTime; // ù ���μ����� ���� �ð��� ������ �ð��� ������, �ؿ��� ���� ���� ������ ����
		start(); // ���μ��� �����층 ����
	}
	
	public void start() {
		Timer timer = new Timer(); // timer�� timertask�� ����� ī��Ʈ�� �������׽��ϴ�
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
					if(time <=20) { // 20�� ������ ������ ����
						Schedulling(); 											// 1�� ���� ����
						time++; // time������ ���������� �ʸ� ǥ��
					}
					else { // 20�ʰ� �Ѿ�� ����
						timer.cancel();
					}
				}
			};
			timer.schedule(task, 1000,1000); 	// 1�ʸ��� ����
	}
	
	/*������ �Է¹ް� �� �������� ���μ������� �������ִ� FCFS�� ����Ʈȭ ��Ű�� �۾��� ���ִ� �Լ�*/
	public void base() {	// ������ �Է¹޴� �Լ�
		System.out.print("#of Processes: ");
		processes = scanner.nextInt();
		
		System.out.print("#of Processors: ");
		processors = scanner.nextInt();
		
		arrivalTime = new int[processes];
		burstTime = new int[processes];
		name = new String[processes];
		System.out.print("#Arrival time: ");
		for(int i =0;i<processes; i++) {
			arrivalTime[i] = scanner.nextInt();
		}
		
		System.out.print("#Burst time: ");
		for(int i =0;i<processes; i++) {
			burstTime[i] = scanner.nextInt();
		}
		
		System.out.print("Process name: ");					// ������� �Է¹޴� ��
		for(int i = 0; i<processes; i++) {
			name[i] = scanner.next();
		}
		
		for(int i = 0; i<processes; i++) {					// processes ������ �����ϰ� LinkedList�� �߰�
			FCFSProcess process = new FCFSProcess(arrivalTime[i], burstTime[i], name[i]);	// FCFSProcesses ������ ����
			FCFSprocess.add(process);
		}
	}
	
	/*�����층 ���ִ� �Լ�(FCFS�����ٸ��� �����Լ�)
	 * */	
	public void Schedulling() {
		System.out.println(time);

		ar = PresentArrivalFCFS.arrivalTime; 							// PresentArrivalFCFS�� ���� �������� FCFS -> �̳��� ���� �ð��� �ʿ��� -> �̰� ar�� ����
		if (ar == time) {
			System.out.println(PresentArrivalFCFS.name + "����");
			queue.add(PresentArrivalFCFS);								// ���⿡�� queue�� ����(���� ������ FCFS��)
			pass++;													
			if (FCFSprocess.size() != 0) {								// ����Ʈ�� ���μ������� ���� ���� �۵��ϰ� ����
				PresentArrivalFCFS = FCFSprocess.pop();					// ���� ���μ����� ������ ��� ���� pop�� ����
				if (pass == 1)											// pass�� 1�� ���� isExit�� true�� ����� ó�� presentBurstFCFS�� �����ϰ� ����
					isExit = true;

			}
		}
		
		if (isExit) {													// 1�ʸ��� �� �Լ��� ������ �ǹǷ� �� IF���� �Ȱɷ��ָ� ����ؼ� ���ϱ� ������ �ɾ�����
			PresentBurstFCFS = queue.poll();							// PresentBurstFCFS�� ť���� POP�� �Ͽ� ������ �־���
			if (PresentBurstFCFS == null)								// �����층�� �� ������ return
				return;
			bt = PresentBurstFCFS.burstTime;							// �������� ���μ����� burstTime�� bt�� ����
			isExit = false;												// �ٽ� false�� �Ͽ� ���� if���� ���� �ɶ����� false
		}
		
		if (time == startTime + bt) {									// bt�ð��� �Ǹ� �����ڲ��ϴ� if��
			System.out.println(PresentBurstFCFS.name + "����");
			startTime = time;											// ó���� startTime�� ó�� ���� ���μ����� arrivalTime�� �����ϰ�, �� �Ŀ��� ���μ����� ������ �ð����� �ؼ� �ð��� ������ 
			isExit = true;												// �������� ���� ������ �ʿ��ϴ� isExit�� true�� ������� ���� ������ �޾ƿ�
		}
			
		
	}
	public static void main(String[] args) {
		
		
		
	}
}

