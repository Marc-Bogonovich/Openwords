myNg.controller("CourseStudyControl", function($scope, $http) {

    $scope.listMyStudy = function(page) {
        $scope.rootMyStudyList.page = page;
        $scope.rootMyStudyList.userId = userInfo.userId;
        listCourse($scope.rootMyStudyList, $http);
    };

    $scope.courseAction = function(c) {
        getScope("CourseProgressControl").setCourse(c);
        mainView.router.load({pageName: "course_progress"});
    };
});


