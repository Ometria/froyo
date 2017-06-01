(ns ometria.froyo.core-test
  (:require [cheshire.core :as cheshire]
            [clojure.test :refer :all]
            [ometria.froyo.handler :refer :all]
            [ring.mock.request :as mock]
            [clj-json.core :as json]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(deftest a-test

  (testing "Test GET request to /orders returns empty array"
           (let [response (app (-> (mock/request :get "/api/orders")))
                 body     (parse-body (:body response))]
             (is (= (:status response) 200))
             (is (= body []))))

  (testing "Test POST request to /order creates new order"
           (let [req-json {:name "Hello", :size "little", :flavour "red", :toppings ["red"]}
                 response (app (-> (mock/request :post "/api/order" (json/generate-string req-json))
                                   (mock/content-type "application/json")))
                 body     (parse-body (:body response))]
             (is (= (:status response) 200))
             (is (= body (assoc req-json :date_placed (body :date_placed))))))

  (testing "Test GET request to /orders shows the new order"
           (let [req-json {:name "Hello", :size "little", :flavour "red", :toppings ["red"]}
                 response (app (-> (mock/request :get "/api/orders")))
                 body     (parse-body (:body response))]
             (is (= (:status response) 200))
             (is (= (count body) 1))
             (is (= (first body) (assoc req-json :date_placed (-> body first :date_placed))))))

  (testing "Test DELETE request to /orders deletes all orders"
           (let [response (app (-> (mock/request :delete "/api/orders")))
                 body     (parse-body (:body response))]
             (is (= (:status response) 200))
             (is (= body []))))

  (testing "Test GET request to /orders returns empty array (2)"
           (let [response (app (-> (mock/request :get "/api/orders")))
                 body     (parse-body (:body response))]
             (is (= (:status response) 200))
             (is (= body []))))
  )
