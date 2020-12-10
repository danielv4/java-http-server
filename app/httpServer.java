// javac httpServer.java
// jar cvfe main.jar httpServer *.class
// java -jar main.jar
import java.io.*;
import java.net.*;
import java.lang.Thread;
import java.util.*;
import java.text.SimpleDateFormat;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;










public class httpServer {
	
	public static String httpGetServerTime() {
		
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat formatQ = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US); 
		formatQ.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatQ.format(cal.getTime());
	}
	
	
	public static String httpCreateResponseFromString(String contentType, String markup) {
		
		int sizeM = markup.length();
		String lenM = String.valueOf(sizeM);
		
		StringBuilder list = new StringBuilder();
		list.append("HTTP/1.1 200 OK\r\n");
		list.append("Date: " + httpGetServerTime() + "\r\n");
		list.append("Server: JavaWebServer/1.0 \r\n");
		list.append("Last-Modified: " + httpGetServerTime() + "\r\n");
		list.append("Accept-Ranges: bytes \r\n");
		list.append("Content-Length: " + lenM + " \r\n");
		list.append("Connection: close \r\n");
		list.append("Content-Type: " + contentType + " \r\n\r\n");
		list.append(markup + "\r\n");
		String res = list.toString();
		
		return res;
	}
	
	public static String readFile(String filename) {
            File f = new File(filename);
            try {
                byte[] bytes = Files.readAllBytes(f.toPath());
                return new String(bytes,"UTF-8");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
    }
	
	
	public static String httpCreateResponseFromFile(String contentType, String pathM) {
		
		String markup = readFile(pathM);
		int sizeM = markup.length();
		String lenM = String.valueOf(sizeM);
		
		StringBuilder list = new StringBuilder();
		list.append("HTTP/1.1 200 OK\r\n");
		list.append("Date: " + httpGetServerTime() + "\r\n");
		list.append("Server: JavaWebServer/1.0 \r\n");
		list.append("Last-Modified: " + httpGetServerTime() + "\r\n");
		list.append("Accept-Ranges: bytes \r\n");
		list.append("Content-Length: " + lenM + " \r\n");
		list.append("Connection: close \r\n");
		list.append("Content-Type: " + contentType + " \r\n\r\n");
		list.append(markup + "\r\n");
		String res = list.toString();
		
		return res;
	}
	
	
	public static void main(String[] args) throws Exception {

		int port = 8080;
		ServerSocket socketM = new ServerSocket(port);
		System.out.println(">> Java http server listen on " + port);
		
		String clientRes = httpCreateResponseFromString("text/html", "<html><head></head><body><div>Data</div></body></html>");
		// String jsfile = httpCreateResponseFromString("application/javascript", "main.js");
		
		System.out.println(clientRes);

		while (true) {


			// Debug peer
			Socket peer = socketM.accept();
			InetAddress addr = socketM.getInetAddress();
			int peerPort = socketM.getLocalPort();
			String peerPortM = String.valueOf(peerPort);
			System.out.println(">> Peer connected " + addr + ":" + peerPortM);


			// Read & Write socket client
			BufferedReader in = new BufferedReader(new InputStreamReader(peer.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(peer.getOutputStream()));

			// Read
			String s;
			while ((s = in.readLine()) != null) {
				System.out.println(s);
				if (s.isEmpty()) {
					break;
				}
			}

			// Write
			out.write(clientRes);
			
			
			// Close
			System.out.println(">> Peer connection close");
			out.close();
			in.close();
			peer.close();
		}
	}
}











