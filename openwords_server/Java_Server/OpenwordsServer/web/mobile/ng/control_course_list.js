myNg.controller("CourseListControl", function($scope, $http) {
    $scope.course = {
        pageNumber: 1
    };

    $scope.listCourses = function(page) {
        console.log("listCourses: " + page);
        $scope.course.list = [];

        for (var i = 1; i <= page; i++) {
            $scope.course.list.push({
                name: "Chinese Go",
                fileCover: "img/test" + i + ".jpg",
                updated: "Posted on January 21, 2015",
                comment: "very nice",
                courseId: i
            });
        }
    };

    //load data before page show
    $scope.listCourses(4);

    $scope.goToCourseContent = function(c) {
        getScope("LessonListControl").setCourse(c);
        mainView.router.load({pageName: "course_lessons"});
    };

    $scope.test = function() {
        for (var i = 5; i <= 9; i++) {
            $scope.course.list.push({
                name: "Chinese Go",
                fileCover: "img/test" + i + ".jpg",
                updated: "Posted on January 21, 2015",
                comment: "very nice",
                courseId: i
            });
        }
    };
});


