# LiteTwitch
Just a small test for new Android APIs and Twitch

# Done
* Get client id for application
* Test API calling using client id
* Create first screen where the user can input his own account name
* Parse a JSON Response from the API end point and convert it to a Java object (thanks Gson) 
* (Partially Done) Write ListAdapter for users follow list, tentative layout and fragment done


# TODO
* Save the user id in SharedPreferences to omit seeing this screen every time
* Clean up ResponseParser lest it gets rather polluted
* Get list of user's follows
* Show online first, apply grey hue to offline channels? Maybe show last online message
* Add disclaimer for Twitch copyright


# Extras
* Let user favorite channels to put them at the top of the list
* Register service to poll for when a channel is online? [This seems to be an OK approach, check every 5 minutes for any channel that is set to notify, otherwise no job scheduler]
* Add progress spinner in splash screen while checking if account exists

# Very extra
* Add local webserver port to verify if an application wants to provide access? Maybe using a webview..