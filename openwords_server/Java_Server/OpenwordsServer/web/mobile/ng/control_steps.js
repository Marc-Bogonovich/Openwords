myNg.controller("StepsControl", function($scope, $http) {
    $scope.lesson;
    $scope.mode;
});

myNg.controller("StepPageControl", function($scope) {
    $scope.myIndex;
    $scope.step;
    $scope.init = function(index) {
        $scope.myIndex = index;
        console.log("hi im " + index);
        console.log("mode " + $scope.mode);
        $scope.step = STEPS[index];

        $scope.step.lines.forEach(function(line) {
            line.forEach(function(item) {
                if (item.type === "ans") {
                    item.text.forEach(function(answer) {
                        $scope.answerPool.push(answer);
                    });
                }
            });
        });

        $scope.step.marplots.forEach(function(group) {
            group.text.forEach(function(mar) {
                $scope.answerPool.push(mar);
            });
        });
    };

    $scope.answerPool = [];
});


