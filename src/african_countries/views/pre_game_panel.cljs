(ns african-countries.views.pre-game-panel
  (:require
   [re-frame.core :as rf]
   [reagent.core :as r]
   [african-countries.subs :as subs]
   [african-countries.events :as events]
   ["evergreen-ui" :refer [Heading Pane IconButton CrossIcon TagInput
                           Spinner majorScale ChevronRightIcon
                           Card Text Button]]))


(defn player-name-card [idx name hovering clicked tag-input-players]
  (let []
    [:> Card {:background (if (= idx @clicked) "greenTint" "teal100")
              :padding 8
              :margin-top 16
              :display "flex"
              :align-items "center"
              :justify-content "center"
              :on-mouse-enter #(rf/dispatch [::events/set-name-hovering idx])
              :on-mouse-leave #(rf/dispatch [::events/set-name-hovering false])
              :elevation (if (= idx @hovering) 2 0)
              :cursor "pointer"
              :on-mouse-down #(reset! clicked idx)
              :on-mouse-up #(reset! clicked false)
              :on-click #(do
                           (rf/dispatch [::events/update-tag-input-players (conj @tag-input-players name)]))}
     [:> Text {:align-items "center"
               :justify-content "center"} name]]))



(defn pre-game-panel []
  (let [tag-input-players (rf/subscribe [::subs/tag-input-players])
        all-players (rf/subscribe [::subs/all-players])
        hovering (rf/subscribe [::subs/name-hovering?])
        clicked (r/atom false)
        db @(rf/subscribe [::subs/db-state])
        game#  @(rf/subscribe [::subs/game-number])]
    (fn []
      (let [remaining-players (clojure.set/difference (set (vals @all-players))
                                                      (set @tag-input-players))
            loading? (nil? @(rf/subscribe [::subs/fb]))
            new-players (clojure.set/difference (set @tag-input-players)
                                                (set (vals @all-players)))]
        [:<>
         [:> Heading {:size 900
                      :margin 40} "Who's Playing?"]
         [:> Pane
          [:> IconButton {:icon CrossIcon
                          :margin 16
                          :display (if (empty? @tag-input-players) "none" nil)
                          :border "none"
                          :height (majorScale 5)
                          :on-click #(rf/dispatch [::events/update-tag-input-players nil])}]

          [:> TagInput {:input-props {:placeholder "Add Player..."}
                        :height (majorScale 5)
                        :values (if (nil? @tag-input-players)
                                  []
                                  @tag-input-players)
                        :on-change #(rf/dispatch [::events/update-tag-input-players %])}]
          [:> IconButton {:icon ChevronRightIcon
                          :margin 16
                          :display (if (empty? @tag-input-players) "none" nil)
                          :border "none"
                          :height (majorScale 5)
                          :on-click #(do
                                       (rf/dispatch [::events/set-active-panel
                                                     :questions-per-player-panel])
                                       (rf/dispatch-sync [::events/add-players new-players])
                                       (rf/dispatch [::events/def-current-players @tag-input-players])
                                       (rf/dispatch [::events/set-current-player (rand-nth (js->clj @tag-input-players))])
                                       (rf/dispatch [::events/set-current-question 0]))}]]

         (cond
           loading?
           [:> Spinner]
           (= @all-players nil) nil
           :else
           [:> Pane {:align-items "left"
                     :margin-top 32}

            (if-not (empty? remaining-players)
              [:> Heading "Previous Players"])
            (doall (map-indexed #(player-name-card %1 %2
                                                   hovering
                                                   clicked
                                                   tag-input-players) remaining-players))])
         ]))))
