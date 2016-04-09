package bin;

import java.util.*;

public class SolverTSP {
	
	private int matrixRow;
	private int matrixCol;
	private int nbCity;
	private int nbSolusi;
	private long nbSimpul;
	private int[][] solver_matrix = new int[30][30];
	private int[] isMadeAsNode = new int[30];
	private int[][] friendNode = new int[30][30];
	private int[] prevNode = new int[30];
	private int[] nextNode = new int[30];
	private int[][] isValid = new int[30][30];
	private int[][] minTour = new int[30][30];
	private QueueTSP queue;
	
	//constructor
	public SolverTSP(final ReadFileEksternal rfe) {
		//inisiasi matrix 
		matrixRow = rfe.getMatrixRow();
		matrixCol = rfe.getMatrixCol();
		//inisiasi jumlah kota dan simpul dibangkitkan 
		nbCity = matrixRow;
		nbSimpul = 0;
		//inisiasi nilai matrix dan minTour
		for (int i = 0; i < matrixRow; i++) {
			for (int j = 0; j < matrixCol; j++) {
				solver_matrix[i][j] = rfe.getDataMatrix()[i][j];
				minTour[i][j] = -1;
			}
		}
		//inisiasi status kota sebagai node atau bukan  
		for (int i = 0; i < nbCity; i++) {
			isMadeAsNode[i] = 0;
		}
		//inisiasi nilai array isValid untuk kasus permutasi 
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				if (j < nbCity) {
					isValid[i][j] = 0;
				} else {
					isValid[i][j] = -1;
				}
			}
		}
		//inisiasi nilai awal friendNode
		for (int i = 0; i < matrixRow; i++) {
			prevNode[i] = -1;
			nextNode[i] = -1;
			for (int j = 0; j < matrixCol; j++) {
				friendNode[i][j] = 0;
			}
		}
		//inisiasi banyak solusi 
		nbSolusi = -1;
		//inisisasi queue kosong
		queue = new QueueTSP();
	}
	
	//getter
	public int getnbSolusi() {
		return nbSolusi;
	}
	public int[][] getMinTour() {
		return minTour;
	}
	public int getnbCity() {
		return nbCity;
	}
	public long getnbSimpul() {
		return nbSimpul;
	}
	
	//method mengambil sisi dengan bobot minimum
	public int chooseTwoMinWeight(int cityID, int prevMinID) {
		
		int min = -1;
		int avoidThisIdx = -1;
		
		for (int i = 0; i < nbCity; i++) {
			if (solver_matrix[cityID][i] == prevMinID) {
				avoidThisIdx = i;
				for (int j = 0; j < nbCity; j++) {
					if (j != i && solver_matrix[cityID][j] != -1) {
						min = solver_matrix[cityID][j];
						break;
					}
				}
				break;
			} else {
				if (solver_matrix[cityID][i] != -1) {
					min = solver_matrix[cityID][i];
				}
			}
		}
		
		for (int i = 0; i < nbCity; i++) {
			if (solver_matrix[cityID][i] != -1 && i != avoidThisIdx) {
				if (min <= solver_matrix[cityID][i]) {
					min = min;
				} else {
					min = solver_matrix[cityID][i];
				}
			}
		}
		
		return min;
		
	}
	
	public int findMinimumCostID(int level) {
		
		//KAMUS 
		double min_cost = queue.getarrIndexCost()[0]; 
		int foundIdx = -1;
		
		//ALGORITMA 
		for (int i = 0; i < queue.getQueueSize(); i++) {
			if (queue.getarrIndexCost()[i] != -1) {
				if (min_cost <= queue.getarrIndexCost()[i]) {
					min_cost = min_cost;
				} else {
					min_cost = queue.getarrIndexCost()[i];
				}
			}
		}
		
		for (int i = 0; i < queue.getQueueSize(); i++) {
			if (queue.getarrIndexCost()[i] == min_cost) {
				foundIdx = queue.getarrIndexNode()[i];
				break;
			}
		}
		
		//mencari banyak cost minimum yang sama 
		if (level == 0) {
			isValid[level][0] = 1;
		} else {
			for (int i = 0; i < queue.getQueueSize(); i++) {
				if (queue.getarrIndexCost()[i] != -1) {
					if (queue.getarrIndexCost()[i] == min_cost) {
						isValid[level][queue.getarrIndexNode()[i]] = 1;
					}
				}
			}		
		}
		return foundIdx;
	}
	
	public int[] createChildNodes(int minCostID) {
		
		//KAMUS 
		int[] childNodes = new int[30];
		int idx = 0;
		
		//ALGORITMA
		for (int i = 0; i < 30; i++) {
			childNodes[i] = -1;
		}
		for (int i = 0; i < nbCity; i++) {
			if (i != minCostID) {
				if (isMadeAsNode[i] == 0) {
					nbSimpul++;
					childNodes[idx] = i;
					idx++;
				}
			}
		}
		return childNodes;
	}
	
	public double countCost(int minimumCostID, int ChildNodes) {
		
		//KAMUS
		int[][] minEdge = new int[30][2];
		int[][] jalurWajib = new int[30][2];
		int prev = prevNode[ChildNodes]; 
		int idx = 0;
		int temp;
		int found = 0, foundprev = 0;
		int iterasi = 0;
		double cost = 0;
		
		//ALGORITMA 
	
		//mencari jalur yang wajib dipilih
		temp = ChildNodes;
		System.out.println("prev: " + prev);
		while (prev != -1) {
			foundprev = 1;
			jalurWajib[idx][0] = prev;
			jalurWajib[idx][1] = temp;
			temp = prevNode[temp];
			prev = prevNode[temp];
			idx++;
		}
		
		//menghitung bobot minimum dari 2 sisi setiap kota		
		for (int i = 0; i < nbCity; i++) {	
			if (foundprev == 1) {
				//cek apakah jalur itu termasuk yang wajib 
				iterasi = 0;
				for (int j = 0; j < idx; j++) {
					if (i == jalurWajib[j][0] || i == jalurWajib[j][1]) {
						for (int k = 0; k < nbCity; k++) {
							if (k != i) {
								if (k == jalurWajib[j][1] || k == jalurWajib[j][0]) {
									minEdge[i][iterasi] = solver_matrix[jalurWajib[j][0]][jalurWajib[j][1]];
									iterasi++;
									break;
								}
							}
						}
						
					}
				}
				
				if (iterasi == 0) {//tidak ada jalur wajib 
					minEdge[i][0] = chooseTwoMinWeight(i, -2);
					minEdge[i][1] = chooseTwoMinWeight(i, minEdge[i][0]);
				} else if (iterasi == 1) {//ada 1 jalur wajib 
					minEdge[i][1] = chooseTwoMinWeight(i, minEdge[i][0]);
				} else {//ada 2 jalur wajib //do nothing 
				}
				
			} else {
				minEdge[i][0] = chooseTwoMinWeight(i, -2);
				minEdge[i][1] = chooseTwoMinWeight(i, minEdge[i][0]);
			}
		}
		//menghitung cost simpul
		for (int i = 0; i < nbCity; i++) {
			cost = cost + minEdge[i][0] + minEdge[i][1];
		}
		cost = 0.5 * cost;
		return cost;
	}
	
	public int[][] processQueue() {
		
		//KAMUS 
		int minimumCostID;
		int sizeChildNodes = 0;
		int[] arrChildNodes = new int[30];
		int[] arrJalurMin = new int[30];
		int[][] arrCollectionJalurMin = new int[30][30];
		double[] arrCostChildNodes = new double[30];
		int level = 0;
		
		//ALGORITMA 
		for (int i = 0; i < 30; i++) {
			arrJalurMin[i] = -1;
			for (int j = 0; j < 30; j++) {
				arrCollectionJalurMin[i][j] = -1;
			}
		}
		int backtrack = 0;		//variabel penanda backtrack 
		int foundValid = 0;		//variabel penanda menemukan kemungkinan nilai lain 
		while (level < nbCity) {
			//mencari indeks dari nilai cost minimum dalam queue 
			if (backtrack == 0) {
				minimumCostID = findMinimumCostID(level);
			}		
			backtrack = 0;
			foundValid = 0;
			for (int x = 0; x < nbCity; x++) {
				if (isValid[level][x] == 1) {
					isValid[level][x] = 0;
					foundValid = 1;
					minimumCostID = x;
					arrJalurMin[level] = minimumCostID;
					//ubah nilai isMadeAsNode untuk kota yang memiliki nilai cost sebesar minimumCost
					isMadeAsNode[minimumCostID] = 1;
					//create child nodes untuk simpul tersebut
					for (int i = 0; i < 30; i++) {
						arrChildNodes[i] = -1;
					}
					arrChildNodes = createChildNodes(minimumCostID);
					//hapus kota dengan indeks minimumCostID dari queue 
					queue.pop(minimumCostID);
					queue.printIsiQueue();
					//menghitung banyaknya elemen child nodes 
					if (level != 0) {
						prevNode[minimumCostID] = arrJalurMin[level-1];
					}
					sizeChildNodes = 0;
					for (int i = 0; i < 30; i++) {
						if (arrChildNodes[i] != -1) {
							sizeChildNodes++;
							prevNode[arrChildNodes[i]] = minimumCostID;
						}
					}
					//hitung cost untuk setiap child nodes
					for (int i = 0; i < sizeChildNodes; i++) {
						arrCostChildNodes[i] = countCost(minimumCostID, arrChildNodes[i]);
					}
					//masukkan nodes hasil child nodes yang sudah dihitung cost nya ke dalam queue 
					for (int i = 0; i < sizeChildNodes; i++) {
						queue.push(arrChildNodes[i], arrCostChildNodes[i]);
					}
					queue.printIsiQueue();
					break;
				}
			}
			if (foundValid == 0) {
				isMadeAsNode[arrJalurMin[level]] = 0;
				level--;
				isMadeAsNode[arrJalurMin[level]] = 0;
				backtrack = 1;
				if (level <= 0) {
					break;
				}
			} else {
				level++;
				if (level == nbCity) {
					nbSolusi++;
					arrJalurMin[nbCity] = 0;
					for (int i = 0; i <= nbCity; i++) {
						arrCollectionJalurMin[nbSolusi][i] = arrJalurMin[i];
					}
					level--;
					isMadeAsNode[arrJalurMin[level]] = 0;
					backtrack = 1;
					level--;
					isMadeAsNode[arrJalurMin[level]] = 0;
				}
			}
			
		}
		return arrCollectionJalurMin;
	}
	
	//method solving utama yang akan menginvoke method solving lainnya
	public int[][] solvermaster() {
		
		//KAMUS
		int[][] jalurMin = new int[30][30];
		int[][] minEdge = new int[30][2];
		double cost = 0;
		
		//ALGORITMA
		//inisiasi jalurMin 
		for (int i = 0; i < 30; i++) {
			for (int j = 0; j < 30; j++) {
				jalurMin[i][j] = -1;
			}
		}
		//menghitung bobot minimum dari 2 sisi setiap kota 
		for (int i = 0; i < nbCity; i++) {
			minEdge[i][0] = chooseTwoMinWeight(i, -2);
			minEdge[i][1] = chooseTwoMinWeight(i, minEdge[i][0]);
		}
		//menghitung cost simpul ke-1
		for (int i = 0; i < nbCity; i++) {
			cost = cost + minEdge[i][0] + minEdge[i][1];
		}
		cost = 0.5 * cost;
		//memasukkan cost simpul ke-1 ke dalam queue 
		nbSimpul++;
		queue.push(0, cost);
		queue.printIsiQueue();
		//inisiasi kota ke-0 sebagai node 
		isMadeAsNode[0] = 1;
		//membentuk search tree dan memproses isi queue 
		jalurMin = processQueue();
		minTour = jalurMin;
		return jalurMin;
	}
}