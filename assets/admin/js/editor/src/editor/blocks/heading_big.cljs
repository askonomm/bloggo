(ns editor.blocks.heading-big
  (:require [reagent.core :as r]
            [re-frame.core :refer [dispatch]]))

(defn on-key-press [event]
  (if (or (= "Enter" (.-key event))
          (= 13 (.-keyCode event)))
    (.preventDefault event)
    (prn "enter was not pressed")))

(defn on-change [index height event]
  (dispatch [:update-heading-block
             {:position index
              :content (.-value (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn block [index block]
  (let [height (r/atom 30)]
    (fn []
      [:textarea
       {:default-value (get block :content)
        :style {:height (str @height "px")}
        :ref (fn [el]
               (when el
                 (reset! height (.-scrollHeight el))))
        :on-key-press #(on-key-press %)
        :on-change #(on-change index height %)}])))