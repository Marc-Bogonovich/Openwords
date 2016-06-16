myNg.controller("StepsControl", function($scope, $http) {
    $scope.steps = [];

    $scope.setSteps = function(s) {
        console.log("setSteps");
        $scope.steps = s;
    };
});


