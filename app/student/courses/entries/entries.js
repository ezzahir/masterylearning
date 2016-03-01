angular.module('myapp.student.courses.entries', ['ui.router', 'ngSanitize', 'myapp.student.courses.entries.structure'])

    .config (['$stateProvider', function ($stateProvider)
    {
        $stateProvider.state ('home.student.courses.entries', {
            url: '/entries/:entry_id',
            resolve: {
                entry: ['$stateParams', 'database', 'course_id','$log', function ($stateParams, database, course_id, $log)
                {
                    var db_entries = database.get_course (course_id).get_entries();

                    return db_entries[$stateParams.entry_id];
                }],
                entry_id: ['$stateParams', function ($stateParams)
                {
                    return $stateParams.entry_id;
                }]
            },
            templateUrl: 'student/courses/entries/entries.html',
            controller: 'EntriesCtrl',
            role: 'ROLE_STUDENT'
        });
    }])

    .controller ('EntriesCtrl', ['$scope', 'entry', '$log', function ($scope, entry, $log)
    {
        $log.info ('[myApp] EntriesCtrl running');
        if (entry.data.type == 'unit')
            $scope.unit = entry;
        else
            $scope.entry = entry;
    }]);
