myNg.controller("LessonListControl", function($scope, $http) {
    $scope.lessons = [];

    $scope.setCourse = function(c) {
        $scope.lessons = [1, 2, 3, "c" + c.courseId];
        console.log(c);
    };

    $scope.goToSteps = function(l) {
        console.log("lesson " + l);
        var steps = [];
        for (var i = 0; i < l; i++) {
            steps.push(i);
        }
        STEPS = steps;
        mainView.router.load({pageName: "steps"});

    };
});


