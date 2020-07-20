package com.qupp.client.network;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * author：wangqi on 2018/1/4 16:39
 */
public class AsyncTaskForUpdata extends AsyncTask<String, Void, String> {

	private static AsyncTaskForUpdata task;
	public AsyncTaskForUpdata(Context mcontext) {
		this.mcontext = mcontext;
	}
	private Context mcontext;
	private String downpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/app/pdf/";

	public static interface Open{
		void listener(File file);
	}

	private Open open;

	public void setOpen(Open open){
		this.open = open;
	}

	@Override
	protected String doInBackground(String... urls) {
		try {
			String url = urls[0];
			String filename = urls[1];
			return download(url,filename);
		}catch (Exception e){

		}
		return "";

	}

	public static AsyncTaskForUpdata newInstance(Context context){
		task = new AsyncTaskForUpdata(context);
		return task;
	}


	@Override
	protected void onPostExecute(String filepath) {
		//Toast.makeText(mcontext,filepath,Toast.LENGTH_LONG).show();
		open.listener(new File(filepath));

	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	public String download(String url, String filename){
		float filesize,mfinished=0;
		File filepath = new File(downpath);
		if(!filepath.exists()){
			filepath.mkdirs();
		}
		File file = new File(downpath+"/"+filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			URL durl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) durl.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			conn.setConnectTimeout(30000);
			filesize = conn.getContentLength();
			InputStream is=conn.getInputStream();
	        FileOutputStream fs=new FileOutputStream(file);
	        int has=0;
	        byte[] b=new byte[1024];
	        long time = System.currentTimeMillis();
	        while((has=is.read(b))>-1)
	        {
	        	fs.write(b, 0, has);
	        	mfinished = has+mfinished;
	        	
	        	if (System.currentTimeMillis() - time > 100) {
					Log.v("jindu",  ((int)(mfinished*100/ filesize))+"");
					time = System.currentTimeMillis();
				}

	        }
            is.close();
            fs.close();
            return file.getAbsolutePath();
		} catch (Exception ex) {
			ex.printStackTrace();
			return "";
		}
	}

	

}
