package com.cnevinc.photo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;


public class PhotoMan {
	static String mFileRootPath = "F:\\picture\\POPO" ; 
	// Create a dictionary to store all the file name (for move purpose) 
	// and creation date( for folder name )
	static  HashMap<String, ArrayList<String> >  mDictionary = new HashMap<String, ArrayList<String> >();  
	static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	public static void main(String[] args) {
		
		System.out.println("Please enter a file path:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			mFileRootPath = br.readLine();
			if (mFileRootPath.equals("")) {System.out.println("The file path you enter is empty") ; return;} 
		} catch (IOException e1) {
			System.out.println("Your input is envalid." +e1 );
		}
		
		// Start the program with root folder path given
		PhotoMan p = new PhotoMan();
		// Initial Dictionary
		initDictionary(mFileRootPath);
		
		// Iterate Dictionary , Create Folder and move files in
		try {
			createFolderAndMoveFiles(mDictionary);
		} catch (IOException e) {
			System.out.println("Error occurs , please contact cnevinc@livemail.tw  with Error Message[" + e.toString() + "]");
		}
		
		// Pause for confirmation
		System.out.println("Move completed! Please press enter to terminate the program");
		Scanner pauser = new Scanner (System.in);
		pauser.nextLine();
		
	}
	
	private static void createFolderAndMoveFiles(
			HashMap<String, ArrayList<String>> dic) throws IOException {
		// Iterate the dictionary, get folder name( creation date)
		for (String folderName : dic.keySet()){
			// Create folder 
			File folder = new File(mFileRootPath + File.separator + folderName);
			folder.mkdir();
				
			// Iterate files that should be in the folder and Move!
			ArrayList<String> files = dic.get(folderName) ;
			for (String file : files){
				File moving = new File(file);
				moving.renameTo(new File(folder.getAbsolutePath()+File.separator+moving.getName()));
				
				System.out.println("Moving File to : " + moving.getAbsolutePath());
			}
		}
		
	}

	private static int initDictionary(String filePath){
		
		// Iterate all the files in the folder (including sub-folder)
		// an retrieve the file/folder name in the given data structure: dictionary 
		File[] files = new File(filePath).listFiles();
		
		for (File f : files){
			String fileToAdd = f.getAbsolutePath();
			
			// skip thumbnails
//			if (fileToAdd.contains(".thumbnails")) continue;
			
			if (f.isDirectory())
				initDictionary(fileToAdd);
			else{
				String fileCreationDate = sdf.format(new Date(f.lastModified())); 
				if(mDictionary.get(fileCreationDate)==null){
					ArrayList<String> newArray = new ArrayList<String>();
					newArray.add(fileToAdd);
					mDictionary.put(fileCreationDate, newArray ) ;
				}else{
					mDictionary.get(fileCreationDate).add(fileToAdd);
//					System.out.println("Add File" + fileToAdd + " at "+ fileCreationDate );
				}
				
			}
				
				
		}
		
		
		return 1;
	}
	

	
}



