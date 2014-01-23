package ba.fit.dl1851;

import java.util.ArrayList;
import java.util.List;
 
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class EditPictureActivity extends Activity {
 
    EditText txtNaziv;
    EditText txtPrice;
    EditText txtDesc;
    EditText txtCreatedAt;
    Button btnSave;
    Button btnDelete;
 
    String pid;
 
    // Progress Dialog
    private ProgressDialog pDialog;
 
    // JSON parser class
    JSONParser jsonParser = new JSONParser();
 
    // single picture url
    private static final String url_picture_detials = "http://127.0.0.1/android_connect/get_picture_details.php";
 
    // url to update picture
    private static final String url_update_picture = "http://127.0.0.1/android_connect/update_picture.php";
 
    // url to delete picture
    private static final String url_delete_picture = "http://127.0.0.1/android_connect/delete_picture.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PICTURE = "picture";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAZIV = "naziv";
    private static final String TAG_PRICE = "price";
    private static final String TAG_DESCRIPTION = "description";
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_picture);
 
        // save button
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
 
        // getting picture details from intent
        Intent i = getIntent();
 
        // getting picture id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
 
        // Getting complete picture details in background thread
        new GetPictureDetails().execute();
 
        // save button click event
        btnSave.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // starting background task to update picture
                new SavePictureDetails().execute();
            }
        });
 
        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View arg0) {
                // deleting picture in background thread
                new DeletePicture().execute();
            }
        });
 
    }
 
    /**
     * Background Async Task to Get complete picture details
     * */
    class GetPictureDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPictureActivity.this);
            pDialog.setMessage("Loading picture details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Getting picture details in background thread
         * */
        protected String doInBackground(String... params) {
 
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    // Check for success tag
                    int success;
                    try {
                        // Building Parameters
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("pid", pid));
 
                        // getting picture details by making HTTP request
                        // Note that picture details url will use GET request
                        JSONObject json = jsonParser.makeHttpRequest(
                                url_picture_detials, "GET", params);
 
                        // check your log for json response
                        Log.d("Single Picture Details", json.toString());
 
                        // json success tag
                        success = json.getInt(TAG_SUCCESS);
                        if (success == 1) {
                            // successfully received picture details
                            JSONArray pictureObj = json
                                    .getJSONArray(TAG_PICTURE); // JSON Array
 
                            // get first picture object from JSON Array
                            JSONObject picture = pictureObj.getJSONObject(0);
 
                            // picture with this pid found
                            // Edit Text
                            txtNaziv = (EditText) findViewById(R.id.inputNaziv);
                            txtDesc = (EditText) findViewById(R.id.inputDesc);
 
                            // display picture data in EditText
                            txtNaziv.setText(picture.getString(TAG_NAZIV));
                            txtDesc.setText(picture.getString(TAG_DESCRIPTION));
 
                        }else{
                            // picture with pid not found
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            pDialog.dismiss();
        }
    }
 
    /**
     * Background Async Task to  Save picture Details
     * */
    class SavePictureDetails extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPictureActivity.this);
            pDialog.setMessage("Saving picture ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Saving picture
         * */
        protected String doInBackground(String... args) {
 
            // getting updated data from EditTexts
            String naziv = txtNaziv.getText().toString();
            String price = txtPrice.getText().toString();
            String description = txtDesc.getText().toString();
 
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(TAG_PID, pid));
            params.add(new BasicNameValuePair(TAG_NAZIV, naziv));
            params.add(new BasicNameValuePair(TAG_PRICE, price));
            params.add(new BasicNameValuePair(TAG_DESCRIPTION, description));
 
            // sending modified data through http request
            // Notice that update picture url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_picture,
                    "POST", params);
 
            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about picture update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update picture
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once picture uupdated
            pDialog.dismiss();
        }
    }
 
    /*****************************************************************
     * Background Async Task to Delete Picture
     * */
    class DeletePicture extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditPictureActivity.this);
            pDialog.setMessage("Deleting picture...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Deleting picture
         * */
        protected String doInBackground(String... args) {
 
            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("pid", pid));
 
                // getting picture details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_picture, "POST", params);
 
                // check your log for json response
                Log.d("Delete picture", json.toString());
 
                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // picture successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about picture deletion
                    setResult(100, i);
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once picture deleted
            pDialog.dismiss();
 
        }
 
    }
}