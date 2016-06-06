// Initialize app and store it to myApp variable for futher access to its methods
var myApp = new Framework7({
    swipePanel: "right",
    showBarsOnPageScrollEnd: false,
    hideNavbarOnPageScroll: true,
    showBarsOnPageScrollTop: false
});

// We need to use custom DOM library, let's save it to $$ variable:
var $$ = Dom7;

var myNg = angular.module("OpenwordsApp", []);

// Add view
var mainView = myApp.addView(".view-main", {
    // Because we want to use dynamic navbar, we need to enable it for this view:
    dynamicNavbar: true,
    domCache: true
});

var stepsUI;
myApp.onPageInit("steps", function(page) {
    stepsUI = myApp.swiper(".swiper-container", {
        pagination: ".swiper-pagination"
    });
});

