package facebook2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
//import com.restfb .Version;
import com.restfb.types.Post;
import com.restfb.types.User;
//import com.restfb.types.Likes;          //need to find import
import com.restfb.types.Comment;        //grabs comments
import java.util.List;
import java.util.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
//import org.json.simple.JSONObject;
//import javax.json.JsonObject;
public class facebooknetwork {

	public Properties properties = new Properties();
	public FacebookClient fbClient = null;
	public String accessToken = null;
        public String secret = null;
	
	public facebooknetwork()
	{
		try {
		//	properties.load(new FileInputStream("fb.properties"));
			//this.accessToken = properties.getProperty("access_token");
                         String MY_ACCESS_TOKEN=""; //must add access token here. 
                         //This is temporary token, however, it seemss there is a way to have it extended
                         String MY_APP_SECRET="";   //add app secret here
                         accessToken=MY_ACCESS_TOKEN;
			//this.fbClient = new DefaultFacebookClient(this.accessToken);
                        this.fbClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);	
                        this.accessToken=MY_ACCESS_TOKEN;
                        this.secret=MY_APP_SECRET;
		//} //catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * Get a list of your facebook friends and related details
	 */
	public void GetFriends()
	{
		//this is a bad hack to get the https to work. not recommended
		javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[]{
			    new X509TrustManager() {
			        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			            return null;
			        }
			        public void checkClientTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			        public void checkServerTrusted(
			            java.security.cert.X509Certificate[] certs, String authType) {
			        }
			    }
			};
		
		// Install the all-trusting trust manager
		try {
		    SSLContext sc = SSLContext.getInstance("SSL");
		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		Connection<User> myFriends = this.fbClient.fetchConnection("me/friends", User.class);
		
                Connection<Post> pagePosts = fbClient.fetchConnection("me/feed", Post.class);
                for (List<Post> posts: pagePosts)
                    for (Post post: posts){
                        for (Comment comment: post.getComments().getData()){
                            System.out.println("Number of Comments: " + comment);
                        }
                        String message = post.getMessage();
                        String id = post.getId();
                        long timestampt = post.getCreatedTime().getTime()/1000;
                    }
                
		for (User friend: myFriends.getData())
		{
			String id = friend.getId();
			String nodeUrl = "https://graph.facebook.com/" + id + "?access_token=" + properties.getProperty("access_token");
			try {
				URL graphNode = new URL(nodeUrl);
				URLConnection conn = graphNode.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				BufferedWriter writer = new BufferedWriter(new FileWriter("facebook-friends/" + id + ".txt"));
				while ((line = in.readLine()) != null)
				{
					writer.write(line);
				}
				in.close();
				writer.flush();
				writer.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
        User getUser(String userName){
      //String MY_ACCESS_TOKEN="EAAGT8Wfe31wBADKZBK9T42PO0iVUXBejTfYQVkylaFp9ShKWI9aBRRppfuwDqtc3yMauxbbOV1DgPVB3xK1TziXrfWxC2tOwhxm7lvaDoVGV0MGQFqHyZCeX0daepg2EKifN8NTyXhZCDZCBpDg4LfIUiNROwTanxf14RuBkqQZDZD";
      //String MY_APP_SECRET="32c7c5246ca8ca1df6d8b52fba51eec2";
     // facebookClient = new DefaultFacebookClient(MY_ACCESS_TOKEN);
      User user = this.fbClient.fetchObject(userName, User.class);
      return user;
  }
  
  void getfeed(){
      Connection<Post> myFeed = this.fbClient.fetchConnection("me/feed", Post.class);

        //System.out.println("Count of my friends: " + myFriends.getData().size());
        System.out.println("First item in my feed: " + myFeed.getData().get(0));

        // Connections support paging and are iterable

        for (List<Post> myFeedConnectionPage : myFeed)
            for (Post post : myFeedConnectionPage)
                System.out.println("Post: " + post);

  }
  
  void getComment(){
      Connection<Post> myComments = this.fbClient.fetchConnection("me/comment", Post.class);
      System.out.println("First Comment: " + myComments.getData().get(0));
      
      for (List<Post>  myCommentsConnectionPage : myComments)
          for (Post post : myCommentsConnectionPage)
              System.out.println("Comments: " + post);
      
  }

void getLikes(){
    Connection<Post> myLikes = this.fbClient.fetchConnection("me/likes", Post.class);
    System.out.println("Page likes: " + myLikes.getData().get(0));
   // System.out.println("Number of likes: " + myLikes.getCount());

}

/*void getComments(){
    Post.Comments comments = fbClient.fetchObject(Post.class + "/comments", Post.Comments.class,
    Parameter.with("summary", 1), Parameter.with("limit", 0));
    long commentsTotalCount = comments.getCount();
}*/
	
	/**
	 * @param args
	 */
       // FacebookClient fbClient=null;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//********************MAIN OUTPUT *******************************************************

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public static void main(String[] args) throws FileNotFoundException {
            
		facebooknetwork fn = new facebooknetwork();
                 fn.fbClient = new DefaultFacebookClient(fn.accessToken);
                User me=null;
                System.setOut(new PrintStream(new FileOutputStream("home.txt")));
      try{
         // user= fb1.getUser("me");
          me = fn.fbClient.fetchObject("me", User.class, Parameter.with("fields", "email,first_name,last_name,gender, hometown, birthday, friends, work, education"));
          System.out.println(me.getFirstName()+ me.getLastName());
        //  User zuck = fn.fbClient.fetchObject("zuck", User.class);
      //System.out.println(zuck.getName());
          List<User> myFriends = fn.fbClient.fetchConnection("me/friends",
            User.class, Parameter.with("fields", "first_name, last_name, gender, birthday, hometown, education, friends, work"))
            .getData();
          
          
          me = fn.fbClient.fetchObject("me", User.class, Parameter.with("fields","friends"));
      System.out.println("Total friends: " + myFriends.size());
    
      
     
      
      //System.out.println("Number of comments: " + comment.size());
      }
      catch(Exception ex){
          System.out.println(ex.getMessage());
      }
       Connection<User> myFriends=null;
      try{
/*          JsonObject obj = fn.fbClient.fetchObject("me/friends", JsonObject.class);

int totalCount = 0;
if (obj.isEmpty()==false){// .has("summary")) {
     totalCount = obj.getJsonObject("summary").getJsonNumber("total_count").intValue();// .getLong("total_count");
*/
       //   myFriends = fn.fbClient.fetchConnection("me/friends", User.class);
          myFriends = fn.fbClient.fetchConnection("me/friends", User.class, 
    Parameter.with("fields", "id,first_name,last_name,name,gender,education,work"));
        Connection<Post> myFeed = fn.fbClient.fetchConnection("me/feed", Post.class);
      }
      catch(Exception ex){
          
      }
      if(myFriends!=null){
      List<User> users=myFriends.getData();

    for(Iterator iterator=users.iterator();iterator.hasNext();)

    {
        User user=(User)iterator.next();
        System.out.println(user.getFirstName()+ user.getLastName());
        System.out.println(user.getRelationshipStatus());

    }
      }
      try{
      fn.getfeed();
      }
      catch(Exception ex){
          System.out.println(ex.getMessage());
      }
		fn.GetFriends();
    }
	}
