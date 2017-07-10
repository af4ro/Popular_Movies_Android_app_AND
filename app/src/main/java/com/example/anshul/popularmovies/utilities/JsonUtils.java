package com.example.anshul.popularmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class JsonUtils {

    public static List<Map<String,String>> stringFromJson(String jsonString) throws JSONException{

        final String OWM_CHECK_CODE = "total_results";

        List<Map<String, String>> resultMoviesMap = new ArrayList<>();

        if(!jsonString.isEmpty()) {
            JSONObject moviesJson = new JSONObject(jsonString);

            //Error checking
            if (!moviesJson.has(OWM_CHECK_CODE) || moviesJson.getInt(OWM_CHECK_CODE) == 0) {
                return null;
            }

            //All results from one page
            JSONArray moviesArray = moviesJson.getJSONArray("results");

            for (int i = 0; i < moviesArray.length(); i++) {
                try {
                    Map<String, String> movieMap = new HashMap<>();
                    JSONObject movieJson = moviesArray.getJSONObject(i);

                    movieMap.put("vote_average", movieJson.get("vote_average").toString());
                    movieMap.put("title", movieJson.get("title").toString());
                    movieMap.put("popularity", movieJson.get("popularity").toString());
                    movieMap.put("poster_path", movieJson.get("poster_path").toString());
                    movieMap.put("original_language", movieJson.get("original_language").toString());
                    movieMap.put("original_title", movieJson.get("original_title").toString());
                    movieMap.put("backdrop_path", movieJson.get("backdrop_path").toString());
                    movieMap.put("adult", movieJson.get("adult").toString());
                    movieMap.put("overview", movieJson.get("overview").toString());
                    movieMap.put("release_date", movieJson.get("release_date").toString());
                    movieMap.put("id", movieJson.get("id").toString());

                    resultMoviesMap.add(movieMap);
                } catch (NullPointerException n) {
                    n.printStackTrace();
                }
            }
        }
        return resultMoviesMap;
    }
}
