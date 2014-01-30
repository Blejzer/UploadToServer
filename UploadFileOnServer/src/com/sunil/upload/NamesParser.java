package com.sunil.upload;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class NamesParser {

	Item objItem;
	List<Item> listArray;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	// url to get all vehicles list
	private static String url_all_vehicles = "http://www.tabletzasvakog.com/android_fit/get_all_products.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_VEHICLES = "products";
	private static final String TAG_PID = "pid";
	private static final String TAG_REGPLATE = "name";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CREATED = "created_at";
	private static final String TAG_UPDATED = "updated_at";
	private static final String TAG_LINK = "link";

	// vehicles JSONArray
	JSONArray vehicles = null;

	public List<Item> getData(String url) {

		try {

			listArray = new ArrayList<Item>();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_vehicles, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All Vehicles: ", json.toString());


			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) 
			{
				// vehicles found
				// Getting Array of Vehicles
				vehicles = json.getJSONArray(TAG_VEHICLES);
				
				String cre;
				String year;
				String day;
				String month;
				// looping through All Vehicles
				for (int i = 0; i < vehicles.length(); i++) {
					JSONObject c = vehicles.getJSONObject(i);

					// Storing each json item in variable
					String pid = c.getString(TAG_PID);

					String regPlate = c.getString(TAG_REGPLATE);

					String description = c.getString(TAG_DESCRIPTION);

					cre = c.getString(TAG_CREATED).substring(0, 10);
					year = cre.substring(0, 4);
					month = cre.substring(6,7);
					day = cre.substring(8, 9);
					
					String created = day.concat("/").concat(month).concat("/").concat(year);

					cre = c.getString(TAG_UPDATED).substring(0, 10);
					year = cre.substring(0, 4);
					month = cre.substring(6,7);
					day = cre.substring(8, 9);
					String updated = "";
					if(year !="0000"){
					updated = day.concat("/").concat(month).concat("/").concat(year);
					}
					
					String temp = c.getString(TAG_LINK).substring(c.getString(TAG_LINK).lastIndexOf("/"));
					String link = "http://www.tabletzasvakog.com/android_fit/images/" + temp;

					objItem = new Item();

					objItem.setPid(pid);
					objItem.setRegPlate(regPlate);
					objItem.setDescription(description);
					objItem.setCreated_at(created);
					objItem.setUpdated_at(updated);
					objItem.setLink(link);

					listArray.add(objItem);						
				} 					
			} else {
				// no vehicles found
				// Launch Add New vehicle Activity
				return listArray;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return listArray;
	}
}
