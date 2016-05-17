package com.vita.myutils.log;

import android.util.Log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 带日志文件输入的，又可控开关的日志调试
 *
 * @author BaoHang
 * @version 1.0
 * @data 2012-2-20
 */
public class LogHelper {
	private static Boolean MYLOG_SWITCH = true; // 日志文件总开关
	private static Boolean MYLOG_WRITE_TO_FILE = true;// 日志写入文件开关
	private static char MYLOG_TYPE = 'v';// 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
	private static String MYLOG_PATH_SDCARD_DIR = "/sdcard/yinuoinfo/log/";// 日志文件在sdcard中的路径
	private static int SDCARD_LOG_FILE_SAVE_DAYS = 0;// sd卡中日志文件的最多保存天数
	private static String MYLOGFILEName = "Log.txt";// 本类输出的日志文件名称
	private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式


	public static void w(String tag, Object msg) { // 警告信息
		log(tag, msg.toString(), 'w');
	}

	public static void e(String tag, Object msg) { // 错误信息
		log(tag, msg.toString(), 'e');
	}

	public static void d(String tag, Object msg) {// 调试信息
		log(tag, msg.toString(), 'd');
	}

	public static void i(String tag, Object msg) {//
		log(tag, msg.toString(), 'i');
	}

	public static void v(String tag, Object msg) {
		log(tag, msg.toString(), 'v');
	}

	public static void w(String tag, String text) {
		log(tag, text, 'w');
	}

	public static void e(String tag, String text) {
		log(tag, text, 'e');
	}

	public static void d(String tag, String text) {
		log(tag, text, 'd');
	}

	public static void i(String tag, String text) {
		log(tag, text, 'i');
	}

	public static void v(String tag, String text) {
		log(tag, text, 'v');
	}

	/**
	 * 根据tag, msg和等级，输出日志
	 *
	 * @param tag
	 * @param msg
	 * @param level
	 * @return void
	 * @since v 1.0
	 */
	private static void log(String tag, String msg, char level) {
		if (MYLOG_SWITCH) {
			if ('e' == level && ('e' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) { // 输出错误信息
				Log.e(tag, msg);
			} else if ('w' == level && ('w' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				Log.w(tag, msg);
			} else if ('d' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				Log.d(tag, msg);
			} else if ('i' == level && ('d' == MYLOG_TYPE || 'v' == MYLOG_TYPE)) {
				Log.i(tag, msg);
			} else {
				Log.v(tag, msg);
			}
			if (MYLOG_WRITE_TO_FILE)
				writeLogToFile(String.valueOf(level), tag, msg);
		}
	}

	/**
	 * 打开日志文件并写入日志
	 *
	 * @return
	 * **/
	private static void writeLogToFile(String mylogtype, String tag, String text) {// 新建或打开日志文件
		Date nowtime = new Date();
		String needWriteFiel = logfile.format(nowtime);
		String needWriteMessage = System.currentTimeMillis() + "    " + mylogtype
				+ "    " + tag + "    " + text;
		File filePath = new File(MYLOG_PATH_SDCARD_DIR);
		File file = new File(MYLOG_PATH_SDCARD_DIR, needWriteFiel
				+ MYLOGFILEName);
		try {
			if (!filePath.exists()) {
				filePath.mkdirs();
			}
			FileWriter filerWriter = new FileWriter(file, true);// 后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
			BufferedWriter bufWriter = new BufferedWriter(filerWriter);
			bufWriter.write(needWriteMessage);
			bufWriter.newLine();
			bufWriter.close();
			filerWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
/*

	private String m_title;
	private String m_message;
	private String qqMail = "nobody@yinuoinfo.com";
	private String revQQMail;
	private String password = "nb329A2";

	public synchronized void sendMail(String qqMail, String title, String message) {
		// this.qqMail = "";
		this.revQQMail = qqMail;
		// this.password = "5221474852";
		StringBuilder sb = new StringBuilder();
		sb.append("域名 = ");
		sb.append(ConfigInfo.httpBase);
		sb.append(" ,版本号 =");
		sb.append(ConfigInfo.version);
		sb.append(" ,商户号 = ");
		sb.append(GlobalData.masterId);
		sb.append(" ,商户名称 = ");
		sb.append(GlobalData.name);
		sb.append(" ,设备号 = ");
		sb.append(GlobalData.deviceId);
		sb.append(" ,token = ");
		sb.append(GlobalData.token);
		sb.append(" ,错误信息 = ");
		sb.append(message);
		m_message=sb.toString().replace("\n", "              ");
		m_title = "Android报错信息： " + title;
		//if (!MainActivity.getInstant().debug) {
		new Thread(send).start();
		//}
	}

	Runnable send = new Runnable() {

		@Override
		public synchronized void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			try {
				///rest/ApiCMembers/errorReport
				String url1 = ConfigInfo.httpBase + "rest/ApiCMembers/errorReport?";

				StringBuilder sb = new StringBuilder();
				sb.append("android端错误信息===============msg=设备号:");
				sb.append(GlobalData.deviceId);
				sb.append("商户： ");
				sb.append(GlobalData.userInfo);
				sb.append("标题:    ");
				sb.append(m_title);
				sb.append("                       报错内容:");
				sb.append(URLEncoder.encode(m_message,"UTF-8"));

				HttpClient.getInstance().getUrlInfo(url1,sb.toString());


				MailSenderInfo mailInfo = new MailSenderInfo();
				mailInfo.setMailServerHost("smtp.qq.com");
				mailInfo.setMailServerPort("25");
				mailInfo.setValidate(true);
				mailInfo.setUserName(qqMail); // 邮箱地址
				mailInfo.setPassword(password);// 邮箱密码
				mailInfo.setFromAddress(qqMail);
				mailInfo.setToAddress(revQQMail);
				mailInfo.setSubject(m_title);
				mailInfo.setContent(m_message);
				// mailInfo.setAttachFileNames(fileNames);
				//
				// 这个类主要来发送邮件
				SimpleMailSender sms = new SimpleMailSender();
				sms.sendTextMail(mailInfo);// 发送文体格式
				// sms.sendHtmlMail(mailInfo);//发送html格式


				Looper.loop();

			} catch (Exception e) {
				Log.e("SendMail", e.getMessage(), e);
				Log.i("wtudnLY", "SendMail="+e.getMessage());
			}
		}
	};
*/

	/**
	 * 删除制定的日志文件
	 * */
	public static void delFile() {// 删除日志文件
		String needDelFiel = logfile.format(getDateBefore());
		File file = new File(MYLOG_PATH_SDCARD_DIR, needDelFiel + MYLOGFILEName);
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * 得到现在时间前的几天日期，用来得到需要删除的日志文件名
	 * */
	private static Date getDateBefore() {
		Date nowtime = new Date();
		Calendar now = Calendar.getInstance();
		now.setTime(nowtime);
		now.set(Calendar.DATE, now.get(Calendar.DATE)
				- SDCARD_LOG_FILE_SAVE_DAYS);
		return now.getTime();
	}

}