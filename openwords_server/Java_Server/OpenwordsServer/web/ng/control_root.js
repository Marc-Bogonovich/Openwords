function RootControl($scope, $http) {

    $scope.loadOverview = function() {
        $http({method: "post", url: "overviewOpenwords"}).success(function(data) {
            $scope.langList = data.result;
            $scope.totalWords = $scope.langList[0][1];
            $scope.langList = $scope.langList.slice(1, $scope.langList.length);
        });
    };

    $scope.loadOverview();

    $scope.showDownload = false;
    $scope.password = "";

    $scope.checkLangIdValid = function() {
        if ($scope.langTwoId > 1 && $scope.password.length > 2) {
            $scope.showDownload = true;
        } else {
            $scope.showDownload = false;
        }
    };
    $scope.$watch("langTwoId", function() {
        $scope.checkLangIdValid();
    });
    $scope.$watch("password", function() {
        $scope.checkLangIdValid();
    });

    $scope.doDownload = function() {
        var path = "getConnectionSheet?langTwoId=" + $scope.langTwoId + "&pass=" + $scope.password;
        window.open(path, '', 'width=480,height=320');
    };
}

