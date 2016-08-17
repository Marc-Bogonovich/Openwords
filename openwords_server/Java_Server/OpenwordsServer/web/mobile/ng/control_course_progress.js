myNg.controller("CourseProgressControl", function($scope, $http) {
    $scope.setCourse = function(c) {
        $scope.course = c;
        console.log(c);
    };

    $scope.learnLesson = function(les) {
        STEPS = les.json.steps;
        STEPS.push({lines: [], marplots: []});
        var StepsControl = getScope("StepsControl");
        StepsControl.lesson = les;
        StepsControl.mode = "exam";
        $$("#back_button_in_steps").once("click", function() {
            mainView.router.load({pageName: "course_progress"});
        });
        mainView.router.load({pageName: "steps"});
    };
});


