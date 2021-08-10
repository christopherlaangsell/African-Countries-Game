(ns african-countries.core
  (:require
   [reagent.dom :as rdom]
   [clojure.string :as str]
   [re-frame.core :as rf]
   [african-countries.events :as events]
   [african-countries.views :as views]
   [african-countries.config :as config]
   [african-countries.data :as data]
   ["firebase/app" :default firebase]
   ["firebase/database"]
   ))

(defn firebase-init []
  (if (zero? (alength (. firebase -apps)))
    (. firebase initializeApp
     (clj->js
      {:apiKey "AIzaSyAqnW14YdLn7D2nYFvCuZq6VmeCRI-MHmo"
       :authDomain "africa-game-cbd56.firebaseapp.com"
       :projectId "africa-game-cbd56"
       :storageBucket "africa-game-cbd56.appspot.com"
       :messagingSenderId "1058694828092"
       :appId "1:1058694828092:web:ff4890ffec301cb5473339"
       :measurementId "G-8F0JRX5B5S"
       :databaseURL "https://africa-game-cbd56-default-rtdb.firebaseio.com/"
       }))))




(defn retrieve-all-data
  []
  (.. firebase database (ref "/")
      (on "value"
          (fn [^js/DataSnapshot snapshot]
            (let [data (-> snapshot .val (js->clj :keywordize-keys true))]
              (rf/dispatch [::events/load-data data]))))))

(defn retrieve-active-panel
  []
  (.. firebase database (ref "current-game/active-panel")
      (on "value"
          (fn [^js/DataSnapshot snapshot]
            (let [data (-> snapshot .val (js->clj :keywordize-keys true))
                  active-panel (if (nil? data)
                                 :intro-panel
                                 (keyword data))]
              (rf/dispatch [::events/load-active-panel active-panel]))))))

(defn dev-setup []
  (when config/debug?
    (println "dev mode")))

(defn ^:dev/after-load mount-root []
  (rf/clear-subscription-cache!)
  (let [root-el (.getElementById js/document "app")]
    (rdom/unmount-component-at-node root-el)
    (rdom/render [views/main-panel] root-el)))


(defn init []
  (rf/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (firebase-init)
  (retrieve-all-data)
  (retrieve-active-panel)
  (mount-root))
