package task;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import model.Ingredient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.superphung.nourriture.Globals;
import com.superphung.nourriture.helpers;

@SuppressWarnings("deprecation")
public class AddIngredientTask extends AsyncTask<String, Void, String> {
	private Context context;
	private Ingredient ingredient;
	private ProgressDialog progress;
	public static final String URL_API = "https://54.64.212.101";
	//public static final String URL_API = "https://192.168.0.103:8081";

	public AddIngredientTask(Context c_, Ingredient ingredient_) {
		context = c_;
		ingredient = ingredient_;
	}

	@Override
	protected void onPreExecute() {
		progress = new ProgressDialog(context);
		progress.setMessage("Uploading...");
		progress.show();
	}


	@Override
	protected String doInBackground(String... params) {
		return addIngredient();
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("result =>"+result);
		if (progress.isShowing()) {
			progress.dismiss();
		}
		if (result.equals("error"))
		{
			Toast toast = Toast.makeText(context, "no internet connection available", Toast.LENGTH_SHORT);
			toast.show();
			return ;
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {}


	public String addIngredient()
	{
		if (helpers.haveNetworkConnection(context))
		{
			/*List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			parameters.add(new BasicNameValuePair("name", ingredient.getName()));
			parameters.add(new BasicNameValuePair("description", ingredient.getDescription()));
			parameters.add(new BasicNameValuePair("category", ingredient.getCategory()));
			parameters.add(new BasicNameValuePair("nutrients", ingredient.getNutrients()));
			System.out.println(Globals.MainActivityDatas.urls.get("POST").get("ingredient"));
			String result = helpers.getDatas(Globals.MainActivityDatas.urls.get("POST").get("ingredient"), parameters, "POST");
			System.out.println(result);
			JSONObject creation = null;
			try {
				creation = new JSONObject(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String id_creation = null;
			try {
				id_creation = creation.get("_id").toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			SampleFileUpload fileUpload = new SampleFileUpload () ;
			System.out.println("JE VAIS TESTER CETTE URL : "+ingredient.getImgUrl());
			System.out.println("ET JE CALL CETTE URL : "+Globals.MainActivityDatas.urls.get("POST").get("ingredient"));
			File file = new File (ingredient.getImgUrl()) ;
			fileUpload.executeMultiPartRequest(Globals.MainActivityDatas.urls.get("POST").get("ingredient"), file, file.getName(), "File Upload test Hydrangeas.jpg description") ;
			return "success";
		}
		else
			return "error";
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

	public class SampleFileUpload {
		/**
		 * A generic method to execute any type of Http Request and constructs a response object
		 * @param requestBase the request that needs to be exeuted
		 * @return server response as <code>String</code>
		 */
		private String executeRequest(HttpRequestBase requestBase){
			String responseString = "" ;

			InputStream responseStream = null ;
			HttpClient client = new DefaultHttpClient () ;
			client = sslClient(client);
			try{
				HttpResponse response = client.execute(requestBase) ;
				System.out.println("MON STATUS CODE EST == "+response.getStatusLine().getStatusCode());
				if (response != null){
					HttpEntity responseEntity = response.getEntity() ;

					if (responseEntity != null){
						responseStream = responseEntity.getContent() ;
						if (responseStream != null){
							BufferedReader br = new BufferedReader (new InputStreamReader (responseStream)) ;
							String responseLine = br.readLine() ;
							String tempResponseString = "" ;
							while (responseLine != null){
								tempResponseString = tempResponseString + responseLine + System.getProperty("line.separator") ;
								responseLine = br.readLine() ;
							}
							br.close() ;
							if (tempResponseString.length() > 0){
								responseString = tempResponseString ;
							}
						}
					}
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if (responseStream != null){
					try {
						responseStream.close() ;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			client.getConnectionManager().shutdown() ;

			return responseString ;
		}

		/**
		 * Method that builds the multi-part form data request
		 * @param urlString the urlString to which the file needs to be uploaded
		 * @param file the actual file instance that needs to be uploaded
		 * @param fileName name of the file, just to show how to add the usual form parameters
		 * @param fileDescription some description for the file, just to show how to add the usual form parameters
		 * @return server response as <code>String</code>
		 */
		public String executeMultiPartRequest(String urlString, File file, String fileName, String fileDescription) {

			HttpPost postRequest = new HttpPost (urlString) ;
			try{

				MultipartEntity multiPartEntity = new MultipartEntity () ;
				//The usual form parameters can be added this way
				multiPartEntity.addPart("name", new StringBody(ingredient.getName()));
				multiPartEntity.addPart("description", new StringBody(ingredient.getDescription()));
				multiPartEntity.addPart("category", new StringBody(ingredient.getCategory()));
				multiPartEntity.addPart("nutrients", new StringBody(ingredient.getNutrients()));

				/*Need to construct a FileBody with the file that needs to be attached and specify the mime type of the file. Add the fileBody to the request as an another part.
	            This part will be considered as file part and the rest of them as usual form-data parts*/
				FileBody fileBody = new FileBody(file, file.getName(), "image/jpeg","UTF-8") ;
				multiPartEntity.addPart("file", fileBody) ;

				postRequest.setEntity(multiPartEntity) ;
			}catch (UnsupportedEncodingException ex){
				ex.printStackTrace() ;
			}
			return executeRequest (postRequest) ;
		}
	}
}