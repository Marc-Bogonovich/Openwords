myNg.controller("CourseProgressControl", function($scope, $http) {
    $scope.setCourse = function(c) {
        $http({
            url: "copyCourse",
            method: "get",
            params: {
                makeTime: c.makeTime,
                userId: userInfo.userId,
                authorId: c.authorId
            }
        }).then(function(res) {
            var r = res.data;
            if (r.errorMessage) {
                myApp.alert(null, "Cannot load course");
                return;
            }
            $scope.course = r.result;
            console.log($scope.course);
        });

    };

    $scope.learnLesson = function(les) {
        STEPS = les.json.steps;
        if (!STEPS[STEPS.length - 1].final) {
            STEPS.push({final: true});
        }

        var StepsControl = getScope("StepsControl");
        StepsControl.lesson = les;
        StepsControl.mode = "exam";
        $$("#back_button_in_steps").once("click", function() {
            mainView.router.load({pageName: "course_progress"});
        });
        mainView.router.load({pageName: "steps"});
    };
});


