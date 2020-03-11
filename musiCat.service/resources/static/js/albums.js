function showAddAlbumForm() {
	showElement("addAlbumForm");
}

function hideAddAlbumForm() {
	hideElement("addAlbumForm");
}

function el(id) {
	return document.getElementById(id);
}

function showElement(elementId) {
	el(elementId).style = "";
}

function hideElement(elementId) {
	el(elementId).style = "display: none";
}

function setVisibility(id, visible) {
	if (visible)
		showElement(id);
	else
		hideElement(id);
}

function displayMessage(id, message, delay) {
	console.log("displayMessage");
	$("#" + id).html(message); 
	$("#" + id).fadeIn().delay(delay).fadeOut();
}

function addalbum_submit_onclick(event) {
	console.log("addalbum_submit_onclick");	
	event.preventDefault();
	
	/* get the action attribute from the <form action=""> element */
    var $form = /*$( this )*/ $("#create_album_form"),
        url = $form.attr( 'action' );
//    
/*    var $form = $( this ),
    	url = $form.attr( 'action' );*/
    
    //console.log("$form = " + $form);
    
    //var imageId = $('#' + id + "_imageid").val();
    //var folderId = $('#' + id + "_folderid").val();
    //var description = $('#' + id + "_edit").val();
    
    var name = document.getElementById( 'create_album_form_name' ).value;
    var description = document.getElementById( 'create_album_form_description' ).value;

    
    console.log("url = " + url);
    console.log("name = " + name);
    console.log("description = " + description);
    
    
    /* Send the data using post with element id name and name2*/
    var posting = $.post( url, 
    		{ 
    			url: url,
    			name: name,
    			description: description
    		} );

    /* Alerts the results */
    posting.done(function( data ) {
    	console.log("posting.done");
    	displayMessage("message", "Added!", 300);
    	//albums.add();
    	retrieveAlbums();
    });
}

function retrieveAlbums() {
	console.log("retrieveAlbums");
    //var url = '/album/api/list';
	var url = '/album/updatelist';

    $("#resultsBlock").load(url);
}

function removealbum_onclick(id) {
	console.log("removealbum_onclick");
	
	var url = "/album/remove/" + id;
    var posting = $.get(url);

    posting.done(function( data ) {
    	console.log("posting.done");
    	displayMessage("message", "Removed!", 300);
    	retrieveAlbums();
    });
}

function setEditMode(id, isEditMode) {
	setVisibility("album_name_static_" + id, !isEditMode);
	setVisibility("album_description_static_" + id, !isEditMode);
	setVisibility("album_name_edit_" + id, isEditMode);
	setVisibility("album_description_edit_" + id, isEditMode);
	setVisibility("album_button_remove_" + id, !isEditMode);
	setVisibility("album_button_edit_" + id, !isEditMode);
	setVisibility("album_button_update_" + id, isEditMode);
	setVisibility("album_button_canceledit_" + id, isEditMode);
}

function editalbum_onclick(id) {
	console.log("editalbum_onclick");
	setEditMode(id, true);
	
	el("album_name_edit_" + id).value = el("album_name_static_" + id).innerHTML;
	el("album_description_edit_" + id).value = el("album_description_static_" + id).innerHTML;
}

function updatealbum_onclick(id) {
	console.log("updatealbum_onclick");
	
	setEditMode(id, false);
	
	var url = "/album/update/";
    var posting = $.post(url,
    		{
    			id: id,
    			name: el("album_name_edit_" + id).value,
    			description: el("album_description_edit_" + id).value
    		});

    posting.done(function( data ) {
    	console.log("posting.done");
    	displayMessage("message", "Updated!", 300);
    	retrieveAlbums();
    });
}

function canceleditalbum_onclick(id) {
	console.log("canceleditalbum_onclick");
	setEditMode(id, false);
}