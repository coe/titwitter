var twitter = require("miga.titwitter");
twitter.connect({
    apikey : "APIKEY", apisecret : "APISECRET", accesstoken : "ACCESSTOKEN", accesssecret : "ACCESSSECRET"
}); 


function onSearchDone(e) {
    var tweets = e.tweets;
    for (var i = 0; i < tweets.length; ++i) {
        Ti.API.info(tweets[i].username + " " + tweets[i].text + " " + tweets[i].id + " " + tweets[i].date);
    }
}

// just do a search
twitter.search({
    query : "#titanium", success : onSearchDone, descending:true
});

// search but only get tweets after lastID
twitter.search({
    query : "#titanium", success : onSearchDone, descending:true, lastID: 1000000
});

// search with date
twitter.search({
    query : "#titanium", success : onSearchDone, descending:true, since:"YYYY-MM-DD"
});

// search with language filter
twitter.search({
    query : "#titanium", success : onSearchDone, descending:true, lang:"de"
});

// search with max count
twitter.search({
    query : "#titanium", success : onSearchDone, descending:true, count: 10
});