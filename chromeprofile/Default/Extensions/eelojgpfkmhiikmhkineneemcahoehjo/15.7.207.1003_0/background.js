//////////////////////////////////////////////////////////////////////////////////
// Symantec copyright header start
//////////////////////////////////////////////////////////////////////////////////
//
//////////////////////////////////////////////////////////////////////////////
//Copyright (c) 2021 Broadcom. All Rights Reserved.
//The term "Broadcom" refers to Broadcom Inc. and/or its subsidiaries.
//
//THIS SOFTWARE CONTAINS CONFIDENTIAL INFORMATION AND TRADE SECRETS OF
//BROADCOM. USE, DISCLOSURE OR REPRODUCTION IS PROHIBITED WITHOUT THE PRIOR
//EXPRESS WRITTEN PERMISSION OF BROADCOM.
//
//The Licensed Software and Documentation are deemed to be commercial computer
//software as defined in FAR 12.212 and subject to restricted rights as
//defined in FAR Section 52.227-19 "Commercial Computer Software - Restricted
//Rights" and DFARS 227.7202, Rights in "Commercial Computer Software or
//Commercial Computer Software Documentation," as applicable, and any successor
//regulations, whether delivered by Broadcom as on premises or hosted services.
//Any use, modification, reproduction release, performance, display or
//disclosure of the Licensed Software and Documentation by the U.S. Government
//shall be solely in accordance with the terms of this Agreement.
//////////////////////////////////////////////////////////////////////////////

//
//////////////////////////////////////////////////////////////////////////////////
// Symantec copyright header stop
//////////////////////////////////////////////////////////////////////////////////

var port_ = null;

var classObject = {

	initializeHost: function()
	{
		if (port_ === null)
		{
			port_ = chrome.runtime.connectNative('com.symantec.dlp');

			port_.onMessage.addListener(function(response) {
					   if (chrome.runtime.lastError) {
							 console.log("ERROR: " + chrome.runtime.lastError.message);
					   } else {
							console.log("Received: " +  JSON.stringify(response));
					 }
			  });
		}

		port_.onDisconnect.addListener(function() {
		  port_ = null;
		  console.log("Disconnected");
		  setTimeout(function(){
		  classObject.initializeHost ();
		  }, 15000);
		});
	},

	onActiveTab: function (activeInfo)
	{
		chrome.tabs.get (activeInfo.tabId, function (tab) {
			console.log ("ACTIVE_URL: " + tab.url);
			port_.postMessage ({"ACTIVE_URL" : tab.url});
		})
	},
	
	onUpdateTab: function (tabId, changeInfo, tab)
	{
		chrome.windows.get (tab.windowId, { populate: true }, function (window) {
			if (window.focused)
			{
				console.log ("tab.active :");
				console.log (tab.active);
				if (changeInfo && changeInfo.url && tab.active)
				{
					console.log ("url changed.......");

					console.log (changeInfo.url);
					port_.postMessage ({"ACTIVE_URL" : changeInfo.url});
				}
			}
		})
	},
	
	onTabRemoved: function (tabsId, removeInfo)
	{
		console.log ("removeInfo.isWindowClosing " + removeInfo.isWindowClosing);
		if (removeInfo.isWindowClosing == false)
		{
			// Loop over all windows and their tabs
			var urls = [];
			chrome.windows.getAll({ populate: true }, function(windowList) {
			
				for (var i = 0; i < windowList.length; i++) 
				{
					for (var j = 0; j < windowList[i].tabs.length; j++) 
					{
						urls.push(windowList[i].tabs[j].url);
					}
				}

				console.log (urls);
				port_.postMessage ({"RUNNING_URLS" : urls});
			})
		}
	},
	
	onWindowRemoved: function (windowId)
	{
		console.log ("onWindowRemoved " + windowId);
		// Loop over all windows and their tabs
		var urls = [];
		chrome.windows.getAll({ populate: true }, function(windowList) {
		
			for (var i = 0; i < windowList.length; i++) 
			{
				for (var j = 0; j < windowList[i].tabs.length; j++) 
				{
					urls.push(windowList[i].tabs[j].url);
				}
			}

			console.log (urls);
			port_.postMessage ({"RUNNING_URLS" : urls});
		})
	},
	
	onWindowActive: function (windowId)
	{
		try {
			chrome.windows.getCurrent({populate : true }, function (window) {
				try {
					console.log (window.focused);
					if (window.focused == false)
					{
						chrome.extension.isAllowedIncognitoAccess (function (isAllowedAccess) {
							if (!isAllowedAccess)
							{
								console.log ({"ACTIVE_URL" : "Unknown"});
								port_.postMessage ({"ACTIVE_URL" : "Unknown"});
							}
						})
					}
				} catch (err) {
					console.log("chrome.windows.getCurrent returned undefined windowId");
					console.log ({"ACTIVE_URL" : "Unknown"});
					port_.postMessage ({"ACTIVE_URL" : "Unknown"});
				}
			})
		} catch (err) {
			console.log("chrome.windows.getCurrent failed");
			console.log ({"ACTIVE_URL" : "Unknown"});
			port_.postMessage ({"ACTIVE_URL" : "Unknown"});
		}

		
		chrome.windows.get (windowId, {populate : true }, function (window) {
			try {
				console.log (window);
				for (var i=0; i<window.tabs.length; i++)
				{
					if (window.tabs[i].active)
					{
						console.log ({"ACTIVE_URL" : window.tabs[i].url});
						port_.postMessage ({"ACTIVE_URL" : window.tabs[i].url});
					}
				}
			} catch (err) {
				console.log ("Failed to get chrome.windows.get due to undefined windowId");
			}
		})
	},
	
	onDisabled: function(info)
	{
		//chrome.management.setEnabled("befafhehepoikdmllhkbndipoeifhbgm", true, null);
		console.log (info.id + " disabled");
	},
	
	onUninstalled: function(id)
	{
		//chrome.management.launchApp("befafhehepoikdmllhkbndipoeifhbgm");
		console.log ("extension with id uninstalled" + id); 
	},
	
	onMessageFromCS: function (event)
	{
		console.log("Got event from CS");
		port_.postMessage(event);
	}
};

// Kick things off.
classObject.initializeHost();

chrome.management.onDisabled.addListener(
	classObject.onDisabled
);

chrome.management.onUninstalled.addListener(
	 classObject.onUninstalled
);

chrome.tabs.onActivated.addListener(
	classObject.onActiveTab
);

chrome.tabs.onUpdated.addListener(
	classObject.onUpdateTab
);

chrome.tabs.onRemoved.addListener(
	 classObject.onTabRemoved
);

chrome.windows.onRemoved.addListener(
	classObject.onWindowRemoved
);

chrome.windows.onFocusChanged.addListener(
	classObject.onWindowActive
);

chrome.runtime.onMessage.addListener(
	classObject.onMessageFromCS
);
