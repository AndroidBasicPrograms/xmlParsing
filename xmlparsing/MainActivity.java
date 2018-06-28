package com.example.jayhind.xmlparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MainActivity extends AppCompatActivity {
    String url="http://feeds.feedburner.com/ndtvnews-top-stories";
    News news;
    ListView lv;
    private ArrayList<News> newsList;
    private Context context;
    ArrayAdapter<News> ad;
    private String v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=findViewById(R.id.lv);
        context=this;
        newsList=new ArrayList<>();
        new FetchData().execute();
    }

    class FetchData extends AsyncTask<String, Void,String>
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(MainActivity.this);
            pd.setMessage("Wait While Loading...");
            pd.setCancelable(false);
            pd.show();
            Toast.makeText(context, "hellloo", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
           doXmlParsing();
           return null;
        }

        private void doXmlParsing() {
            SAXParserFactory spf=SAXParserFactory.newInstance();
            try {
                SAXParser parser=spf.newSAXParser();
                DefaultHandler handler=new DefaultHandler()
                {

                    boolean bTitle, bDesc, bLink, bStoryImage;

                    @Override
                    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        super.startElement(uri, localName, qName, attributes);
                        if(localName.equals("title"))
                        {
                            news = new News();
                            bTitle=true;
                        }else if(localName.equals("link"))
                        {
                            bLink=true;
                        }else if(localName.equals("description"))
                        {
                            bDesc=true;
                        }
                        else if(localName.equals("StoryImage"))
                        {
                            bStoryImage=true;
                        }
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        super.endElement(uri, localName, qName);
                        if(localName.equals("title"))
                        {
                            bTitle=false;
                        }else if(localName.equals("link"))
                        {
                            bLink=false;
                        }else if(localName.equals("description"))
                        {
                            bDesc=false;
                            newsList.add(news);
                        }else if(localName.equals("StoryImage"))
                        {
                            bStoryImage=false;
                        }
                    }

                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        super.characters(ch, start, length);
                        if(bTitle)
                        {
                            news.setTitle(new String(ch,start,length));
                            v= new String(ch,start,length);
                            Log.d("data",v);
                            //Toast.makeText(context,news.getTitle() , Toast.LENGTH_SHORT).show();
                        }else if(bLink)
                        {
                            news.setLink(new String(ch,start,length));
                        }else if(bDesc)
                        {
                            news.setDescription(new String(ch,start,length));
                        }else if(bStoryImage)
                            try {
                                //String temp=new String(ch,start, length);
                                news.setStoryImage(new String(ch, start, length));
                                URL url = new URL(news.getStoryImage());
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                news.setmBitmap(bmp);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                };
                parser.parse(url,handler);

            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
//          adapter.notifyDataSetChanged();
            ad=new ArrayAdapter<News>(context,android.R.layout.simple_list_item_1, newsList);
            lv.setAdapter(ad);
            Toast.makeText(context, "byyyyy", Toast.LENGTH_SHORT).show();
        }
    }
}
