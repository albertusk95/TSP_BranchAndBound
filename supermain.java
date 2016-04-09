//file: supermain.java
package bin;
import java.util.*;
import java.io.IOException;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*; 
import java.util.Date;

public class supermain extends JFrame {
	
	// konstruktor
	public supermain(ReadFileEksternal rfe) {
		
		//KAMUS 
		Container cp = getContentPane();
		
		//ALGORITMA 
		
		//mengatur tampilan program
		cp.setBackground(Color.ORANGE);
		//cp.setLayout(new FlowLayout());
		cp.setLayout(new GridBagLayout());
		//inisiasi GridBagConstraints (koordinat pada frame)
		GridBagConstraints c = new GridBagConstraints();
		
		//menampilkan judul program pada koordinat (0,0)
		c.gridx = 0;
		c.gridy = 0;
		cp.add(new JLabel("TSP with Branch and Bound Algorithm"), c);
	
		//menampilkan garis pembatas pada koordinat (0,1)
		c.gridx = 0;
		c.gridy = 1;
		cp.add(new JLabel("--------------------------------------------------------"), c);
		
		//tombol untuk memulai program
		JButton btnSolve = new JButton("Solve");
		c.gridx = 0;
		c.gridy = 3;
		cp.add(btnSolve, c);
		
		btnSolve.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//KAMUS
				int totalBobot = 0;
				int minBobot;
				int idxSolusi, validIdxForGraph = -1;
				int[] jumlahNode = new int[15];
				int[][] jalurMin = new int[15][15];
				int[] arrBobot = new int[15];
				int[][] posisiNodeGraph = new int[15][2];
				
				//ALGORITMA 
				try {
					// mencari solusi
					for (int i = 0; i < 15; i++) {
						arrBobot[i] = -1;
						posisiNodeGraph[i][0] = -1;
						posisiNodeGraph[i][1] = -1;
						for (int j = 0; j < 15; j++) {
							jalurMin[i][j] = -1;
						}
					}
					  
					long lStartTime = new Date().getTime(); // start time
					
					SolverTSP solTSP = new SolverTSP(rfe);
					jalurMin = solTSP.solvermaster();
					for (int i = 0; i <= solTSP.getnbSolusi(); i++) {
						for (int j = 0; j < 15; j++) {
							if (jalurMin[i][j] == -1) {
								jumlahNode[i] = j;
								break;
							}
						}	
					}
					
					//menghitung total bobot setiap kemungkinan solusi 
					for (int j = 0; j <= solTSP.getnbSolusi(); j++) {
						totalBobot = 0;
						for (int i = 0; i < jumlahNode[j]-1; i++) {
							if (i  < jumlahNode[j]) {
								totalBobot = totalBobot + rfe.getDataMatrix()[jalurMin[j][i]][jalurMin[j][i+1]];
							} else {
								totalBobot = totalBobot + rfe.getDataMatrix()[jalurMin[j][0]][jalurMin[j][i]];
							}
						}
						arrBobot[j] = totalBobot;
					}
					
					//mencari bobot minimum dari semua kemungkinan yang didapat  
					minBobot = arrBobot[0];
					for (int j = 0; j <= solTSP.getnbSolusi(); j++) {
						if (minBobot <= arrBobot[j]) {
							minBobot = minBobot;
						} else {
							minBobot = arrBobot[j];
						}
					}
					
					//menampilkan BOBOT minimum dan jalur2 nya di CMD 
					idxSolusi = 1;
					
					for (int j = 0; j <= solTSP.getnbSolusi(); j++) {
						if (arrBobot[j] == minBobot) {	
							//set validitas solusi 
							if (idxSolusi == 1) {
								validIdxForGraph = j;
							}
							//menampilkan pesan solusi di JAR file 
							c.gridy++;
							c.gridy++;
							printSolusiJAR(idxSolusi, c, cp);
							//menampilkan pesan solusi di CMD 
							printSolusiCMD(idxSolusi);
							for (int i = 0; i < jumlahNode[j]-1; i++) {
								if (i < jumlahNode[j]) {
									//menampilkan di JAR 
									c.gridy++;
									cp.add(new JLabel(Integer.toString(jalurMin[j][i]) + " - " + Integer.toString(jalurMin[j][i+1])), c);
								
									//menampilkan di CMD 
									System.out.println(jalurMin[j][i] + " - " + jalurMin[j][i+1]);
								} 
							}
							//menampilkan di JAR 
							c.gridy++;
							cp.add(new JLabel("Total bobot: " + Integer.toString(arrBobot[j])), c);
							//menampilkan di CMD 
							System.out.println("Total bobot: " + arrBobot[j]);
							idxSolusi++;
						}
					}
					
					//menampilkan gambar GRAF dari matriks dalam input file (mode: 0) 
					posisiNodeGraph = getTheGraph(rfe, 0, posisiNodeGraph, solTSP, validIdxForGraph);
					
					//menampilkan gambar tur terpendek dalam graf (mode: 1)
					posisiNodeGraph = getTheGraph(rfe, 1, posisiNodeGraph, solTSP, validIdxForGraph);
					
					/** Mengakhiri perhitungan waktu eksekusi **/
					long lEndTime = new Date().getTime();        // end time
					long bedaWaktu = lEndTime - lStartTime;      // hitung selisih waktu

					c.gridy = c.gridy + 2;
					cp.add(new JLabel("Waktu eksekusi: " + String.valueOf(bedaWaktu) + " ms"), c);
					c.gridy++;
					cp.add(new JLabel("Jumlah simpul dibangkitkan: " + String.valueOf(solTSP.getnbSimpul())), c);
					System.out.println("Jumlah simpul dibangkitkan: " + solTSP.getnbSimpul());
					System.out.println("Waktu eksekusi: " + bedaWaktu + " ms");  
					
				} catch (Exception f) {
					return;
				}
			}
		});
		//mengatur cara terminasi program 
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//mengatur ukuran frame dari aplikasi 
		setSize(400, 500);
		//menampilkan judul program di bar 
		setTitle("TSP with B&B - 13514100 - AK");
		//mengatur visibilitas aplikasi saat dirun
		setVisible(true);
	} 
	
	// method menggambar GRAPH - return koordinat setiap node 
	public static int[][] getTheGraph(ReadFileEksternal rfe, int mode, int[][] posisiNodeGraph, SolverTSP st, int validIdxForGraph) {
		//KAMUS 
		int[][] nodePosition = new int[30][2];
		int[] isDrawn = new int[30];
		int randX = -1, randY = -1, isSame = -1;
		
		//ALGORITMA 
		
		GraphDraw frame;
		
		if (mode == 0) {
			frame = new GraphDraw("Gambar graf input", validIdxForGraph, mode);
		} else {
			frame = new GraphDraw("Gambar tur terpendek", validIdxForGraph, mode);
		}
		
		//random nilai koordinat 
		Random rand = new Random();
		
		if (mode == 0) {
			randX = rand.nextInt(600) + 50;	 
			randY = rand.nextInt(600) + 50;	
		} else {
			randX = posisiNodeGraph[0][0];
			randY = posisiNodeGraph[0][1];
		}
		
		isSame = 1;
		
		//inisiasi bahwa setiap node belum digambar dan posisi awal setiap node 
		for (int i = 0; i < rfe.getMatrixRow(); i++) {
			isDrawn[i] = 0;
			nodePosition[i][0] = 0;		//absis
			nodePosition[i][1] = 0;		//ordinat 
		}
		
		//menambahkan vertex ke-0  
		if (mode == 0) {
			nodePosition[0][0] = randX;
			nodePosition[0][1] = randY;
		} else {
			nodePosition[0][0] = posisiNodeGraph[0][0];
			nodePosition[0][1] = posisiNodeGraph[0][1];
		}
		
		isDrawn[0] = 1;
		frame.addNode("0", randX, randY);
		
		//menambahkan node selain node ke-0 
		for (int j = 0; j < rfe.getMatrixRow(); j++) {
			if (rfe.getDataMatrix()[0][j] != -1) {
				//bentuk node ke-j 
				isSame = 1;
				
				if (mode == 0) {
					while (isSame == 1) {
						randX = rand.nextInt(600) + 50;
						randY = rand.nextInt(600) + 50;
						isSame = 0;
						//mencari apakah hasil random sudah ada yang punya 
						for (int i = 0; i < rfe.getMatrixRow(); i++) {
							if (isDrawn[i] == 1) {
								if (nodePosition[i][0] == randX && nodePosition[i][1] == randY) {
									isSame = 1;
									break;
								}
							}
						}
					}
				} else {
					randX = posisiNodeGraph[j][0];
					randY = posisiNodeGraph[j][1];
				}
				
				nodePosition[j][0] = randX;
				nodePosition[j][1] = randY;
				isDrawn[j] = 1;
				frame.addNode(String.valueOf(j), randX, randY);
			}
		}
		
		for (int i = 0; i < rfe.getMatrixRow(); i++) {
			for (int j = 0; j < rfe.getMatrixRow(); j++) {
				if (rfe.getDataMatrix()[i][j] != -1) {
					frame.addEdge(i, j, rfe.getDataMatrix()[i][j], st);
				}
			}
		}
		
		return nodePosition;
	}
	
	// viewer 
	public static void print_pembuka() {
		System.out.println("Travelling Salesperson Problem with Branch and Bound Algorithm");
		System.out.println("--------------------------------------------------------------");
	}
	public static void print_goodBye() {
		System.out.println("Good Bye!");
	}
	public static void printSolusiCMD(int idxSolusi) {
		System.out.println("SOLUSI " + idxSolusi);
		System.out.println("---------------");
		System.out.println("Jalur tur terpendek:");
	}
	public static void printSolusiJAR(int idxSolusi, GridBagConstraints pc, Container pcp) {
		pcp.add(new JLabel("SOLUSI" + Integer.toString(idxSolusi)), pc);
		pc.gridy++;
		pcp.add(new JLabel("------------------------------"), pc);
		pc.gridy++;
		pcp.add(new JLabel("Jalur tur terpendek: "), pc);
	}
	
	// getter input user 
	public static int get_UserInput() {
		int u_input;
		Scanner scan = new Scanner(System.in);
		u_input = scan.nextInt();
		return u_input;
	}
	
	// main function
	public static void main(String[] args) throws Exception {
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				try {
					
					//KAMUS
					String path_file;

					//ALGORITMA
						
					// membaca file input 
					path_file = "C:/Users/AlbertusK95/Documents/Semester04/IF2211_Stima/Tucil3/FileInput.txt";
					ReadFileEksternal RFE = new ReadFileEksternal(path_file);
					try {
						RFE.startReadingFile();
					} catch (IOException e) {
						System.err.println("IOException baca file: " + e.getMessage());
					}
					
					new supermain(RFE);
								
				} catch (Exception e) {
					return;
				}
			}
		});
	}
	
}