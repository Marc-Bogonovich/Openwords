myNg.controller("RootControl", function($scope, $http, $httpParamSerializerJQLike) {
    $scope.logOut = function() {
        mainView.router.load({pageName: "index"});
        myApp.alert("Don't forget to add this web app to your phone's home screen!", "See you^_^");
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
        pageSize: 10000,
        all: true
    };

    $scope.rootMyStudyList = {
        page: 1,
        pageSize: 10000
    };

    $scope.goToCourseStudy = function() {
        getScope("CourseStudyControl").listMyStudy(1);
        mainView.router.load({pageName: "course_study"});
    };

    $scope.refreshCourseList = function() {
        listCourse($scope.rootAllCourse, $http);
    };

    $scope.rootTargetCourse = {
        target: null
    };
    $scope.addLessonFromPool = function(les) {
        if ($scope.rootTargetCourse.target) {
            $scope.rootTargetCourse.target.json.lessons.push(JSON.parse(angular.toJson(les)));
        }
    };

    $scope.saveCourse = function() {
        if (!$scope.rootTargetCourse.target.name) {
            myApp.alert(null, "Course must have a name!");
            return;
        }
        $scope.rootTargetCourse.target.content = angular.toJson($scope.rootTargetCourse.target.json);
        $scope.rootTargetCourse.target.json = null;
        $http({
            url: "saveCourse",
            method: "post",
            headers: {"Content-Type": "application/x-www-form-urlencoded; charset=UTF-8"},
            data: $httpParamSerializerJQLike({
                course: angular.toJson($scope.rootTargetCourse.target)
            })
        }).then(function(res) {
            if (res.data.errorMessage) {
                myApp.alert(null, "Save failed");
            } else {
                myApp.alert(null, "Save successful");
                $scope.rootTargetCourse.target = null;

                getScope("CourseManagerControl").listMyCourses(1);
                getScope("CourseListControl").listCourses(1);

                mainView.router.load({pageName: "course_manager"});
            }
        });
    };

    $scope.goToLessonManager = function() {
        mainView.router.load({pageName: "lesson_manager"});
    };
});


