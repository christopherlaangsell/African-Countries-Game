(ns african-countries.views.mid-game-panel
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [african-countries.subs :as subs]
   [african-countries.events :as events]
   [african-countries.data :as data]
   [cljs.core.async :refer [>! go timeout]]
   ["evergreen-ui" :refer [Card majorScale Badge Spinner Pane Heading Button Text]]))

(defn choosing-algorithm []
  (if (< 0.7 (rand))
    {:name (:name (rand-nth data/african-countries)) :country true}
    {:name (rand-nth data/fake-countries) :country false}))

(defn end-turn [remaining-players]
  (let [current-player @(rf/subscribe [::subs/current-player])]
    (if (empty? remaining-players)
      (rf/dispatch [::events/set-active-panel :post-game-panel])
      (do
        (rf/dispatch [::events/player-ready? false])
        (rf/dispatch [::events/set-current-question 0])
        (rf/dispatch [::events/set-prev-player current-player])
        (rf/dispatch [::events/set-current-player (first remaining-players)])))))

(defn score-component [{:keys [answer-points name]}]
  (let [number-of-questions @(rf/subscribe [::subs/number-of-questions])]
    [:> Text {:size 500
              :margin 16} (str name ": " answer-points "/" number-of-questions)]))

(defn question-card [country-data current-question score
                     remaining-players]
  (let [_ (rf/dispatch [::events/set-african-country (:name country-data)])
        number-of-questions (js/parseInt
                             @(rf/subscribe [::subs/number-of-questions]))
        last-question (- number-of-questions 1)
        answer-choice @(rf/subscribe [::subs/answer-choice])
        no-button-active? (if (false? answer-choice)
                            true
                            nil)
        yes-button-active? (if (true? answer-choice)
                             true
                             nil)
        _ (if (not (nil? answer-choice))
            (go
              (<! (timeout 500))
              (rf/dispatch [::events/add-answer-choice nil])))]

    [:> Card {:elevation (if (<= @current-question 4)
                           @current-question
                           4)
              :width 400
              :height 240
              :background "white"
              :margin 24}
     [:> Pane {:height (majorScale 2)}
      (let [score% (int (* 100 (/ score  @current-question)))]
        [:> Badge {:float "left"
                   :margin-top 16
                   :margin-left 16
                   :color (cond
                            (zero? @current-question) "neutral"
                            (> score% 70) "green"
                            (> score% 30) "neutral"
                            :else "red")}
         (if (zero? @current-question)
           "First Question!"
           (str score%  "%"))])]

     [:> Pane {:float "center"
               :display "flex"
               :justify-content "center"
               :margin-top 32
               :align-items "center"
               :flex-direction "column"}
      (let [country-name @(rf/subscribe [::subs/get-african-country])]
        [:> Text {:size "900"
                  :padding 16} (str "Is " country-name " an African country?")])
      [:> Pane {:margin-top 32}
       (let [points (if (:country country-data) 0 1)]
         [:> Button {:intent "danger"
                     :margin-right 8
                     :is-active no-button-active?
                     :on-click #(do
                                  (rf/dispatch [::events/add-prev-q-result
                                                (if (= points 1)
                                                  true
                                                  false)])
                                  (rf/dispatch [::events/add-answer-choice false])
                                  (rf/dispatch [::events/add-answer (+ score points)])
                                  (if (= last-question @current-question)
                                    (end-turn remaining-players)
                                    (rf/dispatch [::events/set-current-question (inc @current-question)])))
                     } "Definitely Not"])

       (let [points (if (:country country-data) 1 0)]
         [:> Button {:intent "success"
                     :margin-left 8
                     :is-active yes-button-active?
                     :on-click #(do
                                  (rf/dispatch [::events/add-prev-q-result
                                                (if (= points 1)
                                                  true
                                                  false)])
                                  (rf/dispatch [::events/add-answer-choice true])
                                  (rf/dispatch [::events/add-answer (+ score points)])
                                  (if (= last-question @current-question)
                                    (end-turn remaining-players)
                                    (rf/dispatch [::events/set-current-question (inc @current-question)])))
                     } "Yes!"])]]]))

(defn mid-game-panel []
  (let [_ (rf/dispatch [::events/player-ready? false])
        number-of-questions (js/parseInt
                             @(rf/subscribe [::subs/number-of-questions]))]
    (fn []
      (let [db @(rf/subscribe [::subs/db-state])
            current-players @(rf/subscribe [::subs/current-players])
            current-player @(rf/subscribe [::subs/current-player])
            previous-players @(rf/subscribe [::subs/prev-players])
            current-question (rf/subscribe [::subs/current-question])
            remaining-players (clojure.set/difference (set (map :name  (vals current-players)))
                                                      (set (list (:name current-player)))
                                                      (set (vals previous-players)))
            score (or @(rf/subscribe [::subs/answer-points]) 0)
            ready? @(rf/subscribe [::subs/player-ready?])
            questions (rf/subscribe [::subs/questions])]

        (if (some nil? (list db current-players current-player @current-question remaining-players ready?))
          [:> Spinner]
          [:<>
           [:> Pane {:display "flex"
                     :justify-content "center"
                     :align-items "center"
                     :flex-direction "column"}
            (let [point-maps (filter #(not (nil? (:answer-points %))) (vals current-players))]
              (if (not (empty? point-maps))
                [:> Pane {:elevation "4"
                          :float "left"
                          :margin 24
                          :display "flex"
                          :justify-content "center"
                          :align-items "center"
                          :flex-direction "row"}
                 (map #(score-component %) point-maps)]))
            [:> Heading {:margin-top 32
                         :size 800}
             (str "Questions for " (:name current-player))]
            (if (and (= false ready?) (zero? @current-question))
              [:> Button {:on-click #(do
                                       (rf/dispatch [::events/player-ready? true])
                                       (rf/dispatch [::events/load-questions (take number-of-questions (repeatedly choosing-algorithm))]))
                          :margin-top 40
                          :appearance "primary"
                          :intent "success"
                          :height 56} "Ready??"]
              [question-card (nth @questions @current-question) current-question score
               remaining-players])]])))) )
