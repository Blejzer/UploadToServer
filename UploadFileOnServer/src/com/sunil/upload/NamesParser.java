package com.sunil.upload;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	// url to get all products list
	private static String url_all_products = "http://10.0.2.2/android_fit/get_all_products.php";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_PRODUCTS = "products";
	private static final String TAG_PID = "pid";
	private static final String TAG_NAME = "name";
	private static final String TAG_DESCRIPTION = "description";
	private static final String TAG_CREATED = "created_at";
	private static final String TAG_UPDATED = "updated_at";
	private static final String TAG_LINK = "link";

	// products JSONArray
	JSONArray products = null;

	public List<Item> getData(String url) {

		try {

			listArray = new ArrayList<Item>();

			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			// getting JSON string from URL
			JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);

			// Check your log cat for JSON reponse
			Log.d("All Products: ", json.toString());


			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) 
			{
				// products found
				// Getting Array of Products
				products = json.getJSONArray(TAG_PRODUCTS);
				
				String cre;
				String year;
				String day;
				String month;
				// looping through All Products
				for (int i = 0; i < products.length(); i++) {
					JSONObject c = products.getJSONObject(i);

					// Storing each json item in variable
					String pid = c.getString(TAG_PID);

					String name = c.getString(TAG_NAME);

					String description = c.getString(TAG_DESCRIPTION);

					cre = c.getString(TAG_CREATED).substring(0, 10);
					year = cre.substring(0, 4);
					month = cre.substring(6,7);
					day = cre.substring(9, 10);
					
					String created = day.concat("/").concat(month).concat("/").concat(year);

					cre = c.getString(TAG_UPDATED).substring(0, 10);
					year = cre.substring(0, 4);
					month = cre.substring(6,7);
					day = cre.substring(9, 10);
					String updated = day.concat("/").concat(month).concat("/").concat(year);
					
					String link = c.getString(TAG_LINK);

					objItem = new Item();

					objItem.setPid(pid);
					objItem.setName(name);
					objItem.setDescription(description);
					objItem.setCreated_at(created);
					objItem.setUpdated_at(updated);
					objItem.setLink(link);

					listArray.add(objItem);						
				} 					
			} else {
				// no products found
				// Launch Add New product Activity
				return listArray;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} 

		return listArray;


		/*DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new URL(url).openStream());

			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName("item");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					objItem = new Item();

					objItem.setPid(getTagValue("pid", eElement));
					objItem.setName(getTagValue("name", eElement));
					objItem.setDescription(getTagValue("description", eElement));
					objItem.setCreated_at(getTagValue("created_at", eElement));
					objItem.setUpdated_at(getTagValue("updated_at", eElement));
					objItem.setLink(getTagValue("link", eElement));

					listArray.add(objItem);
				}
			}

		}*/ 


	}

	/*private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0)
				.getChildNodes();

		Node nValue = (Node) nlList.item(0);

		return nValue.getNodeValue();

	}*/
}
