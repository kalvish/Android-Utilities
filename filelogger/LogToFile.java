package packagename;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.Environment;

public class LogToFile implements Runnable {
	public static BufferedWriter out;
	String tag;
	String message;
	static File LogFilePath;
	public static LogToFile logFile;
	static SimpleDateFormat formatter;

	public static LogToFile getInstance() {
		if (logFile == null) {
			logFile = new LogToFile();
		}
		return logFile;
	}

	public LogToFile() {
		try {
			createFileOnDevice(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter LogWriter = null;
		try {
			LogWriter = new FileWriter(LogFilePath, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		out = new BufferedWriter(LogWriter);
		formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
	}

	private static void createFileOnDevice(Boolean append) throws IOException {
		if (LogFilePath == null) {
			File logDirectory = new File(
					Environment.getExternalStorageDirectory(), "FolderToStore/Logs/");
			if (!logDirectory.exists()) {
				logDirectory.mkdirs();
			}

			if (logDirectory.canWrite()) {
				LogFilePath = new File(logDirectory, "fileToStore.txt");
			}
		}
	}

	/**
	 * function to call the method for writing,
	 * 
	 * @param tag
	 *            is for display the tag of the message
	 * @param message
	 *            for display the message
	 */
	public static void d(String tag, String message) {
		try {
			LogToFile.getInstance().tag = tag;
			LogToFile.getInstance().message = message;
			Thread th = new Thread(LogToFile.getInstance());
			th.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			String date = formatter.format(Calendar.getInstance().getTime());
			out.write(tag + ":" + message + ":" + date + "\r\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
