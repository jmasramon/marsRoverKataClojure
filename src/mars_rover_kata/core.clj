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
(def grid-size 10)
(def grid {:len grid-size :hei grid-size})

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
		(if (not (correct-orientation? (get-orient status)))
			false
			true)))

(defn initialize [status]
	(if (check-status status)
		(do (reset! rover-status-atom status)
			"Roger that!")
		"Rejected that!"))

(defn get-status []
	@rover-status-atom)

(defn movement-delta [stp]
	(* ((get-orient @rover-status-atom) directions) 
		(stp movements)))
		
(defn new-pos [fun stp]
	(mod (+ 	(fun @rover-status-atom) 
			(movement-delta stp))
		grid-size))

(defn set-new-pos [get-fun axis stp]
	(swap! rover-status-atom assoc-in [:pos axis] 
		(new-pos get-fun stp)))

(defn new-orientation-index [stp]
	(mod (+ ((get-orient @rover-status-atom) orientations) 
			(stp movements)) 
		turn-modulo-base))

(defn new-orientation [stp]
	(get turns (new-orientation-index stp)))

(defn set-new-orientation [stp]
	(swap! rover-status-atom assoc-in [:orient] (new-orientation stp)))

(defn move [stp]
	(println "stepping" stp)
	(case  #{(get-orient @rover-status-atom) stp}
		(#{:N :f} #{:N :b} #{:S :f} #{:S :b}) 
			(set-new-pos get-y :y stp)
		(#{:E :f} #{:E :b} #{:W :f} #{:W :b}) 
			(set-new-pos get-x :x stp)
		(#{:N :l} #{:N :r} #{:S :l} #{:S :r} #{:W :l} #{:W :r} #{:E :l} #{:E :r}) 
			(set-new-orientation stp)
		))

(defn do-commands [commands]
	(println "commands" commands)
	(doseq [coms commands] (move coms))
	(get-status))











