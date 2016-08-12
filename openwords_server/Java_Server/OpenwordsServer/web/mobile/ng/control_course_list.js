myNg.controller("CourseListControl", function($scope, $http) {

    $scope.courseListPack = {
        pageNumber: 1,
        pageSize: 10000
    };

    $scope.listCourses = function(page) {
        $scope.courseListPack.page = page;
        listCourse($scope.courseListPack, $http);
    };

    //load data before page show
    $scope.listCourses(1);

    $scope.goToCourseContent = function(c) {
        getScope("LessonListControl").setCourse(c);
        mainView.router.load({pageName: "course_lessons"});
    };
});


