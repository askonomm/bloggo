(ns editor.events
  (:require
   [re-frame.core :refer [reg-event-fx reg-event-db]]
   [editor.db :as db]
   [editor.utils :as utils]))

(reg-event-fx
 :initialise-db
 (fn [_ _]
   {:db db/default-db}))

(reg-event-db
 :set-blocks
 (fn [db [_ blocks]]
   (assoc db :blocks blocks)))

(reg-event-db
 :add-block
 (fn [db [_ {:keys [position block]}]]
   (let [blocks (get db :blocks)]
     (assoc db :blocks (utils/block->blocks blocks block position)))))

(reg-event-db
 :delete-block
 (fn [db [_ index]]
   (let [blocks (get db :blocks)]
     (assoc db :blocks (utils/block<-blocks blocks index)))))

(reg-event-db
 :update-paragraph-block
 (fn [db [_ {:keys [position content]}]]
   (let [block (get-in db [:blocks position])
         new-block (assoc block :content content)]
     (assoc-in db [:blocks position] new-block))))

(reg-event-db
 :update-heading-block
 (fn [db [_ {:keys [position content]}]]
   (let [block (get-in db [:blocks position])
         new-block (assoc block :content content)]
     (assoc-in db [:blocks position] new-block))))

