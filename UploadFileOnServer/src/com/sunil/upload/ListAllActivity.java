package com.sunil.upload;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListAllActivity extends Activity implements OnItemClickListener {

	private static final String feed = "http://www.tabletzasvakog.com/android_fit/get_all_products.php";

	List<Item> arrayOfList;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_all);

		listView = (ListView) findViewById(R.id.listview);
		listView.setOnItemClickListener(this);

		if (Utils.isNetworkAvailable(ListAllActivity.this)) {
			new MyTask().execute(feed);
		} else {
			showToast("Nema mrezne konekcije!!!");
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_all, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Item item = arrayOfList.get(position);
		Intent intent = new Intent(ListAllActivity.this, DetailActivity.class);
		intent.putExtra("link", item.getLink());
		intent.putExtra("regplate", item.getRegPlate());
		intent.putExtra("description", item.getDescription());
		startActivity(intent);
	}


	// My AsyncTask start...

	class MyTask extends AsyncTask<String, Void, Void> {

		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			pDialog = new ProgressDialog(ListAllActivity.this);
			pDialog.setMessage("Ucitavam...");
			pDialog.show();

		}

		@Override
		protected Void doInBackground(String... params) {
			arrayOfList = new NamesParser().getData(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			if (null != pDialog && pDialog.isShowing()) {
				pDialog.dismiss();
			}

			if (null == arrayOfList || arrayOfList.size() == 0) {
				showToast("Nema podataka sa weba!!!");
				Intent i = new Intent(getApplicationContext(),
						MainActivity.class);
				// Closing all previous activities
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
			} else {

				// check data...
				/*
				 * for (int i = 0; i < arrayOfList.size(); i++) { Item item =
				 * arrayOfList.get(i); System.out.println(item.getId());
				 * System.out.println(item.getTitle());
				 * System.out.println(item.getDesc());
				 * System.out.println(item.getPubdate());
				 * System.out.println(item.getLink()); }
				 */

				setAdapterToListview();

			}

		}
	}

	public void setAdapterToListview() {
		NewsRowAdapter objAdapter = new NewsRowAdapter(ListAllActivity.this,
				R.layout.row, arrayOfList);
		listView.setAdapter(objAdapter);
	}

	public void showToast(String msg) {

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
