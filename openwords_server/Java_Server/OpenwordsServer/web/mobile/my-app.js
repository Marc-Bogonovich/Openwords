// Initialize app and store it to myApp variable for futher access to its methods
var myApp = new Framework7({
    swipePanel: "right",
    showBarsOnPageScrollEnd: false,
    showBarsOnPageScrollTop: false,
    hideNavbarOnPageScroll: true,
    swipeBackPage: false
});

// We need to use custom DOM library, let's save it to $$ variable:
var $$ = Dom7;

var myNg = angular.module("OpenwordsApp", ["angularFileUpload"]);

// Add view
var mainView = myApp.addView(".view-main", {
    // Because we want to use dynamic navbar, we need to enable it for this view:
    dynamicNavbar: true,
    domCache: true
});

function getScope(id) {
    return angular.element(document.getElementById(id)).scope();
}

var userInfo;

myApp.onPageInit("course_list", function(page) {
    console.log("course_list init");
});

myApp.onPageReinit("course_list", function(page) {
    console.log("course_list re");
});

var STEPS;
var stepsUI = null;
myApp.onPageInit("steps", function(page) {
    console.log("steps init");
    stepsUI = myApp.swiper(".swiper-container", {
        pagination: ".swiper-pagination"
    });

    $$.ajax({
        url: "slide.html",
        success: function(data) {
            for (var i = 0; i < STEPS.length; i++) {
                stepsUI.appendSlide(data.replace("{{step}}", STEPS[i].toString()));
            }
        }
    });
});

myApp.onPageReinit("steps", function(page) {
    console.log("steps re");
    stepsUI.removeAllSlides();
    $$.ajax({
        url: "slide.html",
        success: function(data) {
            for (var i = 0; i < STEPS.length; i++) {
                stepsUI.appendSlide(data.replace("{{step}}", STEPS[i].toString()));
            }
        }
    });
});

myApp.onPageReinit("course_lessons", function(page) {
    console.log("course_lessons");
    var scope = getScope("LessonListControl");
});
