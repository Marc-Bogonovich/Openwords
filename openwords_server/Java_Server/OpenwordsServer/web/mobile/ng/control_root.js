myNg.controller("RootControl", function($scope, $http, $compile) {
    $scope.logOut = function() {
        mainView.router.load({pageName: "index"});
    };
});


