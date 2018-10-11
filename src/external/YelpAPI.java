package external;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Item;

public class YelpAPI {
	private static final String HOST = "https://api.yelp.com";
	private static final String ENDPOINT = "/v3/businesses/search";
	private static final String DEFAULT_TERM = "";
	private static final int SEARCH_LIMIT = 20;

	private static final String TOKEN_TYPE = "Bearer";
	private static final String API_KEY = "LWc7F_SaopAINieYeqO9swd_WP5pywJ5m_ARzQGDt3ytxp1RbUTMbgT4re1VabecI1wjfAd6VD9F2HdXz-FBBx0Ak4N2NfVqCzaEN3t0MnzW-1hcxTYViiIC0Kq6W3Yx";

	// input query information
	// return information in JSONArray form
	public JSONArray search(double lat, double lon, String term) {
		// sanity check
		if (term == null || term.length() == 0) {
			term = DEFAULT_TERM;
		}

		// prepare for request URL:
		// URL = HOST + ENDPOINT + "?" + PARAMETERS.
		try {
			term = URLEncoder.encode(term, "UTF-8"); // Rick Sun => Rick20%Sun
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String query = String.format("term=%s&latitude=%s&longitude=%s&limit=%s", term, lat, lon, SEARCH_LIMIT);
		String url = HOST + ENDPOINT + "?" + query;

		try {
			/*
			 * High level: Construct a connection(abstract in an object) Necessary
			 * materials: URL,Http Method. Optional materials:RequestProperty
			 */
			/*
			 * Steps: 1.abstract a URL by constructing a URL object. 2.get the abstraction
			 * of the connection of that URL by calling URL.openconnection(). 3.
			 */
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Authorization", TOKEN_TYPE + " " + API_KEY);
			int responseCode = connection.getResponseCode();
			// testing if we get the reponse we want.
			System.out.println("Sending request to URL: " + url);
			System.out.println("responseCode: " + responseCode);

			if (responseCode != 200) {
				return new JSONArray();
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String inputLine = "";
			StringBuilder response = new StringBuilder();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JSONObject obj = new JSONObject(response.toString());
			if (!obj.isNull("businesses")) {
				return obj.getJSONArray("businesses");
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return new JSONArray();
	}

	private void queryAPI(double lat, double lon) {
		JSONArray items = search(lat, lon, "restaurants");
		try {
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = items.getJSONObject(i);
				System.out.println(item.toString(2));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private List<Item> getItemList(JSONArray restaurants) throws JSONException {
		List<Item> list = new ArrayList<>();
		for (int i = 0; i < restaurants.length(); i++) {
			
		}
		return list;
	}

	public static void main(String[] args) {
		YelpAPI oneConnection = new YelpAPI();
		oneConnection.queryAPI(37.38, -122.08);
	}
}
