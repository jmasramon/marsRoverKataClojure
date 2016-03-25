(ns mars-rover-kata.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "Jordi's")

(def orientations #{:N :S :E :W})
(def orders #{:f :b :l :r})
(def grid {:len 10 :hei 10})

(def rover-status-atom (atom {:pos {:x 0 :y 0} :orient :N}))

(defn get-x [status] 
	(get-in status [:pos :x]))

(defn get-y [status] 
	(get-in status [:pos :y]))

(defn get-orient [status] 
	(:orient status))

(defn correct-position? [position]
	(if (and 
		(< (:x position) (:len grid))
		(< (:y position) (:hei grid)))
			true
			false))

(defn correct-orientation? [orientation]
	(if (orientation orientations)
		true
		false))

(defn check-status [status]
	(if (not (correct-position? (:pos status)))
		false
		(if (not (correct-orientation? (:orient status)))
			false
			true)))

(defn initialize [status]
	(if (check-status status)
		(do 
			(reset! rover-status-atom status)
			"Roger that!")
		"Rejected that!"))

(defn get-status []
	@rover-status-atom)

(defn move [stp]
	(println "stepping" stp)
	(case (list (:orient @rover-status-atom) stp)
		'(:N :f) (do
			(println "Oriented north go f")
			(swap! rover-status-atom assoc-in [:pos :y] 1))))
		; (:N :b) (do
		; 	(println "Oriented north go b")
		; 	(swap! rover-status-atom assoc-in [:pos :y] 0))))

(defn do-commands [commands]
	(println "commands" commands)
	(doseq [coms commands] (move coms))
	(get-status))









