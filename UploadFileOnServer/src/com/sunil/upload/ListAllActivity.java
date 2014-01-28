package com.sunil.upload;

import java.util.List;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class ListAllActivity extends Activity implements OnItemClickListener {

	private static final String feed = "http://10.0.2.2/android_fit/get_all_products.php";
	
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
			showToast("No Network Connection!!!");
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
		intent.putExtra("url", item.getLink());
		intent.putExtra("name", item.getName());
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
			pDialog.setMessage("Loading...");
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
				showToast("No data found from web!!!");
				ListAllActivity.this.finish();
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
}
