myNg.controller("CourseManagerControl", function($scope, $http, FileUploader) {
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload.php'
    });

    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
    };
    uploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function(item) {
        console.info('onBeforeUploadItem', item);
    };
    uploader.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
    };
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

    console.info('uploader', uploader);

    $scope.createCourse = function() {
        myApp.prompt("Please enter a name for the course", "Creating Course", function(v) {
            var name = v;
            myApp.prompt("Please enter a simple description for the course", "Creating Course", function(v) {
                var comment = v;
                $http({
                    url: "createCourse",
                    method: "get",
                    params: {
                        name: name,
                        userId: userInfo.userId,
                        userName: userInfo.username,
                        comment: comment
                    }
                }).then(function(res) {
                    var r = res.data;
                    if (!r.errorMessage) {
                        myApp.alert(null, "Success");
                        $scope.listMyCourses(1);
                    } else {
                        myApp.alert(null, "Fail");
                    }
                });
            });
        });
    };

    $scope.courseListPack = {
        pageNumber: 1,
        pageSize: 5
    };

    $scope.listMyCourses = function(page) {
        $scope.courseListPack.pageNumer = page;
        listCourse($scope.courseListPack, $http);
    };

});


