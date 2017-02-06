
angular.module('detailsApp', ['ngCookies']).
    controller('controller', function ($scope, $http, $cookies, $location) {
        $scope.showModal = false;
        $scope.id = parseInt(location.search.substr(1).split("&")[0].split("=")[1]);
        $scope.product = {};
        $scope.products = JSON.parse($cookies.get('products') || '[]');
        $scope.specialProduct = JSON.parse($cookies.get('specialProduct') || '{}');
        $scope.inCard = JSON.parse($cookies.get('inCard') || '{}');
        $scope.newProduct = JSON.parse($cookies.get('newProduct') || '{}');
        $scope.shoppingCart = JSON.parse($cookies.get('shoppingCart') || '{"products" : [],"total" : 0}');

        $http({
            method: 'GET',
            url: 'http://localhost:8090/products/' + $scope.id
        }).then(function successCallback(response) {
            $scope.product = response.data[0];
            if (!$scope.product)
                window.location.assign("http://localhost:8090" + "/404.html");

        }, function errorCallback(response) {
            window.location.assign("http://localhost:8090" + "/404.html");
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });


        $scope.addToCard = (product) => {
            $scope.inCard[product.id] = true;
            product.isAdded = true;
            $scope.shoppingCart.products.push(product);
            $scope.shoppingCart.total += product.price;
            $cookies.put('shoppingCart', JSON.stringify($scope.shoppingCart));
            $cookies.put('inCard', JSON.stringify($scope.inCard));

        }

        $scope.removeFromCard = (product) => {
            $scope.inCard[product.id] = false;
            $scope.shoppingCart.total -= product.price;
            $.each($scope.shoppingCart.products, function (i) {
                if ($scope.shoppingCart.products[i].id === product.id) {
                    $scope.shoppingCart.products.splice(i, 1);
                    return false;
                }
            });
            $cookies.put('shoppingCart', JSON.stringify($scope.shoppingCart));
            $cookies.put('inCard', JSON.stringify($scope.inCard));
        }

        $scope.payer = () => {


            $cookies.put('shoppingCart', '{"products" : [],"total" : 0}');
            $cookies.put('inCard', '{}');

            $scope.shoppingCart = {
                products: [],
                total: 0
            }
            $scope.inCard = []
            $scope.showModal = false;
        }

        $scope.revenir = () => {
            $scope.showModal = false;
        }


    })