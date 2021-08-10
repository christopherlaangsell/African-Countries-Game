(ns african-countries.views.tie-break-panel
  (:require
   [re-frame.core :as rf]
   [african-countries.subs :as subs]
   [african-countries.events :as events]
   [african-countries.data :as data]
   ["evergreen-ui" :refer [TextInput Card Text Pane Heading Button]]
))


(defn tie-break-question-card [country-data remaining-players current-playoff-player]
  (let [tie-break-input @(rf/subscribe [::subs/tie-break-input])]
    [:> Card {:elevation 2
              :width 400
              :padding 16
              :height 240
              :display "flex"
              :justify-content "center"
              :align-items "center"
              :flex-direction "column"
              :background "white"
              :margin 24}
     [:> Text {:size "900"
               :padding-bottom 16} (str "What is the population of " (:name country-data) "?")]
     [:> TextInput {:value tie-break-input
                    :on-change #(rf/dispatch [::events/set-tb-input (.. % -target -value)])}]
     [:> Button {:margin 16
                 :appearance "primary"
                 :intent "none"
                 :on-click #(if (empty? remaining-players)
                              (do (rf/dispatch [::events/set-playoff-answer
                                                {:user-answer (clojure.string/replace tie-break-input "," "")
                                                 :correct-answer (:pop country-data)
                                                 :country (:name country-data)}])
                                  (rf/dispatch [::events/set-active-panel
                                                :post-game-panel]))
                              (do
                                (rf/dispatch [::events/set-playoff-answer
                                              {:user-answer (clojure.string/replace tie-break-input "," "")
                                               :correct-answer (:pop country-data)
                                               :country (:name country-data)}])
                                (rf/dispatch [::events/set-prev-playoff-player current-playoff-player])
                                (rf/dispatch [::events/set-current-player (first remaining-players)])
                                (rf/dispatch [::events/clear-tie-break-input])))}
      "Submit"]]))

(defn tie-break-panel []
  (let [playoff-players (set @(rf/subscribe [::subs/playoff-players]))
        current-playoff-player @(rf/subscribe [::subs/current-player])
        previous-playoff-players @(rf/subscribe [::subs/prev-playoff-players])
        remaining-players (clojure.set/difference (set (map :name  (vals playoff-players)))
                                                  (set (list (:name current-playoff-player)))
                                                  (set (vals previous-playoff-players)))
        playoff-questions @(rf/subscribe [::subs/playoff-questions])
        country-data (nth playoff-questions (count remaining-players))]
    [:<>
     [:> Pane {:display "flex"
               :flex-direction "column"
               :align-items "center"}
      [:> Heading {:margin 40
                   :size 900} (str "Question for " (:name current-playoff-player) ":")]
      [tie-break-question-card country-data remaining-players current-playoff-player]]]))
