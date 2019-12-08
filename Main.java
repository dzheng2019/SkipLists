import java.util.Scanner;
import java.io.*; 
import java.util.ArrayList;

class Main {
  public static void main(String[] args) {
  	Scanner scan = new Scanner(System.in); 
		boolean run = true;
		//new int []{25,15,40,10,18,35,45,5,19,20,44,49}
		SkipList s = new SkipList(new int []{25,15,40,10,18,35,45,5,19,20,44,49});
		Pointer start = s.skipl.get(s.skipl.size()-1);
		System.out.print(s);
    while(run){
			String l = scan.nextLine();
			//int l_i = Integer.parseInt(l);
			if (l.equals("in")){
				String i = scan.nextLine();
				int i_i = Integer.parseInt(i);
				s.insert(i_i);
				start = s.skipl.get(s.skipl.size()-1);
				System.out.print(s);
				System.out.println("Inserted");

			}
			else if (l.equals("r")){
				if (start.right != null)
					start = start.right;
				else 
					System.out.println("END-OF-LEVEL");
			}
			else if (l.equals("d")){
				if (start.down != null)
					start = start.down;
				else 
					System.out.println("LOWEST-LEVEL");
			}
			else if (l.equals("s")){
				start = s.skipl.get(s.skipl.size()-1);
				System.out.println("START");
			}
			else if (l.equals("f")){
				String i = scan.nextLine();
				int i_i = Integer.parseInt(i);
				if (s.search(i_i) != null){
					start = s.search(i_i);
					System.out.println("Found");
				} else {
					System.out.println("Not Found");
				}
			}
			else if (l.equals("rem")){
				String i = scan.nextLine();
				int i_i = Integer.parseInt(i);
				if (s.search(i_i) != null){
					System.out.println("Deleted");
					s.delete(i_i);
					System.out.println(s);
				} else {
					System.out.println("Not Found");
				}
				System.out.println("DONE");
			}
			else if (l.equals("show")){
				System.out.println(s);
			}
			System.out.println(start);
		}
		scan.close();
  }
}

class SkipList{
	public ArrayList<Pointer> skipl = new ArrayList<Pointer> ();

	SkipList(){
		Pointer header = new Pointer(Integer.MIN_VALUE, null);
		Pointer tail = new Pointer(Integer.MAX_VALUE, null);
		header.right = tail;
		skipl.add(header);

		addlevel();
	}

	SkipList(int[] i){
		Pointer header = new Pointer(Integer.MIN_VALUE, null);
		Pointer tail = new Pointer(Integer.MAX_VALUE, null);
		header.right = tail;
		skipl.add(header);

		addlevel();
		for (int x:	i){
			insert(x);
		}

	}
	public void insert(int v){
		Pointer start = skipl.get(skipl.size()-1);
		ArrayList<Pointer> visited = new ArrayList<Pointer>();
		if (search(v) != null)
			return;
		while (start != null && v > start.val){
			if(start.right!=null){
				if (start.right.val > v){
					if (start.down == null){
						visited.add(0, start);
						createPointer(v, visited);
						return;
					} else {
						visited.add(0, start);
						start = start.down;
					}
				} else {
					start = start.right;
				}
			}
		}
	}
	public Pointer search (int v){
		Pointer start = skipl.get(skipl.size()-1);
		while (start != null && v >= start.val){
			if (start.val == v)
				return start;
			if(start.right!=null){
				if (start.right.val > v){
					if (start.down == null){
						start = start.right;
					} else {
						start = start.down;
					}
				} else {
					start = start.right;
				}
			}
		}
		return null;
	}

	public Pointer delete (int v){
		if (search(v)==null){
			return null;
		}
		Pointer del = search(v);
		Pointer start = skipl.get(skipl.size()-1);
		ArrayList<Pointer> visited = new ArrayList<Pointer>();
		while (start != null && v >= start.val){
			if(start.right !=null){
				if (start.right.val >= v){
					if (start.down == null){
						visited.add(0, start);
						removePointer(visited,del);
						return del;
					} else {
						visited.add(0, start);
						start = start.down;
					}
				} else {
					start = start.right;
				}
			}
		}
		return del;
	}

	private void removePointer(ArrayList<Pointer> prevs, Pointer del){
		int l = 0;
		Pointer d = del;
		Pointer t = del;
		while(d.down != null){
			l++;
			d = d.down;
		}
		for (int i = l; i >= 0; i--){
			prevs.get(i).right = t.right;
			t = t.down;
		}
	}

	private void createPointer(int v, ArrayList<Pointer> visited){
		int l = 0;
		Pointer b = new Pointer(visited.get(l),v);
		l++;
		while (Math.random() < .5){
			if (l >= skipl.size()){
				addlevel();
			}
			if (l >= visited.size()){
				b = new Pointer(skipl.get(l), v, b);
				l++;
			} else {
				b = new Pointer(visited.get(l),v, b);
				l++;
			}
		}
		if (l >= skipl.size()){
			addlevel();
		}
	}

	private void addlevel (){
		Pointer header = new Pointer(Integer.MIN_VALUE, null);
		Pointer tail = new Pointer(Integer.MAX_VALUE, null);
		header.right = tail;
		header.down = skipl.get(skipl.size()-1);
		skipl.add(header);

	}

	public String toString(){
		String str = "";
		for (int i = skipl.size()-1 ; i >= 0; i--){
			str = str + "Level[ " + i + " ] " + levelString (skipl.get(i)) + " \n";
		}
		return str;
	}

	private String levelString (Pointer p){
		String str = "";
		while (p != null){
			str = str + p + ", ";
			p = p.right;
		}
		return str;
	}
}

class Pointer{
	public Pointer down = null;
	public Pointer right = null;
	public int val = 0;

	Pointer (Pointer prev, int v){
		right = prev.right;
		prev.right = this;
		val = v;
	}

	Pointer (Pointer prev, int v, Pointer d){
		down = d;
		right = prev.right;
		prev.right = this;
		val = v;
	}

	Pointer (int v, Pointer r){
		right = r;
		val = v;
	}
	public String toString(){
		if (val == Integer.MIN_VALUE){
			return "Start";
		}
		if (val == Integer.MAX_VALUE){
			return "End";
		}
		return "" + val;
	}
}

	
