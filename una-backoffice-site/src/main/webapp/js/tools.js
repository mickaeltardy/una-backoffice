var openEditor = function() {

	$("#editorContainer").fadeIn(
			"500",
			function() {
				var lTmpClass = ($("#page").attr("class")) ? $("#page").attr(
						"class") : "";
				$("#page").prop("data-class-tmp", lTmpClass)
				$("#page").attr("class",
						$("#page").attr("class") + " openedEditor");
				$("#editorContainer").css("display", "block");
			});
}

var closeEditor = function() {
	$("#editorContainer").fadeOut("500", function() {
		$("#page").attr("class", $("#page").prop("data-class-tmp"));
		$("#editorContainer").css("display", "none");
	});
}

var openPreloader = function() {
	$("#preloader").css("top", $(window).scrollTop() + 50 + "px");
	$("#preloaderContainer").fadeIn(
			"500",
			function() {
				$("#preloaderContainer").css("height",
						$("#page").outerHeight() + "px");

				$("#preloaderContainer").css("display", "block");

			});
}

var closePreloader = function() {
	$("#preloaderContainer").fadeOut("500", function() {
		$("#preloaderContainer").css("display", "none");
	});

}

var openMenu = function() {
	$("#nav-panel").slideDown("1000", function() {
		$(this).css("display", "block")
	})
}

var closeMenu = function() {
	$("#nav-panel").slideUp("1000", function() {
		$(this).css("display", "none")
	})
}