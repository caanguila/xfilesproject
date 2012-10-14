$(document).ready(function () {
	
	/* Images inner shadow effect */
	$('img.shadow').one('load', function () {
		$(this).wrap('<span class="shadow" />').wrap(
			$('<div />', {
				'class': 'shadow',
				'css': {
					'width': $(this).width() + 'px',
					'height': $(this).height() + 'px',
				}
			})
		)
		.before('<img src="img/corner_tl.png" class="tl" />' +
			'<img src="img/corner_tr.png" class="tr" />')
		.after('<img src="img/corner_bl.png" class="bl" />' +
			'<img src="img/corner_br.png" class="br" />')
		.after('<div>&nbsp;</div>')
		.addClass('shadow_complete');
	}).each(function () {
		if (this.complete) $(this).trigger('load');
	});
	
	/* Contact form */
	$('#contact_form').submit(function () {
		$.ajax({
			type: 'POST',
			url: 'contact.php',
			data: {
				name: $('#contact_form input[type=text]').val(),
				email: $("#contact_form input[type=email]").val(),
				text: $("#contact_form textarea").val()
			},
			success: function(data) {
				if ( data == 'sent' ) {
					$('#contact_form .status').slideUp().html('<div class="message_good">E-mail has been sent.</div>').slideDown();
				} else if ( data == 'invalid' ) {
					$('#contact_form .status').slideUp().html('<div class="message_bad">E-mail has not been sent. Check your name, e-mail and message.</div>').slideDown();					
				} else {
					$('#contact_form .status').slideUp().html('<div class="message_bad">E-mail could not be sent.</div>').slideDown();					
				}
			},
			error: function () {
				$('#contact_form .status').html('E-mail could not be sent.').addClass('error').slideDown();
			}
		});
		return false;
	});
	
	/* Nivo Slider */
	$('#nivo_slider').nivoSlider({
        effect: 'random', // Specify sets like: 'fold,fade,sliceDown'
        slices: 15, // For slice animations
        boxCols: 8, // For box animations
        boxRows: 4, // For box animations
        animSpeed: 500, // Slide transition speed
        pauseTime: 3000, // How long each slide will show
        startSlide: 0, // Set starting Slide (0 index)
        directionNav: true, // Next & Prev navigation
        directionNavHide: true, // Only show on hover
        controlNav: true, // 1,2,3... navigation
        controlNavThumbs: false, // Use thumbnails for Control Nav
        controlNavThumbsFromRel: false, // Use image rel for thumbs
        controlNavThumbsSearch: '.jpg', // Replace this with...
        controlNavThumbsReplace: '_thumb.jpg', // ...this in thumb Image src
        keyboardNav: true, // Use left & right arrows
        pauseOnHover: true, // Stop animation while hovering
        manualAdvance: false, // Force manual transitions
        captionOpacity: 0.8, // Universal caption opacity
        prevText: 'Prev', // Prev directionNav text
        nextText: 'Next', // Next directionNav text
        beforeChange: function(){}, // Triggers before a slide transition
        afterChange: function(){}, // Triggers after a slide transition
        slideshowEnd: function(){}, // Triggers after all slides have been shown
        lastSlide: function(){}, // Triggers when last slide is shown
        afterLoad: function(){
			$('#nivo_slider').append('<div class="slider_shadow"></div>');
		} // Triggers when slider has loaded
    });
	
	/* Fancybox */
	$('.fancybox a').fancybox({
		'transitionIn'		: 'elastic',
		'transitionOut'		: 'elastic'
	});
	
	/* Portfolio filter */
	$('#portfolio_filter a').click(function () {
		$('#portfolio_items li').animate({width: 'hide'});
		$('#portfolio_items li.type-' + $(this).attr('data-type')).stop().animate({width: 'show'});
		$('#portfolio_filter li').removeClass('selected');
		$(this).parent('li').addClass('selected');
	});
    
	/* Twitter news stream */
	$('#twitter').tweet({
		username: 'envato',
		loading_text: 'loading tweets...',
		query: null,							// [string]   optional search query
		count: 3,								// [integer]  how many tweets to display?
		retweets: true,							// [boolean]  whether to fetch (official) retweets (not supported in all display modes)
		template: '{text}{time}',   			// [string or function] template used to construct each tweet <li> - see code for available vars
	});
	
	/* Map */
	if (window.markers) {
		$("#map a").attr("href", "http://maps.google.com/maps?q=" + escape(markers));
		$("#map a img.shadow").attr("src", "http://maps.google.com/maps/api/staticmap?markers=" + escape(markers) + "&size=614x294&sensor=false");
		$('#map div.shadow').css({
			'width': 614 + 'px',
			'height': 294 + 'px'
		});
	}
	
	/* Customization */
	
		/* Header pattern */
		$('header')
			.addClass('header_black')
			.css('background-image', 'url(img/patterns/dark_stripes.png)');
			
		/* Showcase pattern */
		$('#showcase')
			.css('background-image', 'url(img/patterns/light_honeycomb.png)');

});
