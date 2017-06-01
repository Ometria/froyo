(ns ometria.froyo.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [ring.swagger.schema :refer [coerce!]]))

(s/defschema FroyoIn
  {:name s/Str
   :size (s/enum :little :classic :large)
   :flavour s/Str
   :toppings [s/Str]})

(s/defschema FroyoOut
  {:name s/Str
   :size (s/enum :little :classic :large)
   :flavour s/Str
   :toppings [s/Str]
   :date_placed s/Str})

(defonce orders (atom ()))

(defonce tz (-> (java.util.TimeZone/getTimeZone "Europe/London")))
(defonce df (doto (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss'Z'Z") (.setTimeZone tz)))

(defn get-orders [] (-> orders deref reverse vec))

(defn add! [new-order]
  (let [order (coerce! FroyoOut (assoc new-order :date_placed (.format df (new java.util.Date))))]
    (swap! orders conj order)
    order))

(defn clear! [] (swap! orders empty))

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Ometria Froyo API"
                    :description "Order Froyo here!"}}}}

    (context "/api" []
      :tags ["api"]

      (GET "/orders" []
        :return [FroyoOut]
        :summary "Lists orders"
        (ok (get-orders)))

      (POST "/order" []
        :return  FroyoOut
        :body    [froyo FroyoIn]
        :summary "Creates a new order"
        (ok (add! froyo)))

      (DELETE "/orders" []
        :return []
        :summary "Clears all orders. *Use with caution!!!*"
        (ok (clear!))))))
