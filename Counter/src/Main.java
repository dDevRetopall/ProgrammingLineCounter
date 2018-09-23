import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static int totalLines = 0;
	public static int totalClasses = 0;
	public static int emptyLines = 0;
	public static int totalCodeSize = 0;
	public static  HashMap<String, Integer>lines = new HashMap<>();
	public static  HashMap<String, Integer>classes = new HashMap<>();
	public static void main(String[] args) {
		read();

	}

	public static void read() {

		totalLines = 0;
		long initialTime = System.currentTimeMillis();

		System.out.print("Project path: ");
		Scanner sc = new Scanner(System.in);
		String path = sc.next();
		System.out.println("Extensions of files to detect. Separate with commas (.java,.cpp,.py)");

		System.out.print(">>  ");
		String line = sc.next();
		String[] extensions = getExtensions(line);
		System.out.println("Executing...");
		for(String s:extensions) {
			lines.put(s, 0);
			classes.put(s, 0);
		}
		File projectFolder = new File(path);
		listFilesForFolder(projectFolder, extensions);
		sc.close();
		System.out.println("Finished...");
		System.out.println("###############");
		System.out.println("Total lines: " + totalLines + " lines");
		System.out.println("Total used lines: " + (totalLines - emptyLines) + " lines");
		System.out.println("Total classes: " + totalClasses + " classes");
		System.out.println("Total bytes: " + totalCodeSize + " bytes");
		System.out.println("---------------");
		printHashMap();
		System.out.println("###############");
		double execution = (int) (((System.currentTimeMillis() - initialTime) / 1000f) * 1000) / 1000d;
		System.out.println("Execution time: " + execution + "s");
	}

	public static String[] getExtensions(String lines) {

		return lines.trim().replace(" ", "").split(",");
	}

	public static void listFilesForFolder(File folder, String[] fileExtension) {
		try {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
					listFilesForFolder(fileEntry, fileExtension);
				} else {
					
					for (int i=0;i<fileExtension.length;i++) {
						if (fileEntry.getName().endsWith(fileExtension[i])) {
							readFile(fileEntry, false,fileExtension[i]);
						}
					}
					
				}
			}
		} catch (Exception e) {
			System.out.println("Workspace folder doesn't exist");
		}
	}

	public static void readFile(File f, boolean printData,String fileExtension) {
		FileReader fr;
		int counter = 0;
		try {
			fr = new FileReader(f);

			BufferedReader br = new BufferedReader(fr);

			String line = "";
			while ((line = br.readLine()) != null) {
				counter++;
				if (printData) {
					System.out.println(line);
				}
				if (line.equals("") || line == null || line.isEmpty()) {
					emptyLines++;
				}
			}
			br.close();
			fr.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		} catch (IOException e) {
			e.printStackTrace();
		}
		totalClasses++;
		totalLines += counter;
		long length = f.length();
		totalCodeSize += length;
	
		int classAdd=classes.get(fileExtension);
		int linesAdd= lines.get(fileExtension)+counter;
		classAdd++;
		
		lines.replace(fileExtension,linesAdd);
		classes.replace(fileExtension, classAdd);

		System.out.println(f.getName() + "   " + counter + " lines" + "   " + length + " bytes");
	}
	public static void printHashMap() {
		ArrayList<String>keys = new ArrayList<>(lines.keySet());
		for(int i=0;i<lines.size();i++) {
			String key=keys.get(i);
			int numClasses=classes.get(key);
			int numLines=lines.get(key);
			System.out.println(key+" > "+ numClasses+" classes "+numLines+" lines");
		}
	}
	public static String getExtension(File f) {
		String fileName=f.getName();
		String extension = "";

		int i = fileName.lastIndexOf('.');
		if (i > 0) {
		    extension = fileName.substring(i+1);
		}
		return extension;
	}
}
