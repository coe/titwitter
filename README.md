Twitter4J (http://twitter4j.org) android module for Titanium

#What is working:
logging in and searching for tweets

#Functions:
##connect
###Parameter:
* apikey : "APIKEY"
* apisecret : "APISECRET"
* accesstoken : "ACCESSTOKEN"
* accesssecret : "ACCESSSECRET"

##search
###Parameter: 
* query: String (e.g. "#titanium")
* success : Success callbak
* descending: boolean (will sort the output in Java)
* count: int (maximum number of tweets <100)
* since: string (YYYY-MM-DD, tweets since date)
* lang: string (e.g. "de", language filter)
* lastID: int (will filter out all tweets < lastID)

#How to use:
see android/example/app.js 


Twitter4J is released under Apache License 2.0. Copyright 2007 Yusuke Yamamoto
