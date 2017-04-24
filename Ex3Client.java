import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
public class Ex3Client {

	public static void main(String[] args) throws IOException {
		try (Socket socket = new Socket("codebank.xyz", 38002)){
			InputStream is = socket.getInputStream();
			
			int numBytes = firstByte(is);
			byte[] sentBytes = getBytes(is, numBytes);
			
		}
			
	}
	public static int firstByte(InputStream is) throws IOException{
		byte firstByte = (byte) is.read(); //0xFF
		int result = (int) firstByte & 0xFF;
		return result;
	}
	public static byte[] getBytes(InputStream is, int size) throws IOException{
		byte[] message = new byte[size];
		for (int i = 0; i < size; ++i){
			message[i] = (byte) is.read();
//			System.out.println(i + ": " + Integer.toBinaryString(message[i] & 0xFF));
		}
		return message;
	}
	public static short checksum(byte[] b){
		short bits = 0;
		for (int i = 0; i < b.length; ++i){
			
		}
		return bits;
	}

}
