package org.recommendationenginer.auxillary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SampleDataSet {

	public static void main(String args[]) {
		try {
			Map<String, Integer> userCount = getUsers("C:\\Users\\nkumar\\Master-Thesis\\data\\training_set\\training_set");
			System.out.println(userCount.size());
			Set<String> actualUsers = new HashSet<String>();
			int counter=0;
			for(Map.Entry<String, Integer> user: userCount.entrySet()) {				
				counter++;
				if(counter % 3 != 0) {
					actualUsers.add(user.getKey());
				}
			}
			writeSampledFiles("C:\\Users\\nkumar\\Master-Thesis\\data\\training_set\\training_set", "C:\\Users\\nkumar\\Master-Thesis\\sampled-data\\training_set", actualUsers);
		} catch (Exception ex) {

		}
	}
	public static void writeSampledFiles(String srcpath, String path, Set<String> users) {
		File file = new File(srcpath);
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File curFile:files) {
				System.out.println(curFile.getName());				
				BufferedReader br = null;
				BufferedWriter bw = null;
			    try {
			    	br= new BufferedReader(new FileReader(curFile));
			    	bw = new BufferedWriter(new FileWriter(path+"\\"+curFile.getName()));
			        String line = br.readLine();
			        while (line != null) {
			        	String tokens[] = line.trim().split(",");
			        	if(tokens.length == 3) {
			        		if(users.contains(tokens[0].trim())) {
			        			bw.write(line);
			        			bw.newLine();
			        		}
			        	} else {
			        		bw.write(line);
			        		bw.newLine();
			        	}			        	
			        	line = br.readLine();			        	
			        }
			    } catch(Exception ex) {
			    	System.out.println(ex);
			    } finally {
			        try {
						br.close();
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
		}		
	}


	public static Map<String, Integer> getUsers(String path) {
		Map<String, Integer> users = new HashMap<String, Integer>();
		ValueComparator bvc =  new ValueComparator(users);
        TreeMap<String, Integer> sorted_users = new TreeMap<String, Integer>(bvc);
		File file = new File(path);
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File curFile:files) {
				System.out.println(curFile.getAbsolutePath());
				BufferedReader br = null;
			    try {
			    	br= new BufferedReader(new FileReader(curFile));
			        String line = br.readLine();
			        while (line != null) {
			        	String tokens[] = line.trim().split(",");
			        	if(tokens.length == 3) {
			        		Integer counter = users.get(tokens[0].trim()); 
			        		if(users.get(tokens[0]) == null) {
			        			users.put(tokens[0].trim(), 1);
			        		} else {
			        			users.put(tokens[0].trim(), counter + 1);
			        		}
			        	}
			        	line = br.readLine();
			        }
			    } catch(Exception ex) {
			    	System.out.println(ex);
			    } finally {
			        try {
						br.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			    }
			}
		}
		
		sorted_users.putAll(users);
		
		return sorted_users;
	}
}
class ValueComparator implements Comparator<String> {

    Map<String, Integer> base;
    public ValueComparator(Map<String, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return 1;
        } else {
            return -1;
        } // returning 0 would merge keys
    }
}
