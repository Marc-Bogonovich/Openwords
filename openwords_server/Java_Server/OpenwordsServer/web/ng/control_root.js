function RootControl($scope, $http) {

    $scope.loadOverview = function() {
        $http({method: "post", url: "overviewOpenwords"}).success(function(data) {
            $scope.langList = data.result;
            $scope.totalWords = $scope.langList[0][1];
            $scope.langList = $scope.langList.slice(1, $scope.langList.length);
        });
    };

    $scope.loadOverview();
}

