package com.sunil.upload;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{

	private TextView messageText;
	private Button uploadButton, btnselectpic;
	private ImageView imageview;
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;

	private String upLoadServerUri = null;
	private String imagepath=null;
	

	JSONParser jsonParser = new JSONParser();
	EditText inputName;
	EditText inputDesc;

	// url to create new product
	private static String url_create_vehicle = "http://www.tabletzasvakog.com/android_fit/create_product.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		uploadButton = (Button)findViewById(R.id.uploadButton);
		messageText  = (TextView)findViewById(R.id.messageText);
		btnselectpic = (Button)findViewById(R.id.button_selectpic);
		imageview = (ImageView)findViewById(R.id.imageView_pic);
		// Edit Text
		inputName = (EditText) findViewById(R.id.inputName);
		inputDesc = (EditText) findViewById(R.id.inputDesc);

		btnselectpic.setOnClickListener(this);
		uploadButton.setOnClickListener(this);
		upLoadServerUri = "http://www.tabletzasvakog.com/android_fit/UploadToServer.php";
	}


	@Override
	public void onClick(View arg0) {
		if(arg0==btnselectpic)
		{
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			startActivityForResult(Intent.createChooser(intent, "Odaberite sliku koristeci:"), 1);
		}
		else if (arg0==uploadButton) {
			
			if(inputName.getText().toString().compareTo("") == 0 || inputDesc.getText().toString().compareTo("") == 0) {
			     // Your piece of code for example
			     Toast toast=Toast.makeText(getApplicationContext(), "Polja 'Naziv' i 'Opis' ne mogu biti prazna!", Toast.LENGTH_SHORT);  
			     toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
			     toast.show();
			 } else {
			     // Your code  
			 

			dialog = ProgressDialog.show(MainActivity.this, "", "Uploadujem sliku...", true);
			messageText.setText("Upload je zapoceo.....");
			//Ovdje dodajem kod za dodavanje proizvoda u bazu

			// creating new product in background thread
			new CreateNewProduct().execute();
			// kraj dodanog koda

			new Thread(new Runnable() {
				public void run() {

					uploadFile(imagepath);

				}
			}).start();    
			 }
		}

	} 

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1 && resultCode == RESULT_OK) {
			//Bitmap photo = (Bitmap) data.getData().getPath(); 

			Uri selectedImageUri = data.getData();
			Log.e("uploadFile", selectedImageUri.toString());
			imagepath = getPath(selectedImageUri);
			Bitmap bitmap=BitmapFactory.decodeFile(imagepath);
			imageview.setImageBitmap(bitmap);
			messageText.setText("Uploadujem sliku na:" +imagepath);

		}
	}
	@SuppressWarnings("deprecation")
	public String getPath(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	public int uploadFile(String sourceFileUri) {


		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;  
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024; 
		File sourceFile = new File(sourceFileUri); 

		if (!sourceFile.isFile()) {

			dialog.dismiss(); 

			Log.e("uploadFile", "Izvorna slika ne postoji :"+imagepath);

			runOnUiThread(new Runnable() {
				public void run() {
					messageText.setText("Izvorna slika ne postoji :"+ imagepath);
				}
			}); 

			return 0;

		}
		else
		{
			try { 

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(sourceFile);
				URL link = new URL(upLoadServerUri);

				// Open a HTTP  connection to  the URL
				conn = (HttpURLConnection) link.openConnection(); 
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("uploaded_file", fileName); 

				dos = new DataOutputStream(conn.getOutputStream());

				dos.writeBytes(twoHyphens + boundary + lineEnd); 
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ fileName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of  maximum size
				bytesAvailable = fileInputStream.available(); 

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);  

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);   

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response je : " 
						+ serverResponseMessage + ": " + serverResponseCode);

				if(serverResponseCode == 200){

					
					runOnUiThread(new Runnable() {
						public void run() {
							String msg = "Upload slike je zavrsen.\n\n uploadovanu sliku mozete pronaci na : \n\n"
									+" www.tabletzasvakog.com/android_fit/images/";
							messageText.setText(msg);
							Toast.makeText(MainActivity.this, "Upload slike kompletan.", Toast.LENGTH_SHORT).show();
						}
					});                
				}    

				//close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();  
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("MalformedURLException Exception : check script url.");
						Toast.makeText(MainActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
			} catch (Exception e) {

				dialog.dismiss();  
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						messageText.setText("Got Exception : see logcat ");
						Toast.makeText(MainActivity.this, "Got Exception : see logcat ", Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception", "Exception : "  + e.getMessage(), e);  
			}
			dialog.dismiss();       
			return serverResponseCode; 

		} // End else block 
	}
	// Klasa dodavanje novog proizvoda
	/**
	 * Background Async Task to Create new product
	 * */
	class CreateNewProduct extends AsyncTask<String, String, String> {

		/**
		 * Creating product
		 * */
		protected String doInBackground(String... args) {
			String regPlate = inputName.getText().toString();
			String description = inputDesc.getText().toString();
			String link = "http://www.tabletzasvakog.com/android_fit/images/" + (new File(imagepath).getName());

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("name", regPlate));
			params.add(new BasicNameValuePair("description", description));
			params.add(new BasicNameValuePair("link", link));

			// getting JSON Object
			// Note that create product url accepts POST method
			JSONObject json = jsonParser.makeHttpRequest(url_create_vehicle,
					"POST", params);

			// check log cat fro response
			Log.d("Create Response", json.toString());

			// check for success tag
			try {
				int success = json.getInt(TAG_SUCCESS);

				if (success == 1) {
					// successfully created product
					/*Intent i = new Intent(getApplicationContext(), AllProductsActivity.class);
					startActivity(i);*/
					Log.d("Create Response", String.valueOf(success));
					// closing this screen
					// finish();
				} else {
					// failed to create product
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_all, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.newvehicle:
			//define the file-name to save photo taken by Camera activity
			Intent i = new Intent(getApplicationContext(),
					MainActivity.class);
			// Closing all previous activities
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;

		case R.id.gallery:
			Intent ii = new Intent(getApplicationContext(),
					ListAllActivity.class);
			// Closing all previous activities
			ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(ii);
			return true;
		}
		return false;
	}


}