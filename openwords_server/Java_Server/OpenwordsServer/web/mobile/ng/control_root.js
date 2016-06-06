myNg.controller("RootControl", function($scope, $http) {
    $scope.logOut = function() {
        mainView.router.load({pageName: "index"});
    };
});


