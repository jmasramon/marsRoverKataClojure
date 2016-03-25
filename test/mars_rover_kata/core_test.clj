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
    (is (= {:pos {:x 0 :y 1} :orient :N} (do-commands '(:f))))))

; (deftest move-backwards
;   (testing "Move backwards."
;   	(initialize {:pos {:x 0 :y 1} :orient :N})
;     (is (= {:pos {:x 0 :y 0} :orient :N} (do-commands '(:b))))))









