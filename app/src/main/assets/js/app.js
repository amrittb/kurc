function showContent(content) {
	$("#kurc-content").html(content);

	modifyContent();
}

function modifyContent() {
	$("#kurc-content table").each(function() {
		$(this).addClass("striped centered");
	});

	resizeImages();

	$("#kurc-content video").each(function() {
		$(this).addClass("responsive-video");
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
}

function resizeImages() {
	$("#kurc-content img").each(function() {
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

$("document").ready(function() {
	if(!window.Android) {
		modifyContent();
	}

	$(window).resize(function() {
		resizeImages();
	});
});