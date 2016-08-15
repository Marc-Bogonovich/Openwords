myNg.controller("CourseEditControl", function($scope, $http) {
    $scope.addLesson = function() {
        myApp.popup(".popup-choose-lesson");
        console.log($scope.rootMyLessonList);
    };
});


