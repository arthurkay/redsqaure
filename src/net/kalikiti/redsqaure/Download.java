package net.kalikiti.redsqaure;

import java.util.Observable;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class Download extends Observable implements Runnable {
	
	//Status codes 
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
	public static final int COMPLETE = 2;
	public static final int CANCELLED = 3;
	public static final int ERROR = 4;
	
	//Maximum buffer size 
	public static final int MAX_BUFFER_SIZE = 1024;
	
	//Operation status names
	public static final String STATUSES[] = {
			"Downloading",
			"Paused",
			"Complete",
			"Cancelled",
			"Error"
	};
	
	//Table columns 
	private URL url;
	private int downloaded;
	private int size;
	private int status;
	
	//Constructor method to start the download
	public Download(URL url) {
		
		
		//Initialise the download attributes
		this.url = url;
		size = -1;
		downloaded = 0;
		status = DOWNLOADING;
		
		//Begin the download now
		download();
	}
	
	public String getUrl() {
		return url.toString();
	}
	
	public int getSize() {
		return size;
	}
	
	public int getStatus() {
		return status;
	}
	
	public float getProgress() {
		return ((float)downloaded / size) *100;
	}
	
	public void pause() {
		status = PAUSED;
		stateChanged();
	}
	
	public void resume() {
		status = DOWNLOADING;
		stateChanged();
		download();
	}
	
	public void cancel() {
		status = CANCELLED;
		stateChanged();
	}
	
	public void error() {
		status = ERROR;
		stateChanged();
	}
	
	private void download() {
		Thread thread = new Thread(this);
		thread.start();
	}
	
	private String getUrlname(URL url) {
		String fileName = url.getFile();
		return fileName.substring(fileName.lastIndexOf('/')+1);
	}
	
	public void run() {
		RandomAccessFile file = null;
		InputStream stream = null;
		
		try {
			//Open A connection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			System.out.println("Connecting to "+url);
			//Portion of file to download
			connection.setRequestProperty("Range", "Bytes=" + downloaded + "-");
			
			//Connect to server
			connection.connect();
			if (connection.getResponseCode() != 200) {
				error();
				System.out.println("Unable to connect, server responded with "+ connection.getResponseMessage() +" \n Response code of: "+connection.getResponseCode());
			}
			
			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				error();
				System.out.println("File content length error, cannot be less than 1 \n Response: "+connection.getContentLength());
			}
			
			//Setting the size of the download file, if not already set.
			if (size == -1) {
				size = contentLength;
				stateChanged();
			}
			
			file = new RandomAccessFile(getUrlname(url), "rw");
			file.seek(downloaded);
			
			stream = connection.getInputStream();
			while (status == DOWNLOADING) {
				//Buffer according to how much of the file is left to download
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				}
				else {
					buffer = new byte[size - downloaded];
				}
				
				//Reading into the buffer space
				
				int read = stream.read(buffer);
				
				if (read == -1)
					break;
					
					//Write buffer space data to  file
					file.write(buffer, 0, read);
					downloaded += read;
					stateChanged();
				}
				
				if (status == COMPLETE) {
					status = COMPLETE;
					stateChanged();
				}

		}
		catch(Exception e) {
			error();
		}
		finally {
			
			if (file != null) {
				try {
					file.close();
				}
				catch(Exception e) {
					System.out.println("Unable to close file, nothing serious though!" + e.getMessage());
				}
			}
			
			if (stream != null) {
				try {
				stream.close();
				}
				catch (Exception e) {
					System.out.println("Unable to close stream, nothing serious though!" + e.getMessage());
				}
			}
		}
	}
	
	public void stateChanged() {
		setChanged();
		notifyObservers();
	}
	
}
