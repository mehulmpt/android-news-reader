package unofficial.com.inshorts.newsreader;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<newsItem> newsFeed = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        engine();
        addClickListener();
    }

    private void engine() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                "http://10.0.3.2/news.json",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray newsItems = response.getJSONArray("newsItems");

                            for (int i = 0; i < newsItems.length(); i++) {
                                JSONObject temp = newsItems.getJSONObject(i);

                                String image = temp.getString("image");
                                String title = temp.getString("title");
                                String time = temp.getString("time");
                                String date = temp.getString("date");
                                String content = temp.getString("content");
                                String link = temp.getString("link");

                                newsFeed.add(new newsItem(title, content, date, time, link, image));

                            }
                        } catch(JSONException e) {
                            Log.i("myTag", e.toString());
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("myTag", error.toString());
                    }
                });


        myReq.setRetryPolicy(new DefaultRetryPolicy(
                10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        queue.add(myReq);

        ArrayAdapter<newsItem> adapter = new customAdapter();

        ListView newsItems = (ListView) (findViewById(R.id.newsItems));
        newsItems.setAdapter(adapter);
    }

    private void addClickListener() {
        ListView newsItems = (ListView) (findViewById(R.id.newsItems));
        newsItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "I'm here at least...!", Toast.LENGTH_SHORT).show();
                newsItem currentItem = newsFeed.get(position);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(currentItem.getUrl()));
                startActivity(i);
            }
        });
    }

    private class customAdapter extends ArrayAdapter<newsItem> {
        public customAdapter() {
            super(MainActivity.this, R.layout.item, newsFeed);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item, parent, false);
            }

            newsItem currentItem = newsFeed.get(position);

            ImageView newsImage = (ImageView) convertView.findViewById(R.id.leftIco);
            TextView heading = (TextView) convertView.findViewById(R.id.heading);
            TextView desc = (TextView) convertView.findViewById(R.id.desc);



            heading.setText(currentItem.getNewsHeading());
            desc.setText(currentItem.getNewsDescSmall());
            Picasso.with(MainActivity.this).load(currentItem.getImageURL()).into(newsImage);


            return convertView;
        }
    }
}
