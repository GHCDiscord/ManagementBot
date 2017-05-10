# ManagementBot
Discord Bot mit allerlei Funktionen, die im Chat gebraucht werden.

*** Step 1: getting your own bot running:

You can create your own app, convert it to a bot and see your _____YOUR_App-ID_____ here:
https://discordapp.com/developers/applications/me
There are 2 Keys / Secrets for discord API. All of them are located in /content/Secure.java
1. add your "app bot user Token" to `public static final String DiscordToken = ""; `

Permissions required to run:
DOK Permissions:
https://discordapp.com/developers/docs/topics/permissions#bitwise-permission-flags
1. READ_MESSAGES	    0x00000400	Allows reading messages in a channel. The channel will not appear for users without this permission
2. SEND_MESSAGES	    0x00000800	Allows for sending messages in a channel.
3. MANAGE_MESSAGES 	0x00002000	Allows for deletion of other users messages
4. MANAGE_ROLES 	    0x10000000	Allows management and editing of roles (add role "verified")
5. Result:           0x10002C00 

AUTH Link: (manage roles permission)
https://discordapp.com/api/oauth2/authorize?client_id=_____YOUR_App-ID_____&scope=bot&permissions=0x10002C00

_______________________

*** Step 2: Twitter integration:
There are 4 Keys / Secrets for the Twitter API. All of them are located in /content/Secure.java
1.    `public static final String ConsumerKey = "";`
2.    `public static final String ConsumerSecret = "";`
3.    `public static final String AccessToken = "";`
4.    `public static final String AccesSecret = "";`

To find all the required information get yourself into https://dev.twitter.com/overview/api and make yourself familiar with the basics.
However, if you don't want to do this here is a step-by-step.
1. You will need a twitter account with a verified phone number. Go get it.
2. You need to join the dev.twitter.com team
3. Create your Twitter app here: https://apps.twitter.com (read only permission).
4. After you created your app you will find the `Consumer Key (API Key)` and the `Customer Secret (API Secret)` on the `Keys and Access Tokens` page.
5. Generate your access token (bottom of the page). You now got the `Access Token` and the `Access Token Secret`.
6. Add them to /content/Secure.java

_______________________

*** Step 3: Access to your IP Database
The bot does not have direct access to your database. It's using the API of your Website.
1. to generate a token for your bot login to any admin account, go to your user settings and click at `Neuer Token`.
2. add your "database token" to `public static final String DBToken = "";` in /content/Secure.java

_______________________

**Congratulations, your bot is ready to run.**
