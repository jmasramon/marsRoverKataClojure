(ns mars-rover-kata.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "Jordi's")

(def orientations {:N 0 :E 1 :S 2 :W 3})
(def turns [:N :E :S :W])
(def directions {:N 1 :W -1 :S -1 :E 1})
(def turn-modulo-base 4)
(def orders #{:f :b :l :r})
(def movements {:f 1 :b -1 :l -1 :r 1})
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
	(case  #{(get-orient @rover-status-atom) stp}
		(#{:N :f} #{:N :b} #{:S :f} #{:S :b}) 
			(swap! rover-status-atom assoc-in [:pos :y] 
				(+ (get-y @rover-status-atom) 
					(* ((:orient @rover-status-atom) directions) 
						(stp movements))))
		(#{:E :f} #{:E :b} #{:W :f} #{:W :b}) 
			(swap! rover-status-atom assoc-in [:pos :x] 
				(+ (get-x @rover-status-atom) 
					(* ((:orient @rover-status-atom) directions) 
						(stp movements))))
		(#{:N :l} #{:N :r} #{:S :l} #{:S :r} #{:W :l} #{:W :r} #{:E :l} #{:E :r}) 
			(swap! rover-status-atom assoc-in [:orient] 
					(get turns (mod (+ ((get-orient @rover-status-atom) orientations) (stp movements)) turn-modulo-base)))
		))

(defn do-commands [commands]
	(println "commands" commands)
	(doseq [coms commands] (move coms))
	(get-status))










