(ns african-countries.subs
  (:require
   [re-frame.core :as rf]))


(rf/reg-sub ::fb
  (fn [db _]
    (:firebase-data db)))

(rf/reg-sub ::game-number
  :<- [::fb]
  (fn [db _]
    (:game-number db)))

(rf/reg-sub ::active-panel
  (fn [db]
    (:active-panel db)))

(rf/reg-sub ::all-players
  :<- [::fb]
  (fn [db _]
    (:players db)))

(rf/reg-sub ::db-state
  (fn [db]
    db))

(rf/reg-sub ::name-hovering?
  (fn [db]
    (:name-hovering? db)))

(rf/reg-sub ::current-players
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :current-players])))

(rf/reg-sub ::playoff-players
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :playoff-players])))

(rf/reg-sub ::current-player
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :current-player])))

(rf/reg-sub ::current-question
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :current-question])))

(rf/reg-sub ::answer-points
  :<- [::fb]
  (fn [db _]
    (let [current-uid (:uid @(rf/subscribe [::current-player]))
          uid (keyword current-uid)]
      (get-in db [:current-game :current-players uid :answer-points]))))

(rf/reg-sub ::prev-players
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :previous-players])))

(rf/reg-sub ::prev-question-result
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :previous-question])))

(rf/reg-sub ::tie-break-input
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :tie-break-input])))

(rf/reg-sub ::prev-playoff-players
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :previous-playoff-players])))

(rf/reg-sub ::get-african-country
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :african-country])))

(rf/reg-sub ::tag-input-players
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :pre-game :tag-input-players])))

(rf/reg-sub ::player-ready?
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :mid-game :player-ready])))

(rf/reg-sub ::questions
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :mid-game :questions])))

(rf/reg-sub ::answer-choice
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :mid-game :prev-answer-choice])))

(rf/reg-sub ::number-of-questions
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :pre-game :number-of-questions])))

(rf/reg-sub ::playoff-questions
  :<- [::fb]
  (fn [db _]
    (get-in db [:current-game :playoff-questions])))
