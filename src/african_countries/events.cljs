(ns african-countries.events
  (:require
   [re-frame.core :as rf :refer [reg-fx]]
   [african-countries.db :as db]
   [african-countries.subs :as subs]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   [clojure.string :as str]
   ["firebase/app" :default firebase]
   ["firebase/database"]
   ))

(rf/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))



(defn- db-ref [path]
  (.ref (. firebase database) (str/join "/" path)))

(defn- firebase-store [path value]
  (.set (db-ref path) (clj->js value)))

(reg-fx
 :store-in-firebase
 (fn [[path value]]
   (firebase-store path value)))

(reg-fx
 :multi-store-in-firebase
 (fn [path-value-pairs]
   (doseq [[path value] path-value-pairs]
     (firebase-store path value))))

(rf/reg-event-fx
    ::clear-game-fb
  (fn [{:keys [db]} _]
    {:store-in-firebase [["current-game"] nil]}))

(rf/reg-event-fx
    ::add-players
  (fn [{:keys [db]} [_ new-players-set]]
    (let [uid #(str "uid-" (random-uuid))
          player-pairs (map #(vector ["players" (uid)] %) new-players-set)
          player-names (map #(second (first %)) player-pairs)
          player-uids (map second player-pairs)
          db-update-map (zipmap player-uids player-names)]

      (if-not (empty? player-pairs)
        {:db (update-in db [:firebase-data :players] merge db-update-map)
         :multi-store-in-firebase player-pairs}))))

(defn get-uid [name db]
  (let [player-data @(rf/subscribe [::subs/all-players])
        correct-player (filter (comp #{name} player-data) (keys player-data))]
    (first correct-player)))

(rf/reg-event-fx
    ::def-current-players
  (fn [{:keys [db]} [_ all-players]]
    (let [players (js->clj all-players)
          player-pairs (map #(vector ["current-game" "current-players" (name (get-uid % db)) "name"] %) players)]
      {:multi-store-in-firebase player-pairs})))


(rf/reg-event-fx
    ::set-active-panel
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :active-panel data)
     :store-in-firebase [["current-game" "active-panel"] data]}))

(rf/reg-event-fx
    ::load-active-panel
  (fn [{:keys [db]} [_ data]]
    {:db (assoc db :active-panel data)}))

(rf/reg-event-db
    ::set-name-hovering
  (fn [db [_ data]]
    (assoc db :name-hovering? data)))

(rf/reg-event-fx
    ::set-current-player
  (fn [{:keys [db]} [_ name]]
    {:store-in-firebase [["current-game" "current-player"] {:name name :uid (get-uid name db)}]}))

(rf/reg-event-fx
    ::set-current-question
  (fn [{:keys [db]} [_ number]]
    {:store-in-firebase [["current-game" "current-question"] number]}))


(rf/reg-event-fx
    ::add-answer
  (fn [{:keys [db]} [_ data]]
    (let [current-player @(rf/subscribe [::subs/current-player])]
      {:store-in-firebase [["current-game" "current-players" (:uid current-player) "answer-points"] data] })))

(rf/reg-event-db
    ::load-data
  (fn [db [_ data]]
    (assoc db :firebase-data data)))

(rf/reg-event-fx
    ::set-prev-player
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "previous-players" (:uid data)] (:name data)]}))

(rf/reg-event-fx
    ::add-prev-q-result
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "previous-question"] data]}))

(rf/reg-event-fx
    ::set-playoff-players
  (fn [{:keys [db]} [_ names]]
    (let [player-pairs (map #(vector ["current-game" "playoff-players" (name (get-uid % db)) "name"] %) names)]
      {:multi-store-in-firebase player-pairs})))

(rf/reg-event-fx
    ::set-tb-input
  (fn [{:keys [db]} [_ data]]
    {:db (assoc-in db [:firebase-data :current-game :tie-break-input] data)
     :store-in-firebase [["current-game" "tie-break-input"] data]}))

(rf/reg-event-fx
    ::set-playoff-answer
  (fn [{:keys [db]} [_ data]]
    (let [current-player @(rf/subscribe [::subs/current-player])]
      {:store-in-firebase [["current-game"
                            "playoff-players"
                            (:uid current-player)
                            "answer"] data]})))


(rf/reg-event-fx
    ::set-prev-playoff-player
  (fn [{:keys [db]} [_ data]]
    (let [ ]
      {:store-in-firebase [["current-game" "previous-playoff-players" (:uid data)] (:name data)]})))

(rf/reg-event-fx
    ::clear-tie-break-input
  (fn [{:keys [db]} _]
    {:db (assoc-in db [:firebase-data :current-game :tie-break-input] "")
     :store-in-firebase [["current-game" "tie-break-input"] ""]}))

(rf/reg-event-fx
    ::increment-game
  (fn [{:keys [db]} [_ number]]
    {:store-in-firebase [["game-number"] (+ 1 number)]}))

(rf/reg-event-fx
    ::set-african-country
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "african-country"] data]}))

(rf/reg-event-fx
    ::update-tag-input-players
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "pre-game" "tag-input-players"] data]}))

(rf/reg-event-fx
    ::player-ready?
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "mid-game" "player-ready"] data]}))

(rf/reg-event-fx
    ::load-questions
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "mid-game" "questions"] data]}))

(rf/reg-event-fx
    ::add-answer-choice
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "mid-game" "prev-answer-choice"] data]}))

(rf/reg-event-fx
    ::set-number-of-questions
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "pre-game" "number-of-questions"] data]}))

(rf/reg-event-fx
    ::load-playoff-questions
  (fn [{:keys [db]} [_ data]]
    {:store-in-firebase [["current-game" "playoff-questions"] data]}))
