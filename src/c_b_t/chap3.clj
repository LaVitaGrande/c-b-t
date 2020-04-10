(ns c-b-t.chap3
  (:require [clojure.string])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  []
  (print "Hello, chap3"))

(def failed-names
  ["Bob Potter" "Bob the Explorer" "The incredible Blob" "eugene the yeeterest"])

(def hash-test {:a 1 :b 2 :c 3 :d 4})

(def hash2 {:a 1 :b 2 :c 3 :sub {:z 26 :y 25} :vec [0 1 2 3]})

; demonstration of defn, name/arguents/doc-string/function 
(defn too-enthusiastic
  "Return a cheer that might be a bit too enthusiastic"
  [name]
  (str "OH. MY. GOD! " name " YOU ARE MOST DEFINITELY LIKE THE BEST "
       "MAN SLASH WOMAN EVER I LOVE YOU AND WE SHOULD RUN AWAY SOMEWHERE"))

; demonstration of variable arity.  Notice defn can call itself
(defn x-chop
  "Describe the kind of chop you're inflicting on someone"
  ([name chop-type]
   (str "I " chop-type " chop " name "! Take that!"))
  ([name]
   (x-chop name "karate")))

(defn codger-communication
  [whippersnapper]
  (str "Get off my lawn, " whippersnapper "!!!"))

; demonstration of rest operator.  In this case assigning "the rest" of the
; arguements to whippersnappers and then mapping coder-com to that list
(defn codger
  [& whippersnappers]
  (map codger-communication whippersnappers))

; rest operaor needs to come last, followed by var it is being assigned to
(defn favorite-things
  [name & things]
  (str "Hi, " name ", here are my favorite things: "
       (clojure.string/join ", " things)))


;;; destructuring examples
;; Return the first element of a collection
(defn my-first
  [[first-thing]] ; Notice that first-thing is within a vector
  first-thing)

;; Return the second element of a collection.
(defn my-second
  [[_ second-thing]] ; Notice that first-thing is within a vector
  second-thing)

;; more complex version using rest parameter
(defn chooser
  [[first-choice second-choice & unimportant-choices]]
  (println (str "Your first choice is: " first-choice))
  (println (str "Your second choice is: " second-choice))
  (println (str "We're ignoring the rest of your choices. "
                "Here they are in case you need to cry over them: "
                (clojure.string/join ", " unimportant-choices))))

; destructuring a map - note this gets the value :lat from the map and assigns
; it to lat and gets the value of :lng from the map and assigns it to lng
; it DOES NOT pass the map, just the values assigned to the keys at those locations
(defn announce-treasure-location
  [{lat :lat lng :lng}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

; destructuring with the :keys keyword does the same thing if you keep
; the names
(defn a-t-l
  [{:keys [lat lng]}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng)))

; can be combined with :as keyword that provides full access to the original map
(defn r-t-l
  [{:keys [lat lng] :as treasure-location}]
  (println (str "Treasure lat: " lat))
  (println (str "Treasure lng: " lng))
  (println (str "Treasure height: " (:height treasure-location))))

; (r-t-l  {:lat 28.22 :lng 81.33 :height 10})

; functions default to returning last form evaluted
(defn illustrative-function
  "Complicated way to return the string joe"
  []
  (+ 1 304)
  30
  "joe")

; annonymous function is created with fn
(map (fn [name] (str "Hi, " name))
     ["Darth Vader" "Mr. Magoo"])

; in fact defn is just a macro for (def NAME (fn [arguments] (body)))
; above should be the same as
(defn hi-er [name] (str "Hi, " name))
(def high-er (fn [name] (str "Higher, " name)))
(map hi-er ["Darth Vader" "Mr. Magoo"])

; #(BODY WITH % AS ARGUMENT) also creates anon function 
(map #(str "Hi, " %) ["Darth Vader" "Mr. Magoo"])

; multiple arity anon funcs can use %1 %2 %3 for the multiple args
(#(str %1 " and " %2) "cornbread" "butter beans")

; and rest with %&.  Identity just returns a unchanged list of its arguements
(#(identity %&) 1 "blarg" :yip)

; demonstration of returning function created dynamically through anon func
; returns function that increments by passed value
(defn inc-maker
  "Create a custom incrementor"
  [inc-by]
  #(+ % inc-by))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

(def dalmatian-list
  ["Pongo" "Perdita" "Puppy 1" "Puppy 2"])

(let [dalmatians (take 2 dalmatian-list)] 
  dalmatians)

(def x 0)

(let [x (inc x)] x)

(let [[pongo & dalmatians] dalmatian-list]
  [pongo dalmatians])

;; compare loop recur to func recure below it
(loop [iteration 0]
  (println (str "Iteration " iteration))
  (if (> iteration 3)
    (println "Goodbye!")
    (recur (inc iteration))))

;; func recur compared to loop recur above
(defn recursive-printer
  ([]
   (recursive-printer 0))
  ([iteration]
   (println iteration)
   (if (> iteration 3)
     (println "Goodbye!")
     (recursive-printer (inc iteration)))))

(recursive-printer)

;; #" " is a regex form - returns matching part
 (re-find #"pla" "platypus")

(reduce + [1 2 3 4 5])

;; replaces loop recur with reduce
;; uses anonymous function 
(defn better-symmetrize-body-parts
  "Expects a seq of maps that have a :name and :size"
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
     (into final-body-parts (set [part (matching-part part)])))
   []
   asym-body-parts))

