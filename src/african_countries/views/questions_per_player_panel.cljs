(ns african-countries.views.questions-per-player-panel
  (:require
   [re-frame.core :as rf]
   [african-countries.subs :as subs]
   [african-countries.events :as events]
   ["evergreen-ui" :refer [IconButton CrossIcon TagInput
                           majorScale ChevronRightIcon
                           Heading TextInputField Pane]]))

(defn questions-per-player-panel []
  (let [input-val (rf/subscribe [::subs/number-of-questions])
        valid-input? (or (and (>= @input-val 1) (<= @input-val 20))
                         (nil? @input-val)
                         (= @input-val ""))]
    [:<>
     [:> Heading {:size 900
                  :margin 40} "How Many Questions Per Player?"]
     [:> Pane {:display "flex"
               :flex-direction "row"
               :align-content "center"}
      [:> IconButton {:icon CrossIcon
                      :margin-right 16
                      :display (if (empty? @input-val) "none" nil)
                      :margin-top 8
                      :border "none"
                      :height (majorScale 5)
                      :on-click #(rf/dispatch [::events/set-number-of-questions ""])}]
      [:> TextInputField {:placeholder "E.g. 5"
                          :input-height (majorScale 5)
                          :value @input-val
                          :is-invalid (not valid-input?)
                          :validation-message (if (not valid-input?)
                                                "Must be between 1 and 20")
                          :on-change #(rf/dispatch [::events/set-number-of-questions (.. % -target -value)])}]
      [:> IconButton {:icon ChevronRightIcon
                      :margin-left 16
                      :margin-top 8
                      :display (if (or (not valid-input?)
                                       (empty? @input-val)) "none" nil)
                      :border "none"
                      :height (majorScale 5)
                      :on-click #(rf/dispatch [::events/set-active-panel
                                               :mid-game-panel])}]]]))
