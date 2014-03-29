var twitter = require("miga.titwitter");
twitter.connect({
    apikey : "APIKEY", apisecret : "APISECRET", accesstoken : "ACCESSTOKEN", accesssecret : "ACCESSSECRET"
}); 


function onSearchDone(e) {
    var tweets = e.tweets;
    for (var i = 0; i < tweets.length; ++i) {
        Ti.API.info(tweets[i].username + " " + tweets[i].text);
    }
}

twitter.search({
    query : "#titanium", success : onSearchDone
});