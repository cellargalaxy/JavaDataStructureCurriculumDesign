package util;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * Created by cellargalaxy on 2017/5/31.
 */
public class ServletSendFile {
	public static void sendFile(HttpServletResponse response, InputStream inputStream, OutputStream outputStream, boolean isDownload, String fileName) throws IOException {
		response.reset();
		if (isDownload) {
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fileName, "UTF-8") + "\"");
		}
		
		int len;
		byte[] bs = new byte[1024 * 10];
		while ((len = inputStream.read(bs)) != -1) {
			outputStream.write(bs, 0, len);
			outputStream.flush();
		}
	}
}
