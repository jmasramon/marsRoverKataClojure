(ns mars-rover-kata.core-test
  (:require [clojure.test :refer :all]
            [mars-rover-kata.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= 1 1))))

(deftest correct-position?-test
  (testing "correct-position?"
    (is (correct-position? {:x 0 :y 0}))
    (is (correct-position? {:x 9 :y 0}))
    (is (correct-position? {:x 0 :y 9}))
    (is (not (correct-position? {:x 10 :y 9})))))

(deftest correct-orientation?-test
  (testing "correct-orientation?"
    (is (correct-orientation? :S))
    (is (correct-orientation? :N))
    (is (correct-orientation? :W))
    (is (correct-orientation? :E))
    (is (not (correct-orientation? :K)))))

(deftest initialization
  (testing "Initializing the rover."
    (is (= "Roger that!" (initialize {:pos {:x 0 :y 0} :orient :N})))))

(deftest getting-status
	(testing "Getting statatus.")
	(is (= {:pos {:x 0 :y 0} :orient :N} (get-status)))
	(is (not (= {:pos {:x 1 :y 0} :orient :N} (get-status)))))

(deftest initialization-bis
  (testing "Initializing the rover to a diff status."
    (is (= "Roger that!" (initialize {:pos {:x 1 :y 1} :orient :S})))
    (is (= {:pos {:x 1 :y 1} :orient :S} (get-status)))))

(deftest move-forwards
  (testing "Move forwards."
  	(initialize {:pos {:x 0 :y 0} :orient :N})
    (is (= {:pos {:x 0 :y 1} :orient :N} (do-commands '(:f))))
    (initialize {:pos {:x 0 :y 0} :orient :S})
    (is (= {:pos {:x 0 :y 9} :orient :S} (do-commands '(:f))))))

(deftest move-backwards
  (testing "Move backwards."
  	(initialize {:pos {:x 0 :y 1} :orient :N})
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands '(:b))))
  	(initialize {:pos {:x 0 :y 1} :orient :S})
    (is (= {:pos {:x 0 :y 2} :orient :S} (do-commands '(:b))))))

(deftest turn-left
  (testing "Turn left."
  	(initialize {:pos {:x 0 :y 0} :orient :N})
    (is (= {:pos {:x 0 :y 0} :orient :W} (do-commands '(:l))))
    (is (= {:pos {:x 0 :y 0} :orient :S} (do-commands '(:l))))
    (is (= {:pos {:x 0 :y 0} :orient :E} (do-commands '(:l))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands '(:l))))
))

(deftest turn-right
  (testing "Turn right."
  	(initialize {:pos {:x 0 :y 0} :orient :N})
    (is (= {:pos {:x 0 :y 0} :orient :E} (do-commands '(:r))))
    (is (= {:pos {:x 0 :y 0} :orient :S} (do-commands '(:r))))
    (is (= {:pos {:x 0 :y 0} :orient :W} (do-commands '(:r))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands '(:r))))
))

(deftest mixed-orders
	(testing "Mixed string of orders.")
  	(initialize {:pos {:x 0 :y 0} :orient :N})
    (is (= {:pos {:x 1 :y 0} :orient :S} (do-commands '(:f :r :f :r :f))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands '(:b :l :b :l :b))))
    (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands '(:f :b :b :f))))
)  

(deftest wrapping
	(testing "Wrapping to an spheric planet.")
	(initialize {:pos {:x 0 :y 0} :orient :N})
    (is (= {:pos {:x 1 :y 9} :orient :N} (do-commands '(:f :b :r :f :l :b))))
    (is (= {:pos {:x 9 :y 9} :orient :W} (do-commands '(:l :f :f))))
)








