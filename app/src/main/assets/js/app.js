var isPost = true;

$("document").ready(function() {
	if(!window.Android) {
		if(isPost) {
			modifyContent();			
		} else {
			modifyCommittee();
		}
	}

	$(window).resize(function() {
		if(isPost) {
			resizeImages();
		}
	});
});

function showContent(content) {
	isPost = true;

	$("#kurc-content").html(content);

	modifyContent();
}

function showCommittee(content) {
	$("#kurc-content").html(content);

	modifyCommittee();
}

function modifyCommittee() {
    isPost = false;

	// Changes multi column table to single column table.
	$("#kurc-content table.tg").find("td").unwrap().wrap($("<tr />"));

	$("#kurc-content table.tg td img").addClass("circle")
									.css("display","block")
									.css("margin-left","auto")
									.css("margin-right","auto");

    $("#kurc-content table.tg td p").css("text-align","center");

	$("#kurc-content table.tg td strong,#kurc-content table.tg td em").css("display","block");
}

function modifyContent() {
	$("#kurc-content table").each(function() {
		$(this).addClass("striped centered");
	});

	$("#kurc-content img").each(function() {
		$(this).addClass("full-width-media");
	});

	$("#kurc-content video").each(function() {
		$(this).addClass("full-width-media responsive-video");
	});

	$("#kurc-content iframe").each(function() {
		$(this).addClass("full-width-media");
	});

	$("#kurc-content a").each(function() {
		$(this).addClass("teal-text text-darken-2");
	});

	$("#kurc-content ul,ol").each(function() {
		$(this).addClass("collection");
	});

	$("#kurc-content li").each(function() {
		$(this).addClass("collection-item");
	});

	resizeMedia();
}

function resizeMedia() {
	$("#kurc-content .full-width-media").each(function() {
		var width = $(this).width();
		var height = $(this).height();

		if($(this).attr("width") && $(this).attr("height")) {
		    width = $(this).attr("width");
		    height = $(this).attr("height");
		}

		var ratio = height / width;

		width = $(window).width();
		height = ratio * width;

		$(this).width(width);
		$(this).height(height);

		$(this).removeAttr("alt");
	});			
}