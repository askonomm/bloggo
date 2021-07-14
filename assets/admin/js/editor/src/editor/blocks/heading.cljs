(ns editor.blocks.heading
  (:require
   [reagent.core :as r]
   [re-frame.core :refer [dispatch]]))

(defn on-key-press [index event]
  (when (or (= "Enter" (.-key event))
            (= 13 (.-keyCode event)))
    (.preventDefault event)
    (let [id (random-uuid)]
      (dispatch
       [:add-block
        {:position (+ index 1)
         :block {:id id
                 :type :paragraph
                 :content ""}}])
      (dispatch
       [:focus-block
        {:id id
         :where :beginning}]))))

(defn on-change [index height event]
  (dispatch
   [:update-heading-block
    {:position index
     :content (.-value (.-target event))}])
  (reset! height (.-scrollHeight (.-target event))))

(defn block [index block]
  (let [height (r/atom 30)]
    (fn []
      [:textarea
       {:default-value (get block :content)
        :placeholder "Start writing a heading ..."
        :style {:height (str @height "px")}
        :ref (fn [el] (when el (reset! height (.-scrollHeight el))))
        :on-key-press #(on-key-press index %)
        :on-change #(on-change index height %)}])))