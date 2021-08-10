(ns african-countries.views
  (:require
   [re-frame.core :as rf]
   [african-countries.views.intro-panel :refer [intro-panel]]
   [african-countries.views.pre-game-panel :refer [pre-game-panel]]
   [african-countries.views.mid-game-panel :refer [mid-game-panel]]
   [african-countries.views.post-game-panel :refer [post-game-panel]]
   [african-countries.views.tie-break-panel :refer [tie-break-panel]]
   [african-countries.views.questions-per-player-panel :refer
    [questions-per-player-panel]]
   [african-countries.subs :as subs]
   [african-countries.events :as events]
   [cljs.core.async :refer [>! go timeout]]
   [reagent.core :as r]
   ["evergreen-ui" :refer [Spinner Pane Button]] ))

(defn main-panel []
  (let [active-panel (rf/subscribe [::subs/active-panel])
        previous-question-res (rf/subscribe [::subs/prev-question-result])]

    (fn []
      (let [background-color (cond
                               (nil? @previous-question-res) nil
                               @previous-question-res "green200"
                               :else "red300")
            _ (if (not (nil? @previous-question-res))
                (go
                  (<! (timeout 500))
                  (rf/dispatch [::events/add-prev-q-result nil])))]

        [:> Pane {:display "flex"
                  :align-items "center"
                  :flex-direction "column"
                  :height "100vh"
                  :width "100%"
                  :background background-color
                  :justify-content "center"}

         (if-not (or (= @active-panel :intro-panel) (nil? @active-panel))
           [:> Button {:margin-top 16
                       :on-click #(do
                                    (rf/dispatch-sync [::events/clear-game-fb])
                                    (rf/dispatch [::events/set-active-panel :intro-panel]))}
            "Restart Game"])

         (case @active-panel
           nil [:> Spinner]
           :intro-panel [intro-panel]
           :pre-game-panel [pre-game-panel]
           :questions-per-player-panel [questions-per-player-panel]
           :mid-game-panel [mid-game-panel]
           :post-game-panel [post-game-panel]
           :tie-break-panel [tie-break-panel])]))))


