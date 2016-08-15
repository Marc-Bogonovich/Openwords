myNg.controller("CourseListControl", function($scope, $http) {

    $scope.listCourses = function(page) {
        $scope.rootAllCourse.page = page;
        listCourse($scope.rootAllCourse, $http);
    };

    //load data before page show
    $scope.listCourses(1);

    $scope.goToCourseContent = function(c) {
        getScope("LessonListControl").setCourse(c);
        mainView.router.load({pageName: "course_lessons"});
    };
});


