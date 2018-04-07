/**
 * Author: Colin Koo
 * Professor: Nima Davarpanah
 * Description: Reads a random amount of bytes based on the initial byte read, then creates the corresponding checksum.
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
public class Ex3Client {

	public static void main(String[] args) {
		
		try (Socket socket = new Socket("codebank.xyz", 38103)){
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			
			int numBytes = firstByte(is);
			byte[] sentBytes = getBytes(is, numBytes);
			short cksum = checksum(sentBytes);
			for (int j = 8; j >= 0; j-=8){
				os.write((byte)(cksum >> j));
			}
			if (is.read() == 1){
				System.out.println("Response good.");
			} else{
				System.out.println("Response bad.");
			}
		} catch (IOException e){
			System.out.println("Erorr! " + e.getMessage());
		}
	}
	/**
	 * This method returns the first byte read from the server which represents the amount of bytes to be sent.
	 * @param is
	 * @return amount of bytes to be read.
	 * @throws IOException
	 */
	public static int firstByte(InputStream is) throws IOException{
		byte firstByte = (byte) is.read(); //0xFF
		int result = (int) firstByte & 0xFF;
		System.out.println("Reading " + result + "bytes.");
		return result;
	}
	/**
	 * This method takes in the InputStream and size parameters that represent the server and the amount of bytes
	 * to be read, the reads those bytes and stores them into a byte array and returns it.
	 * @param is
	 * @param size
	 * @return byte array of bytes read from the server.
	 * @throws IOException
	 */
	public static byte[] getBytes(InputStream is, int size) throws IOException{
		byte[] message = new byte[size];
		System.out.print("Data received: ");
		for (int i = 0; i < size; ++i){
			message[i] = (byte) is.read();
			System.out.print(Integer.toHexString(message[i] & 0xFF).toUpperCase());
		}
		System.out.println();
		return message;
	}
	/**
	 * This method concatenates every two bytes into a long to represent an unsigned short, then adds those "shorts"
	 * to a sum, and checks for overflow- if so, it clears the overflow and adds 1 to the sum.  Then the method returns
	 * the ones' complement of the sum.
	 * @param b
	 * @return ones' complement of the sum
	 */
	public static short checksum(byte[] b){
		long concat = 0x0;
		long sum = 0x0;
		for (int i = 0; i < b.length; i+=2){
			concat = (long) (b[i] & 0xFF);
			concat <<= 8;
			
			if ((i+1) < b.length){
				concat |= (b[i+1] & 0xFF);
			}
			sum = sum + concat;
			if (sum > 0xFFFF){
				sum &= 0xFFFF;
				sum ++;
			}
		}
		short checksum = (short) (~sum);
		System.out.println("Checksum calculated: 0x" + Integer.toHexString(checksum & 0xFFFF).toUpperCase());
		return (short) (~sum);
	}
}