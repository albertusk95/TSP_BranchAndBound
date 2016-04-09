package bin;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GraphDraw extends JFrame {
	//KAMUS
    int width;
    int height;
    int validIdxSolusi;
	int isNormalGraph;
	ArrayList<Node> nodes;
    ArrayList<edge> edges;
	
	//ALGORITMA
    public GraphDraw(String name, int _validIdxSolusi, int _isNormalGraph) { // konstruktor
		Container cpp = getContentPane();
		cpp.setBackground(Color.WHITE);
		setVisible(true);
		setSize(700, 700);
		setTitle(name);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		nodes = new ArrayList<Node>();
		edges = new ArrayList<edge>();
		width = 30;
		height = 30;
		validIdxSolusi = _validIdxSolusi;
		isNormalGraph = _isNormalGraph;
	}

    class Node {
		int x, y;
		String name;
		public Node(String myName, int myX, int myY) {
			x = myX;
			y = myY;
			name = myName;
		}
    }
    
    class edge {
		int i,j, bobot;
		SolverTSP ST;
		public edge(int ii, int jj, int bb, SolverTSP st) {
			i = ii;
			j = jj;	    
			bobot = bb;
			ST = st;
		}
    }
    
    public void addNode(String name, int x, int y) { //menambahkan node di titik (x, y)
		nodes.add(new Node(name,x,y));
		this.repaint();
    }
    public void addEdge(int i, int j, int b, SolverTSP st) { //menambahkan sisi 
		edges.add(new edge(i,j,b, st));
		this.repaint();
	}
    
    public void paint(Graphics g) { // menggambar sisi, node, dan bobot 
		FontMetrics f = g.getFontMetrics();
		int nodeHeight = Math.max(height, f.getHeight());
		g.drawString("Menampilkan solusi ke: " + Integer.toString(validIdxSolusi),  100, 100);				
		g.setColor(Color.black);
		for (edge e : edges) {	// menggambar garis dan bobot 
			g.setColor(Color.black);
			if (isNormalGraph == 1) {
				for (int k = 0; k < e.ST.getnbCity(); k++) {
					if (e.i == e.ST.getMinTour()[validIdxSolusi][k]) {
						if (k  < e.ST.getnbCity()) {
							if (e.j == e.ST.getMinTour()[validIdxSolusi][k+1]) {
								g.setColor(Color.red);
								break;
							} 
						} 
						if (k - 1 >= 0) {
							if (e.j == e.ST.getMinTour()[validIdxSolusi][k-1]) {
								g.setColor(Color.red);
								break;
							} 
						}
					}
				}
			}
			g.drawLine(nodes.get(e.i).x, nodes.get(e.i).y, nodes.get(e.j).x, nodes.get(e.j).y);
			g.drawString(Integer.toString(e.bobot) , (nodes.get(e.i).x + nodes.get(e.j).x) / 2, (nodes.get(e.i).y + nodes.get(e.j).y) / 2);				
		}
		
		for (Node n : nodes) { // menggambar node dan isi di dalamnya
			int nodeWidth = Math.max(width, f.stringWidth(n.name)+width/2);
			g.setColor(Color.white);
			g.fillOval(n.x-nodeWidth/2, n.y-nodeHeight/2, nodeWidth, nodeHeight);
			g.setColor(Color.black);
			g.drawOval(n.x-nodeWidth/2, n.y-nodeHeight/2, nodeWidth, nodeHeight);
			g.drawString(n.name, n.x-f.stringWidth(n.name)/2, n.y+f.getHeight()/2);
		}
    }
}
