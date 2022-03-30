import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Queue;

public class FCFS {
	int bt = 0;		 //실행시간 변수
	int ar = 0;		// 들어가는 시간 변수
	private int time = 0; 	// 시간 변수 
	int startTime = 0; 		// 포로세서가 실행을 시작할 시간 변수
	int processes = 0;		// 프로세스를 몇개를 받을 것인지 변수
	int pass; 				// 첫 bt에 정보를 넣기위해 만든 변수
	int processors = 0;		// 프로세서를 몇개 받을 것인지 변수
	int arrivalTime[];		// 각 프로세서들의 arrivaltime을 담을 배열
	int burstTime[];		// 각 프로세서들의 bursttime을 담을 배열
	int arraySize = 0;			// 리스트의 사이즈를 담는 변수
	boolean isExit;			// bt정보를 담을 때 사용되는 변수
	String[] name;			// 각 변수들의 이름을 담을 변수
	
	LinkedList<FCFSProcess> FCFSprocess = new LinkedList<>();		// 프로세스를 저장할 리스트
	
	FCFSProcess PresentArrivalFCFS;			// arrivaltime을 정의 할때 사용될 Process변수
	FCFSProcess PresentBurstFCFS;			// bursttime을 정의 할때 사용될 Process변수
	
	Queue<FCFSProcess> queue = new LinkedList<>();		// 기능 구현에 사용될 큐 변수
	Scanner scanner = new Scanner(System.in);		// scanf를 하기 위한 변수
	
	public FCFS() {
		base();		//입력 하는 함수
		arraySize = FCFSprocess.size();	// 프로세서 리스트를 미리 한번 pop을 하기에 처음 프로세서 리스트의 사이즈를 저장
		PresentArrivalFCFS = FCFSprocess.pop();	// 미리 한번 팝을 시킨 이유가 밑에서 팝을 해버리면 계속해서 처음에 팝이 되서 엉망이 되기에 처음에 팝을 해줌
		startTime = PresentArrivalFCFS.arrivalTime; // 첫 프로세서의 실행 시간을 시작할 시간을 가져와, 밑에서 했을 때의 오류를 잡음
		start(); // 프로세서 스케쥴링 시작
	}
	
	public void start() {
		Timer timer = new Timer(); // timer와 timertask를 사용해 카운트를 구현시켰습니다
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
					if(time <=20) { // 20초 이하일 때까지 실행
						Schedulling(); 											// 1초 마다 실행
						time++; // time변수를 증가시켜줘 초를 표현
					}
					else { // 20초가 넘어가면 멈춤
						timer.cancel();
					}
				}
			};
			timer.schedule(task, 1000,1000); 	// 1초마다 실행
	}
	
	/*변수들 입력받고 그 변수들이 프로세스들을 저장해주는 FCFS를 리스트화 시키는 작업을 해주는 함수*/
	public void base() {	// 변수들 입력받는 함수
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
		
		System.out.print("Process name: ");					// 여기까지 입력받는 곳
		for(int i = 0; i<processes; i++) {
			name[i] = scanner.next();
		}
		
		for(int i = 0; i<processes; i++) {					// processes 정보를 기입하고 LinkedList에 추가
			FCFSProcess process = new FCFSProcess(arrivalTime[i], burstTime[i], name[i]);	// FCFSProcesses 생성자 참고
			FCFSprocess.add(process);
		}
	}
	
	/*스케쥴링 해주는 함수(FCFS스케줄링의 메인함수)
	 * */	
	public void Schedulling() {
		System.out.println(time);

		ar = PresentArrivalFCFS.arrivalTime; 							// PresentArrivalFCFS는 현재 실행중인 FCFS -> 이놈의 도착 시간이 필요함 -> 이걸 ar에 저장
		if (ar == time) {
			System.out.println(PresentArrivalFCFS.name + "들어옴");
			queue.add(PresentArrivalFCFS);								// 여기에서 queue에 저장(현재 도착한 FCFS를)
			pass++;													
			if (FCFSprocess.size() != 0) {								// 리스트에 프로세서들이 있을 때만 작동하게 했음
				PresentArrivalFCFS = FCFSprocess.pop();					// 다음 프로세서의 정보를 얻기 위해 pop을 해줌
				if (pass == 1)											// pass가 1일 때만 isExit을 true로 만들어 처음 presentBurstFCFS를 정의하게 했음
					isExit = true;

			}
		}
		
		if (isExit) {													// 1초마다 이 함수가 실행이 되므로 이 IF문을 안걸러주면 계속해서 변하기 때문에 걸어줬음
			PresentBurstFCFS = queue.poll();							// PresentBurstFCFS에 큐에서 POP을 하여 정보를 넣어줌
			if (PresentBurstFCFS == null)								// 스케쥴링이 다 끝나면 return
				return;
			bt = PresentBurstFCFS.burstTime;							// 실행중인 프로세서의 burstTime을 bt에 저장
			isExit = false;												// 다시 false를 하여 밑의 if문이 실행 될때까지 false
		}
		
		if (time == startTime + bt) {									// bt시간이 되면 나가겠끔하는 if문
			System.out.println(PresentBurstFCFS.name + "나감");
			startTime = time;											// 처음에 startTime을 처음 들어온 프로세서의 arrivalTime을 설정하고, 그 후에는 프로세서가 끝나는 시간으로 해서 시간을 맞춰줌 
			isExit = true;												// 나갔으니 다음 정보가 필요하니 isExit을 true로 만들어줘 다음 정보를 받아옴
		}
			
		
	}
	public static void main(String[] args) {
		
		
		
	}
}

