package net.hydrotekz.HydroCrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PrivateKey;

public class Decryptor {

	private static String decryptionKey = null;
	private static PrivateKey privateKey = null;

	public static void loadModule() throws Exception {
		// Load public key
		decryptionKey = "hidden";
		if(decryptionKey != null){
			privateKey = RSA.loadPrivateKey(decryptionKey);
		}
	}

	// Carefully decrypt block by block
	public static void decryptFile(File file, File to) throws Exception {
		long startTime = System.currentTimeMillis();

		// Prepare streams
		FileOutputStream outputStream = new FileOutputStream(to);
		FileInputStream inputStream = null;
		try {
			// Create a inputstream
			inputStream = new FileInputStream(file);

			// Handle bytes
			int blockSize = 512;
			long blocksNeeded = ((file.length()) / blockSize);

			// Decrypt blocks
			for (int i=0; i <= blocksNeeded-1; i++) {
				// Read a chunk of bytes
				byte[] block = new byte[blockSize];

				inputStream.read(block, 0, blockSize);

				// Write encrypted data
				byte[] decrypted = RSA.decrypt(block, privateKey);

				outputStream.write(decrypted);
			}

			// Decrypt last block
			if (file.length() > blocksNeeded * blockSize){
				// Read last chunk of bytes
				blockSize = (int) (file.length() - (blocksNeeded * blockSize));
				byte[] block = new byte[blockSize];

				inputStream.read(block, 0, blockSize);

				// Write encrypted data
				byte[] decrypted = RSA.decrypt(block, privateKey);

				outputStream.write(decrypted);
			}

			// Debug
			long endTime = System.currentTimeMillis();
			long time = endTime-startTime;
			System.out.println("Decrypted: " + file.getName() + " finished in " + time + " ms.");

		} catch (Exception e) {
			// Ignore error

		} finally {
			// Close stream
			try {
				// Close input stream
				if (inputStream != null) {
					inputStream.close();
				}
				// Close output stream
				outputStream.close();

			} catch (Exception e) {
				// Ignore error
			}
		}
	}
}