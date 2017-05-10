# ManagementBot
Discord Bot mit allerlei Funktionen, die im Chat gebraucht werden.

*** Step 1: getting your own bot running:

You can create your own app, convert it to a bot and see your _____YOUR_App-ID_____ here:
https://discordapp.com/developers/applications/me
There are 2 Keys / Secrets for discord API. All of them are located in /content/Secure.java
1. add your "app bot user Token" to `public static final String DiscordToken = ""; `
2. add your "database token" to `public static final String DBToken = "";` 

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
