# LiteTwitch
Just a small test for new Android APIs and Twitch

# Done
* Get client id for application
* Test API calling using client id

# TODO
* Create first screen where the user can input his own account name
* Save the user id in SharedPreferences to omit seeing this screen every time
* Get list of user's follows
* Write ListAdapter for users follow list
* Show online first, apply grey hue to offline channels? Maybe show last online message


# Extras
* Let user favorite channels to put them at the top of the list
* Register service to poll for when a channel is online? [This seems to be an OK approach, check every 5 minutes for any channel that is set to notify, otherwise no job scheduler]
* Add progress spinner in splash screen while checking if account exists

# Very extra
* Add local webserver port to verify if an application wants to provide access? Maybe using a webview..