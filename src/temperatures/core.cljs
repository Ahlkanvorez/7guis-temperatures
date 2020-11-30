(ns temperatures.core
  (:require [reagent.core :as r]
            [reagent.dom :as rd]))

(defmulti to-celsius :unit)
(defmulti to-fahrenheit :unit)

(defmethod to-celsius :C [temperature] temperature)

(defmethod to-fahrenheit :F [temperature] temperature)

(defmethod to-celsius :F [{:keys [value]}]
  {:unit :C :value (/ (- value 32) (/ 9.0 5))})

(defmethod to-fahrenheit :C [{:keys [value]}]
  {:unit :F :value (+ (* value (/ 9.0 5)) 32)})

(def name-for-unit {:F "Fahrenheit" :C "Celsius"})

(defn temperature-view [value unit convert]
  [:div {:style {:display :flex}}
   [:input
    {:type :number
     :value (:value (convert @value))
     :on-change #(reset! value {:unit unit :value (.. % -target -value)})}]
   [:label (name-for-unit unit)]])

(defn temperature-converter [temp]
  [:div {:style {:display :flex :flex-wrap :wrap}}
   [temperature-view temp :F to-fahrenheit]
   [:span {:style {:margin-left "15px" :margin-right "15px"}} "="]
   [temperature-view temp :C to-celsius]])

(defn mount-root []
  (rd/render [temperature-converter (r/atom {:value 100 :unit :F})]
             (js/document.getElementById "app-root")))
