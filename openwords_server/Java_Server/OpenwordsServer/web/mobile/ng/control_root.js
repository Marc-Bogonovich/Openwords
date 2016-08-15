myNg.controller("RootControl", function($scope, $http, $compile) {
    $scope.logOut = function() {
        mainView.router.load({pageName: "index"});
    };

    $scope.rootMyLessonList = {
        page: 1,
        pageSize: 10000
    };

    $scope.rootMyCourseList = {
        page: 1,
        pageSize: 10000
    };

    $scope.rootAllCourse = {
        page: 1,
        pageSize: 10000
    };

});


