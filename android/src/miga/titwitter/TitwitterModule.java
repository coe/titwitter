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


import java.util.List;
import org.appcelerator.kroll.common.Log;

@Kroll.module(name="Titwitter", id="miga.titwitter")
public class TitwitterModule extends KrollModule {
	
	Context context;
	Activity activity;
	Twitter twitter;
	
	
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
	  TwitterFactory tf = new TwitterFactory(cb.build());
	  twitter = tf.getInstance();
	  Log.d("Twitter","connected");
	}
      
      @Kroll.method
      public void search(HashMap args){
	KrollDict arg = new KrollDict(args);
	KrollFunction success =(KrollFunction) arg.get("success");
	HashMap<String, KrollDict[]> event = new HashMap<String, KrollDict[]>();
	
	try {
	      Query query = new Query(arg.getString("query"));
	      QueryResult result;
	  
	      result = twitter.search(query);
	      List<Status> tweets = result.getTweets();
	      KrollDict[] dList = new KrollDict[tweets.size()];
	      
	      int i=0;
	      for (Status tweet : tweets) {
		  KrollDict d = new KrollDict();
		  d.put("username",tweet.getUser().getScreenName());
		  d.put("text",tweet.getText());
		  dList[i] = d;
		  i++;
	      }
		
	      event.put("tweets", dList);
	      success.call(getKrollObject(), event);
	  } catch (TwitterException te) {
	      
	      Log.d("twitter","Failed to search tweets: " + te.getMessage());
	     
	  }
	 
	}
      
	
}


