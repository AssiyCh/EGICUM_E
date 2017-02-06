
angular.module('indexApp', ['ngCookies']).
    controller('controller', function ($scope, $http, $cookies) {
        $scope.showModal = false;
        $scope.products = [];
        $scope.specialProduct = {};
        $scope.inCard = JSON.parse($cookies.get('inCard') || '{}');
        $scope.newProduct = {},
            $scope.todayProduct = {},
            $scope.shoppingCart = JSON.parse($cookies.get('shoppingCart') || '{"products" : [],"total" : 0}');
        $http({
            method: 'GET',
            url: 'http://localhost:8090/products'
        }).then(function successCallback(response) {
            $scope.products = response.data;
            $cookies.put('products', JSON.stringify($scope.products));
            $scope.randomProduct();
        }, function errorCallback(response) {
            // called asynchronously if an error occurs
            // or server returns response with an error status.
        });


        $scope.randomProduct = () => {
            let max = $scope.products.length;

            let index = Math.floor(Math.random() * max);
            $scope.specialProduct = $scope.products[index];
            $cookies.put('specialProduct', JSON.stringify($scope.specialProduct));


            index = Math.floor(Math.random() * max);
            $scope.newProduct = $scope.products[index];
            $cookies.put('newProduct', JSON.stringify($scope.newProduct));


            index = Math.floor(Math.random() * max);
            $scope.todayProduct = $scope.products[index];
        }

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