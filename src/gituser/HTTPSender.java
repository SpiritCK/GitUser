package gituser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class HTTPSender {
	/**
	 * Personal GitHub API token
	 */
	private final String token = "b4565a9f29f8c0d8689dd1ea6357b9cd0d8932f9";
	
	/**
	 * Send GET request
	 * @param url URL request
	 * @return Response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object getRequest(String url) throws Exception {
		URL obj = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
		conn.setRequestProperty("Authorization", "token " + token);

		conn.setRequestMethod("GET");
		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			Integer temp = responseCode;
			throw new Exception(temp.toString());
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null) { response.append(inputLine); }
		
		in.close();
		JSONParser parser = new JSONParser();
		Object obj2 = parser.parse(response.toString());
		JSONObject obj3 = new JSONObject();
	    obj3.put("item", obj2);

		Map<String, List<String>> map = conn.getHeaderFields();

		//System.out.println("Printing Response Header...\n");
		int pageNumber = 1;

		for (Map.Entry<String, List<String>> entry : map.entrySet()) {
			System.out.println("Key : " + entry.getKey()
	                           + " ,Value : " + entry.getValue());
			if (entry.getKey() != null && entry.getKey().toString().equals("Link")) {
				Pattern pattern = Pattern.compile("rel=\"next\"(?:.*)page=(.*?)>; rel=\"last\"");
				Matcher matcher = pattern.matcher(entry.getValue().toString());

		        List<String> listMatches = new ArrayList<String>();

		        while(matcher.find())
		        {
		            listMatches.add(matcher.group(1));
		        }
		        if (listMatches.size() > 0) {
		        	pageNumber = Integer.parseInt(listMatches.get(0));
		        }
			}
		}
		obj3.put("page_number", pageNumber);
		System.out.println(obj3);
	    return obj3;
	}
}
