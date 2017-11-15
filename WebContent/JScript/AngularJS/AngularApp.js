 //= angular.module('application', [ 'numbersOnly' ]);

 var App = angular.module('application', [])
 
 
 .directive('passwordMatch', [function () {
    return {
        restrict: 'A',
        scope:true,
        require: 'ngModel',
        link: function (scope, elem , attrs,control) {
            var checker = function () {
 
                //get the value of the first password
                var e1 = scope.$eval(attrs.ngModel); 
 
                //get the value of the other password  
                var e2 = scope.$eval(attrs.passwordMatch);
                return e1 == e2;
            };
            scope.$watch(checker, function (n) {
 
                //set the form control to valid if both 
                //passwords are the same, else invalid
                control.$setValidity("pwmatch", n);
            });
        }
    };
}])
 
 
 /*
 .directive('numbersOnly', function() {
	
	
	return {
		require : 'ngModel',
		link : function(scope, element, attrs, modelCtrl) {
			modelCtrl.$parsers.push(function(inputValue) {
				// this next if is necessary for when using ng-required on your
				// input.
				// In such cases, when a letter is typed first, this parser will
				// be called
				// again, and the 2nd time, the value will be undefined
				if (inputValue == undefined)
					return ''
				var transformedInput = inputValue.replace(/[^0-9]/g, '');
				if (transformedInput != inputValue) {
					modelCtrl.$setViewValue(transformedInput);
					modelCtrl.$render();
				}

				return transformedInput;
			});
		}
	};
});

function MyCtrl($scope) {
	$scope.number = ''
}*/