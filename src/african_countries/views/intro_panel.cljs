(ns african-countries.views.intro-panel
  (:require [african-countries.subs :as subs]
            [re-frame.core :as rf]
            [african-countries.events :as events]
            ["evergreen-ui" :refer [Heading Button majorScale]]))

(defn intro-panel []
  (let [game-number @(rf/subscribe [::subs/game-number])]
    [:<>
     [:> Heading {:size 900
                  :margin 40} "Is It An African Country???"]
     [:> Button {:on-click #(do
                              (rf/dispatch [::events/increment-game game-number])
                              (rf/dispatch [::events/set-active-panel :pre-game-panel]))
                 :height (majorScale 5)} "Let's Play"]]))
