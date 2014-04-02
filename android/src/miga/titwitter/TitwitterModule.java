package miga.titwitter;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.KrollFunction;
import java.util.HashMap;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.KrollDict;
import android.content.Context;
import android.app.Activity;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.util.Collections;
import java.util.Comparator;

import java.util.List;
import org.appcelerator.kroll.common.Log;

@Kroll.module(name="Titwitter", id="miga.titwitter")
public class TitwitterModule extends KrollModule {
	
	Context context;
	Activity activity;
	AsyncTwitter twitter;
	KrollFunction success;
	private static final Object LOCK = new Object();
	boolean desc = false;
	long lastID=0;
	
        @Override
        public void onDestroy(Activity activity) {
	  super.onDestroy(activity);
        }
        
        @Override
        public void onResume(Activity activity) {
         super.onResume(activity);	  
        }
        
       
	@Override
	public void onStart(Activity activity) {
	  super.onStart(activity);
	  
        }
	
	public TitwitterModule () {
		super();
		TiApplication appContext = TiApplication.getInstance();
		activity = appContext.getCurrentActivity();
		context=activity.getApplicationContext();	
	}
	
	
	@Kroll.method
	public void connect(HashMap args){
	  KrollDict arg = new KrollDict(args);
	
	  ConfigurationBuilder cb = new ConfigurationBuilder();
	  cb.setDebugEnabled(true).setOAuthConsumerKey(arg.optString("apikey", "")).setOAuthConsumerSecret(arg.optString("apisecret", "")).setOAuthAccessToken(arg.optString("accesstoken", "")).setOAuthAccessTokenSecret(arg.optString("accesssecret", ""));
	  AsyncTwitterFactory tf = new AsyncTwitterFactory(cb.build());
	  twitter = tf.getInstance();
	  	  
	  
	  twitter.addListener(new TwitterAdapter() {
	    @Override 
	    public void updatedStatus(Status status) {
	      Log.d("Twitter","text: " + status.getText());
	    }
	  
	   
	  
	  
	    @Override
            public void searched(QueryResult result) {
                
		
		HashMap<String, KrollDict[]> event = new HashMap<String, KrollDict[]>();
		List<Status> tweets = result.getTweets();
		KrollDict[] dList = new KrollDict[tweets.size()];
		
		
		// sort tweets
		Collections.sort(tweets, new Comparator<Status>() {
		    public int compare(Status o1, Status o2) {
		    
			if (desc) {
			  if (o2.getId() < o1.getId()) {
			    return 1;
			  } else {
			    return -1;
			  }
			} else {
			  if (o2.getId() > o1.getId()) {
			    return 1;
			  } else {
			    return -1;
			  }
			}
		    }
		});
		
		// return tweets to titanium
		int i=0;
		for (Status tweet : tweets) {
		    if (lastID==-1 || lastID<tweet.getId()){
		      KrollDict d = new KrollDict();
		      d.put("username",tweet.getUser().getScreenName());
		      d.put("userimage",tweet.getUser().getProfileImageURL());
		      d.put("text",tweet.getText());
		      d.put("date",tweet.getCreatedAt());
		      d.put("id",Long.toString(tweet.getId()));
		      dList[i] = d;
		      lastID = tweet.getId();
		      i++;
		    }
		}
		
		KrollDict[] dList2 = new KrollDict[i];
		
		// shorten array
		System.arraycopy(dList, 0, dList2, 0, i);
		  
		event.put("tweets", dList2);
		success.call(getKrollObject(), event);
		  
                
                synchronized (LOCK) {
                    LOCK.notify();
                }
            }
            
           
            @Override
            public void onException(TwitterException e, TwitterMethod method) {
		  synchronized (LOCK) {
		      LOCK.notify();
		  }
		  Log.e("twitter","error: " + e.getErrorMessage());
            }
        });
	  Log.d("Twitter","connected");	  
	}
      
      @Kroll.method
      public void search(HashMap args){
	KrollDict arg = new KrollDict(args);
	Query query = new Query(arg.getString("query"));
	query.setLang(arg.optString("lang",""));
	query.setCount(arg.optInt("count",20));
	if (arg.optString("since","")!="") {
	  query.setSince(arg.getString("since"));
	}
	desc = arg.optBoolean("descending",false);
	success =(KrollFunction) arg.get("success");
	lastID = Long.parseLong(arg.optString("lastID","-1"));
	twitter.search(query);
      }

      
      @Kroll.method
      public void post(HashMap args){
	KrollDict arg = new KrollDict(args);
	String str = arg.optString("text","");
	if (str.length()>0){
	  twitter.updateStatus(str);
	}
      }
      
      
      @Kroll.method
      public void shutdown(){
	twitter.shutdown();
      }
	
}


