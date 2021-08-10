(ns african-countries.views.post-game-panel
  (:require
   [re-frame.core :as rf]
   [african-countries.subs :as subs]
   [african-countries.events :as events]
   [african-countries.data :as data]
   ["evergreen-ui" :refer [Card Text Pane Heading Button]]
))


(defn int->str [int]
  (let [int-str (if (int? int)
                  (str int)
                  int)
        num-split (rest (clojure.string/split int-str #""))
        len-num (count num-split)
        rmdr (mod len-num 3)
        indexes (map #(+ rmdr %) (range 0 12 3))]
    (apply str
           (map-indexed (fn [idx val] (if (and (not (zero? idx))
                                               (some #(= idx %) indexes))
                                        (str "," val)
                                        val)) num-split))))

(defn player-result-card [name answer-points]
  (let [num-questions (js/parseInt
                    @(rf/subscribe [::subs/number-of-questions]))
        score (int (* 100 (/ answer-points num-questions)))]
    [:> Card {:elevation 2
              :display "flex"
              :flex-direction "column"
              :align-items "center"
              :margin 16
              :padding 32
              :background (cond
                            (> score 70) "green200"
                            (> score 30) nil
                            :else "red300")

              :justify-content "center"}
     [:> Heading {:size 500} name]
     [:> Text {:size 700} (str score "%")]]))

(defn playoff-result-card [{:keys [name country correct-answer deviation user-answer]} winner?]
  [:> Card {:elevation 2
            :display "flex"
            :flex-direction "column"
            :align-items "center"
            :margin 16
            :padding 32
            :background (if winner?
                          "#ffef99"
                          "blue50")
            :justify-content "center"}
   [:> Heading {:size (if winner? 900 500)} name]
   [:> Text {:size 700} (str deviation "% off")]
   [:> Text {:size 500} (str "Country: " country)]
   [:> Text {:size 500} (str "Correct Population: " (int->str correct-answer))]
   [:> Text {:size 500} (str "Your guess: " (int->str user-answer))]])

(defn post-game-panel []
  (let [players @(rf/subscribe [::subs/current-players])
        name-score-maps-list (vals players)
        sorted-scores (reverse (sort-by :answer-points name-score-maps-list))
        winning-score (:answer-points (first sorted-scores))
        winners (reduce #(if (= (:answer-points %2) winning-score) (conj %1 (:name %2)) %1) '() sorted-scores)
        playoff-players @(rf/subscribe [::subs/playoff-players])
        name-answer-maps-list-playoff (vals playoff-players)
        calc-data (fn [{:keys [name answer]}]
                    (let [{:keys [correct-answer user-answer country]} answer
                          answer-dif (- (int user-answer) correct-answer)
                          abs-answer-dif (max answer-dif (* -1 answer-dif))]
                      {:deviation (int (* 100 (/ abs-answer-dif correct-answer)))
                       :user-answer user-answer
                       :name name
                       :correct-answer correct-answer
                       :country country}))
        country-data-vec (take (count winners) (repeatedly #(rand-nth data/african-countries)))]
    [:> Pane {:display "flex"
              :align-items "center"
              :height "100%"
              :flex-direction "column"}
     (if (and (= (:answer-points (first sorted-scores)) (:answer-points (second sorted-scores)))
              (nil? playoff-players))
       [:> Pane
        [:> Heading {:size 900
                     :margin 56} "There's a Tie!"]
        [:> Pane {:display "flex"
                  :align-items "center"
                  :flex-direction "column"}
         [:> Text {:size 900} "Between:"]
         [:> Heading {:size 700
                      :margin 40}
          (if (> (count winners) 2)
            (str (apply str (map #(str % ", ") (butlast winners)))
                 "and"
                 (last winners))
            (str (first winners) " and " (second winners)))]]
        [:> Pane {:display "flex"
                  :flex-direction "row"
                  :margin 32}
         [:> Button {:height 56
                     :appearance "primary"
                     :intent "none"
                     :width "100%"
                     :on-click #(do
                                  (rf/dispatch [::events/set-active-panel
                                                :tie-break-panel])
                                  (rf/dispatch [::events/set-playoff-players winners])
                                  (rf/dispatch [::events/load-playoff-questions country-data-vec])
                                  (rf/dispatch [::events/set-current-player (first winners)]))} "Tie Break Anyone?"]]]

       [:> Pane
        [:> Heading {:size 900
                             :margin 56} "The Winner Is:"]
         (if name-answer-maps-list-playoff
           (let [answer-maps (map #(calc-data %) name-answer-maps-list-playoff)
                 sorted-answer-maps (sort-by :deviation answer-maps)
                 deviations (map :deviation sorted-answer-maps)
                 tie?  (= (first deviations) (second deviations))
                 winner-playoff (first sorted-answer-maps)]
             (if tie?
               [:> Heading {:size 900} "There's a tie and the chance of this happening is so slim I didn't build anything for it!"]

               [playoff-result-card winner-playoff true]))

           [:> Card {:elevation 2
                     :display "flex"
                     :flex-direction "column"
                     :align-items "center"
                     :margin 16
                     :padding 32
                     :justifyContent="center"
                     :background "#ffef99"
                     :border="default"}
            [:> Heading {:size 900} (first winners)]])])

     [:> Heading {:size 700
                  :margin-top 56} "Final Results"]
     [:> Pane {:display "flex"
               :flex-direction "column"}
      (when name-answer-maps-list-playoff
        (let [answer-maps (map #(calc-data %) name-answer-maps-list-playoff)
              sorted-answer-maps (sort-by :deviation answer-maps)
              non-winners (rest sorted-answer-maps)]
          [:> Pane {:display "flex"
                    :align-items "center"
                    :flex-direction "column"}
           [:> Text {:margin-top 16}"Tie Breaker"]
           [:> Pane {:display "flex"
                     :flex-direction "row"}
            (doall (map #(playoff-result-card % false) non-winners))
            ]]))

      [:> Pane {:display "flex"
                :align-items "center"
                :flex-direction "column"}
       (if name-answer-maps-list-playoff
         [:> Text {:margin-top 32
                   :margin-bottom 16} "Regular Round"])
       [:> Pane {:display "flex"
                 :flex-direction "row"}
        (doall (map #(player-result-card (:name %) (:answer-points %)) name-score-maps-list))]]]]))
