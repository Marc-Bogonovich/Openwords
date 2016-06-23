myNg.controller("CourseListControl", function($scope, $http) {

    $scope.courseListPack = {
        pageNumber: 1,
        pageSize: 5
    };

    $scope.listCourses = function(page) {
        $scope.courseListPack.pageNumer = page;
        listCourse($scope.courseListPack, $http);
    };

    //load data before page show
    $scope.listCourses(5);

    $scope.goToCourseContent = function(c) {
        getScope("LessonListControl").setCourse(c);
        mainView.router.load({pageName: "course_lessons"});
    };
});


