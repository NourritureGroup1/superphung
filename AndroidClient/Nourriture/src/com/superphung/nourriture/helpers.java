package com.superphung.nourriture;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import task.MySSLSocketFactory;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

public class helpers {
	public static boolean haveNetworkConnection(Context context) {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	    }
	    return haveConnectedWifi || haveConnectedMobile;
	}
	
	 private static HttpClient sslClient(HttpClient client) {
		    try {
		        X509TrustManager tm = new X509TrustManager() { 
		            public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		            }

		            public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		            }

		            public X509Certificate[] getAcceptedIssuers() {
		                return null;
		            }
		        };
		        SSLContext ctx = SSLContext.getInstance("TLS");
		        ctx.init(null, new TrustManager[]{tm}, null);
		        SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
		        ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		        ClientConnectionManager ccm = client.getConnectionManager();
		        SchemeRegistry sr = ccm.getSchemeRegistry();
		        sr.register(new Scheme("https", ssf, 443));
		        return new DefaultHttpClient(ccm, client.getParams());
		    } catch (Exception ex) {
		        return null;
		    }
		}
	 
    public static String getDatas(String address, List<NameValuePair> parameters){
    	
    	HttpClient client = new DefaultHttpClient();
    	client = sslClient(client);
    	
    	StringBuilder builder = new StringBuilder();
    	HttpPost httpGet = new HttpPost(address);
    	try{
            httpGet.setEntity(new UrlEncodedFormEntity(parameters));
    		HttpResponse response = client.execute(httpGet);
    		StatusLine statusLine = response.getStatusLine();
    		int statusCode = statusLine.getStatusCode();
    		if(statusCode == 200){
    			HttpEntity entity = response.getEntity();
    			InputStream content = entity.getContent();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(content));
    			String line;
    			while((line = reader.readLine()) != null){
    				builder.append(line);
    			}
    		} else {
    			Log.e(MainActivity.class.toString(),"Failed to get JSON object");
    			return "error";
    		}
    	}catch(ClientProtocolException e){
    		e.printStackTrace();
    	} catch (IOException e){
    		e.printStackTrace();
    	}
    	return builder.toString();
    }
    
    public final static boolean isValidEmail(CharSequence target) {
    	  if (TextUtils.isEmpty(target)) {
    	    return false;
    	  } else {
    	    return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    	  }
    }
}	