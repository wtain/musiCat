// <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
//new Element("script", {src: "http://code.jquery.com/jquery-1.9.1.js", type: "text/javascript"});

function description_onclick(id) {
	console.log("description_onclick");
	//window.alert("Edit " + id);
	var enable_edit = $("#global_enable_edit").val();
	console.log("enable_edit = " + enable_edit);
	if (!enable_edit)
		return;
	document.getElementById(id + "_div").style = "";
	document.getElementById(id).style = "display: none";
	document.getElementById(id + "_edit").focus();
}

function description_edit_onfocusout(id) {
	console.log("description_edit_onfocusout");
	hideEdit(id);
}

function canceledit_onclick(id) {
	console.log("canceledit_onclick");
	hideEdit(id);
}

function hideEdit(id) {
	console.log("hideEdit");
	document.getElementById(id + "_div").style = "display: none";
	document.getElementById(id).style = "";
}

function remove_onclick(imageid, divid) {
	console.log("remove_onclick");
	
	var url = "/image/remove/" + imageid; 
	
	console.log("Running " + url);
	var posting = $.get(url);

    posting.done(function( data ) {
    	console.log("posting.done");
    	document.getElementById(divid).innerHTML = "";
    });
}

function submit_onclick(id, event) {
	console.log("submit_onclick");
	//window.alert("Edit " + id + "\nEvent = " + event);
	hideEdit(id);
	form_submit(event, id);
}

function form_submit(event, id) {
	console.log("form_submit");
    /* stop form from submitting normally */
    event.preventDefault();

    /* get the action attribute from the <form action=""> element */
    var $form = /*$( this )*/ $('#' + id + "_form"),
        url = $form.attr( 'action' );
    
    var imageId = $('#' + id + "_imageid").val();
    var folderId = $('#' + id + "_folderid").val();
    var description = $('#' + id + "_edit").val();
    var album = $('#' + id + "_album").val();
    
    var albumName = $('#' + id + "_album option:selected").text();
    
    console.log("url = " + url);
    console.log("ImageId = " + imageId);
    console.log("FolderId = " + folderId);
    console.log("description = " + description);
    console.log("album = " + album);
    console.log("albumName = " + albumName);

    /* Send the data using post with element id name and name2*/
    var posting = $.post( url, 
    		{ 
    			url: url,
    			id: imageId, 
    			folder_id: folderId,
    			description: description,
    			album: album
    		} );
    
    //window.alert(posting);

    /* Alerts the results */
    posting.done(function( data ) {
      //alert('success');
    	console.log("posting.done");
    	console.log("success: description = " + description);
    	document.getElementById(id + "_text").innerHTML = description;
    	document.getElementById(id + "_albumtext").innerHTML = albumName;
    	//${cimage.description} = description;
    	//[[${session.address.cimage.description}]] = description;
    	//console.log("document.getElementById(id).text = " + document.getElementById(id).text);
    });
}