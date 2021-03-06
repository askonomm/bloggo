(ns editor.utils
  (:require
   ["sanitize-html" :as sanitize-html]))

(defn block->blocks
  "Takes an input of `blocks`, into which it adds `block`, 
  depending on given `position`. If the `blocks` are empty, 
  it simply returns a vector with just the given `block` in it. 
  Otherwise, it will position it accordingly."
  [blocks block position]
  (if (empty? blocks)
    [block]
    (cond
      ; if position is 0, we want the new block
      ; to become before any other block
      (= position 0)
      (vec (flatten [block blocks]))

      ; if the position is after the last block,
      ; we want the new block to be the new last block.
      (= position (count blocks))
      (vec (flatten [blocks block]))

      ; otherwise simply add the new block to replace
      ; a position of another block, by coming before that.
      :else
      (vec
       (flatten
        (map-indexed
         (fn [index iteration-block]
           (if (= index position)
             [block iteration-block]
             iteration-block)) blocks))))))

(defn block<-blocks
  "Takes an input of `blocks` from which it removes whatever
  block is occupying space on given `index`, and returns the
  result."
  [blocks index]
  (vec
   (remove nil?
           (map-indexed
            (fn [iteration-index iteration-block]
              (when-not (= iteration-index index)
                iteration-block)) blocks))))

(defn parse-html
  "Takes in a raw string of `html`, and then removes all HTML
  elements other than a `a`, `u`, `b`, `i`. It also removes 
  any attributes the HTML elements may have, to make sure that
  when a user pastes content from something that gives along 
  formatting instructions, we'd ignore that."
  [html]
  (sanitize-html
   html
   (clj->js
    {:allowedTags ["b" "strong" "i" "em" "a" "u"]
     :allowedAttributes {"a" ["href"]}})))

(defn string->string
  "Inserts the `inserted-string` into `string` at the given
  index `index`."
  [string inserted-string index]
  (let [split-beginning (subs string 0 index)
        split-end (subs string index)]
    (str split-beginning inserted-string split-end)))

(defn find-by-predicate [predicate collection]
  (first (filter predicate collection)))