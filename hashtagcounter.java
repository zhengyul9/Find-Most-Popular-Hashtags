import java.io.*;
import java.util.*;
import java.util.Scanner;

public class hashtagcounter{

    public static void main(String[] args) throws FileNotFoundException{
        if(args.length > 2){
            throw new ArrayIndexOutOfBoundsException("Too many arguments in the command line. Input only one argument indicates the file name which contains hashtags");
        }
		if(args.length == 2){
			File file = new File(args[0]);
			File output = new File(args[1]);
			try{
				// initialization hashmap, heap, read and write file tools
				output.createNewFile();
				Scanner sc = new Scanner(file);
				BufferedWriter writeFile = new BufferedWriter(new FileWriter(output));
				MaxFibonacciHeap fibonacciHeap = new MaxFibonacciHeap();
				HashMap<String, NodeStructure> hashMap = new HashMap<>();
				//keep reading file till the end of the file
				while(sc.hasNextLine()){
					String str = sc.nextLine();
					if(str.charAt(0) == '#'){ 
			            // if input line contains a hashtag, update hash map and heap info
						String[] split = str.split("\\s+");
						split[0] = split[0].substring(1, split[0].length());
						if(hashMap.containsKey(split[0]))// node exists, increase key
							fibonacciHeap.increaseKey(hashMap.get(split[0]),Integer.parseInt(split[1]));
						else{// node is not exist, create node and insert it to heap
							NodeStructure node = new NodeStructure(split[0],Integer.parseInt(split[1]));
							hashMap.put(split[0], node);
							fibonacciHeap.insert(node);
						}
					}
					else if(str.equals("stop")){// stop
						break;
					}
					else{//Query. Retrieve info, get max node first, record it in the result, remove it in order to get the second large node and so on. Finally insert nodes back
						int query = Integer.parseInt(str.trim());
						NodeStructure[] array = new NodeStructure[query];
						String result = "";
						for(int i = 0; i < query; i++) {
							NodeStructure thisNode = new NodeStructure(fibonacciHeap.maxNode.key,fibonacciHeap.maxNode.val);
							array[i] = thisNode;
							hashMap.put(thisNode.key,thisNode);
							result = result.concat(thisNode.key);
							if(i < query - 1){
								result = result.concat(",");
							}
							fibonacciHeap.deleteMax();
						}
						writeFile.write(result);
						writeFile.write("\n");
						writeFile.flush();
						// array used to store removed max nodes, insert nodes to heap after finishing queries
						for(int i = 0; i < array.length; i++){
							fibonacciHeap.insert(array[i]);
						}
					}
				}
			}catch(IOException e){
            e.printStackTrace();
			}finally{
			}
		}
		else if(args.length == 1){// output to console
			File file = new File(args[0]);
			try{
				// initialization hashmap, heap, read file tool
				Scanner sc = new Scanner(file);
				MaxFibonacciHeap fibonacciHeap = new MaxFibonacciHeap();
				HashMap<String, NodeStructure> hashMap = new HashMap<>();
				//keep reading file till the end of the file
				while(sc.hasNextLine()){
					String str = sc.nextLine();
					if(str.charAt(0) == '#'){ 
			            // if input line contains a hashtag, update hash map and heap info
						String[] split = str.split("\\s+");
						split[0] = split[0].substring(1, split[0].length());
						if(hashMap.containsKey(split[0]))
							fibonacciHeap.increaseKey(hashMap.get(split[0]),Integer.parseInt(split[1]));
						else{
							NodeStructure node = new NodeStructure(split[0],Integer.parseInt(split[1]));
							hashMap.put(split[0], node);
							fibonacciHeap.insert(node);
						}
					}
					else if(str.equals("stop")){// stop
						break;
					}
					else{//Query. Retrieve info, get max node first, record it in the result, remove it in order to get the second large node and so on. Finally insert nodes back
						int query = Integer.parseInt(str.trim());
						NodeStructure[] array = new NodeStructure[query];
						String result = "";
						for(int i = 0; i < query; i++) {
							NodeStructure thisNode = new NodeStructure(fibonacciHeap.maxNode.key,fibonacciHeap.maxNode.val);
							array[i] = thisNode;
							hashMap.put(thisNode.key,thisNode);
							result = result.concat(thisNode.key);
							if(i < query - 1){
								result = result.concat(",");
							}
							fibonacciHeap.deleteMax();
						}
						System.out.println(result);
						// array used to store removed max nodes, insert them to heap after getting all max nodes
						for(int i = 0; i < array.length; i++){
							fibonacciHeap.insert(array[i]);
						}
					}
				}
			}catch(IOException e){
            e.printStackTrace();
			}finally{
			}
		}
		else{
			System.out.println("Please add input file name in the command line!");
		}
    }
}
