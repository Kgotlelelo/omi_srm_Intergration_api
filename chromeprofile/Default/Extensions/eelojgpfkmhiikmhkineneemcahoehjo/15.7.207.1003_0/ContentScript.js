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


function wait(delayTime)
{
	var start = new Date().getTime();
	while (new Date().getTime() < start + delayTime);
}

function GetURL()
{
	return window.location.href
}

function SendPrintOperationDetailsToAgent(PrintContent)
{
	var array = {};
	array["URL"] = GetURL();
	array["PRINT_CONTENT"] = PrintContent;
	
	chrome.runtime.sendMessage({"PRINT_OPERATION":array});
	console.log("Print operation details sent for url : " + array["URL"]);
	wait(500);
}

var counter = 0;

(function() 
{
    var beforePrint = function() 
	{
		console.log('beforePrint called');
	
		if(counter == 0)
		{
			var x = document.querySelectorAll("body");
			var bodyHtmlTextContent = '';
		
			for (i = 0; i < x.length; i++) 
			{
					
				bodyHtmlTextContent += x[i].outerHTML;
					
			}
		
			SendPrintOperationDetailsToAgent(bodyHtmlTextContent);
			
			counter++;
		}
    
	};
	
    var afterPrint = function() 
	{		
		counter = 0; 
    };

    if (window.matchMedia) 
	{
        var mediaQueryList = window.matchMedia('print');
        mediaQueryList.addListener(function(mql) 
		{
            if (mql.matches) 
			{
				beforePrint();
            } else 
			{
                afterPrint();
            }
        });
    }

    window.onbeforeprint = beforePrint;
    window.onafterprint = afterPrint;
}());