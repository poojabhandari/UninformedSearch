import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class waterFlow {

	public static void main(String args[]) {
		long progStartTime = System.currentTimeMillis();
		waterFlow ob = new waterFlow();
		try {
			FileReader fileReader = new FileReader(args[0]);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			FileWriter fw = new FileWriter("output.txt");
			String newLine = System.getProperty("line.separator");

			int noOfCases = Integer.parseInt(bufferedReader.readLine().trim());
			System.out.println(noOfCases);

			String[] cases = new String[noOfCases];

			cases = ob.getAllTestcases(bufferedReader, noOfCases);

			String answer = "";
			int i;
			for (i = 0; i < noOfCases; i++) {
				
				answer = "";
				if (cases[i].startsWith("BFS")) {
					answer = ob.BFS(cases[i]);
					fw.write("" + answer + newLine);
				}
				if (cases[i].startsWith("DFS")) {
					answer = ob.DFS(cases[i]);
					fw.write("" + answer + newLine);
				}
				if (cases[i].startsWith("UCS")) {
					answer = ob.UCS(cases[i]);
					fw.write("" + answer  + newLine);
				}
			}

			fw.close();
			System.out.println("End");
			long endTime = System.currentTimeMillis();
			long timeElapsed = endTime - progStartTime;
			System.out.println("\n\ntimeElapsed: " + timeElapsed);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String[] getAllTestcases(BufferedReader bufferedReader,
			int noOfCases) {

		String s;
		String[] cases = new String[noOfCases];
		int i;
		for (i = 0; i < cases.length; i++)
			cases[i] = "";

		try {
			for (i = 0; i < noOfCases - 1; i++) {

				int intermediate = 0;
				int check = 0;
				int breakint=0;
				while (true) {
					s = bufferedReader.readLine();
					if (check == 0) {
						if (!s.equals("UCS") && !s.equals("BFS")
								&& !s.equals("DFS")) {
							continue;
						}

					}

					check = 1;
					intermediate++;
					cases[i] = cases[i] + "\n" + s;
					if (intermediate==5) 
						breakint=Integer.parseInt(s.trim())+5+1;
					else if(intermediate==breakint)	
						break;

				}
				cases[i] = cases[i].replaceFirst("\n", "");
			}
			int intermediate = 0;
			int check = 0;
			int breakint=0;
			while (true) {
				s = bufferedReader.readLine();
				if (check == 0) {
					if (!s.equals("UCS") && !s.equals("BFS")
							&& !s.equals("DFS")) {
						continue;
					}

				}

				check = 1;
				intermediate++;
				cases[i] = cases[i] + "\n" + s;
				if (intermediate==5) 
					breakint=Integer.parseInt(s.trim())+5+1;
				else if(intermediate==breakint)	
					break;

			}
			cases[i] = cases[i].replaceFirst("\n", "");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return cases;
	}

	ArrayList<ArrayList<String>> getEdges(String[] bfsArray) {
		ArrayList<ArrayList<String>> edges = new ArrayList<ArrayList<String>>();

		for (int i = 5; i < bfsArray.length - 1; i++) {
			String[] edgeArray = bfsArray[i].split(" ");
			ArrayList<String> edge = new ArrayList<String>();

			for (int j = 0; j < edgeArray.length; j++) {
				edge.add(edgeArray[j]);
			}

			edges.add(edge);
		}
		return edges;
	}

	boolean isDestination(String ele, String[] goalNodes) {
		for (int i = 0; i < goalNodes.length; i++) {
			if (ele.equals(goalNodes[i]))
				return true;
		}
		return false;
	}

	String BFS(String BFScase) {

		System.out.println("\n///// \n" + BFScase + "\n//////");

		String[] bfsArray = BFScase.split("\n");
		String startNode = bfsArray[1];
		String[] goalNodes = bfsArray[2].split(" ");
		int startTime = Integer.parseInt(bfsArray[bfsArray.length - 1].trim());

		ArrayList<ArrayList<String>> edges = getEdges(bfsArray);
		Map<String, String> parentsMap = new HashMap<String, String>();
		Map<String, Integer> visited = new HashMap<String, Integer>();
		Queue<String> queue = new LinkedList<String>();
		String ele = null;
		boolean flag = false;

		queue.add(startNode);
		parentsMap.put(startNode, "0");

		while (!queue.isEmpty()) {
			ele = queue.remove();
			visited.put(ele, 1);
			if (isDestination(ele, goalNodes)) {
				flag = true;
				break;
			}

			List<String> adjecentEle = getAdjecentElements(ele, edges);
			Collections.sort(adjecentEle);

			for (int j = 0; j < adjecentEle.size(); j++) {
				if (!visited.containsKey(adjecentEle.get(j))) {
					queue.add(adjecentEle.get(j));
				}

				if (!parentsMap.containsKey(adjecentEle.get(j)))
					parentsMap.put(adjecentEle.get(j), ele);
			}

		}

		System.out.println(parentsMap);

		if (flag == false)
			ele = "NULL";

		String Solution = ele;
		int count = 0;

		if (flag != false) {
			while (!parentsMap.get(ele).equals("0")) {
				count++;
				ele = parentsMap.get(ele);
			}
			count = count + startTime;
		}

		count = count % 24;
		System.out.println("Solution:" + Solution + "  Count:" + count);
		String sol = "";

		if (flag != false)
			sol = "" + Solution + " " + count;
		else
			sol = "None";

		return sol;
	}

	String DFS(String DFSCase) {
		System.out.println("\n///// \n" + DFSCase + "\n//////");

		String[] dfsArray = DFSCase.split("\n");
		int startTime = Integer.parseInt(dfsArray[dfsArray.length - 1].trim());
		System.out.println("StartTime: " + startTime);
		String startNode = dfsArray[1];
		String[] goalNodes = dfsArray[2].split(" ");
		ArrayList<ArrayList<String>> edges = getEdges(dfsArray);
		Map<String, String> parentsMap = new HashMap<String, String>();
		Map<String, Integer> visited = new HashMap<String, Integer>();
		Stack<String> stack = new Stack<String>();
		String ele = null;
		boolean flag = false;

		stack.add(startNode);
		parentsMap.put(startNode, "0");

		while (!stack.isEmpty()) {
			ele = stack.remove(0);
			visited.put(ele, 1);
			if (isDestination(ele, goalNodes)) {
				flag = true;
				break;
			}

			List<String> adjecentEle = getAdjecentElements(ele, edges);
			Collections.sort(adjecentEle);

			int k = 0;
			for (int j = 0; j < adjecentEle.size(); j++) {
				if (!visited.containsKey(adjecentEle.get(j))) {
					stack.add(k++, adjecentEle.get(j));
					parentsMap.put(adjecentEle.get(j), ele);
				}
			}

		}

		System.out.println(parentsMap);

		if (flag == false)
			ele = "NULL";

		String Solution = ele;
		int count = 0;
		String sol = "";
		if (flag != false) {
			while (!parentsMap.get(ele).equals("0")) {
				count++;
				ele = parentsMap.get(ele);
			}
			count = count + startTime;
		}

		count = count % 24;
		System.out.println("DFS :" + Solution + " " + count);

		if (flag != false)
			sol = "" + Solution + " " + count;
		else
			sol = "None";
		return sol;
	}

	
	String UCS(String UCScase) {
		System.out.println("\n/////\n" + UCScase + "\n//////\n");
		String[] ucsArray = UCScase.split("\n");
		int startTime = Integer.parseInt(ucsArray[ucsArray.length - 1].trim());
		System.out.println("StartTime: " + startTime);
		String startNode = ucsArray[1];
		String[] goalNodes = ucsArray[2].split(" ");

		ArrayList<ArrayList<String>> edges = getEdges(ucsArray);

		Map<String, String> parentsMap = new HashMap<String, String>();
		Map<String, Integer> visited = new HashMap<String, Integer>();
		Map<String, Integer> distance = new HashMap<String, Integer>();

		Stack<String> stack = new Stack<String>();

		String ele = null;
		boolean flag = false;

		stack.add(startNode);
		parentsMap.put(startNode, "0");
		distance.put(startNode, startTime);

		while (!stack.isEmpty()) {

			ele = stack.remove(0);
			visited.put(ele, 1);
			if (isDestination(ele, goalNodes)) {
				flag = true;
				break;
			}

			ArrayList<ArrayList<String>> adjecentEle = getAvailable(distance.get(ele),
					ele, edges);

			// sort
			adjecentEle = this.sortByValue(adjecentEle);

			for (int x = 0; x < adjecentEle.size(); x++) {
				String val = adjecentEle.get(x).get(0) + "";
				if (!stack.contains(val) && !visited.containsKey(val)) {
					stack.add(val);
					parentsMap.put(val, ele);
					Integer d = Integer
							.parseInt((adjecentEle.get(x).get(1) + "").trim());
					distance.put(val, d + distance.get(ele));
					stack = sortStack(stack, distance);
					// System.out.println(stack);
				} else {
					Integer d = Integer
							.parseInt((adjecentEle.get(x).get(1) + "").trim());
					if (distance.get(val) > (d + distance.get(ele))) {
						parentsMap.put(val, ele);
						distance.put(val, d + distance.get(ele));
						stack = sortStack(stack, distance);
					}// if
						// System.out.println(stack);
				}// else
			}// for

		}// while stack not empty
		String solution = "";
		if (flag == false)
			solution = "NULL";
		else
			solution = ele;

		System.out.println(parentsMap);
		System.out.println(distance);

		String sol;
		if (flag != false)
			sol = "" + solution + " " + distance.get(solution) % 24;
		else
			sol = "None";

		System.out.println(sol);
		return sol;
	}// ucs

	List<String> getAdjecentElements(String ele,
			ArrayList<ArrayList<String>> edges) {
		List<String> adj = new ArrayList<String>();
		for (int i = 0; i < edges.size(); i++) {
			ArrayList<String> a = edges.get(i);
			if (a.get(0).equals(ele))
				adj.add(a.get(1));

		}

		return adj;
	}

	private ArrayList<ArrayList<String>> getAvailable(int t, String ele,
			ArrayList<ArrayList<String>> edges) {

		t = t % 24;

		ArrayList<ArrayList<String>> adjeleList = new ArrayList<ArrayList<String>>();
		ArrayList<String> eleList;

		for (int i = 0; i < edges.size(); i++) {
			ArrayList<String> a = edges.get(i);
			if (a.get(0).equals(ele)) {
				if (a.get(3).equals("0")) {
					eleList = new ArrayList<String>();
					eleList.add(a.get(1));
					eleList.add(a.get(2).trim());
					adjeleList.add(eleList);
				} else {
					int count = 0;
					for (int j = 4; j < a.size(); j++) {
						int min = Integer.parseInt(a.get(j).substring(0,
								a.get(j).indexOf("-")).trim());
						min = min % 24;
						int max = Integer.parseInt(a.get(j).substring(
								a.get(j).indexOf("-") + 1, a.get(j).length()).trim());
						max = max % 24;
						if (t > max || t < min)
							count++;
					}
					if (count == (a.size() - 4)) {
						eleList = new ArrayList<String>();
						eleList.add(a.get(1));
						eleList.add(a.get(2));
						adjeleList.add(eleList);
					}
				}
			}
		}
		return adjeleList;
	}

	public Stack<String> sortStack(Stack<String> s, Map<String, Integer> distance) {
		int n = s.size();
		int k;
		String temp = "";
		for (int m = n; m >= 0; m--) {
			for (int i = 0; i < n - 1; i++) {
				k = i + 1;
				if (distance.get(s.get(i)) >= distance.get(s.get(k))) {
					if (distance.get(s.get(i)) == distance.get(s.get(k))) {
						String first = (String) s.get(i);
						String second = (String) s.get(k);
						if (first.compareTo(second) > 0) {
							temp = (String) s.get(i);
							s.add(i, s.get(k));
							s.add(k, temp);
							s.remove(i + 1);
							s.remove(k + 1);
						}
					} else {
						temp = (String) s.get(i);
						s.add(i, s.get(k));
						s.add(k, temp);
						s.remove(i + 1);
						s.remove(k + 1);
					}
				}

			}
		}
		return s;
	}

	public ArrayList<ArrayList<String>> sortByValue(ArrayList<ArrayList<String>> unsortArrayList) {

		int n = unsortArrayList.size();
		int k;
		
		ArrayList<String> temp1 = new ArrayList<String>();
		ArrayList<String> temp2 = new ArrayList<String>();

		for (int m = n; m >= 0; m--) {
			for (int i = 0; i < n - 1; i++) {
				k = i + 1;
				Integer n1 = Integer.parseInt((String) ("" + unsortArrayList
						.get(i).get(1)).trim());
				Integer n2 = Integer.parseInt((String) ("" + unsortArrayList
						.get(k).get(1)).trim());
				if (n1 >= n2) {
					if (n1 == n2) {
						String first = (String) unsortArrayList.get(i).get(0);
						String second = (String) unsortArrayList.get(k).get(0);
						if (first.compareTo(second) > 0) {
							temp1 = unsortArrayList.get(i);
							temp2 = unsortArrayList.get(k);

							unsortArrayList.add(i, temp2);
							unsortArrayList.add(k, temp1);
							unsortArrayList.remove(i + 1);
							unsortArrayList.remove(k + 1);

						}
					} else {
						temp1 = unsortArrayList.get(i);
						temp2 = unsortArrayList.get(k);

						unsortArrayList.add(i, temp2);
						unsortArrayList.add(k, temp1);
						unsortArrayList.remove(i + 1);
						unsortArrayList.remove(k + 1);

					}
				}

			}
		}

		return unsortArrayList;
	}

}
