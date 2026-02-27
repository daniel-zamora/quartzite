(ns clojurewerkz.quartzite.test.matchers-test
  (:require [clojurewerkz.quartzite.jobs :as j]
            [clojure.test :refer [deftest is testing are]]
            [clojurewerkz.quartzite.matchers :as matchers]))


(deftest test-group-matcher-factory-functions
  (testing "group-equals"
    (let [g (matchers/group-equals "abc")]
      (is (.isMatch g (j/key "job1" "abc")))
      (is (not (.isMatch g (j/key "job1" "group2"))))
      (is (not (.isMatch g (j/key "job1"))))
      (is (.isMatch g (j/key "job99" "abc")))))
  (testing "group-starts-with"
    (let [g (matchers/group-starts-with "abc")]
      (is (.isMatch g (j/key "job1" "abcdef")))
      (is (.isMatch g (j/key "job99" "abcdef")))
      (is (not (.isMatch g (j/key "job1" "group2"))))
      (is (not (.isMatch g (j/key "job1"))))))
  (testing "group-ends-with"
    (let [g (matchers/group-ends-with "def")]
      (is (.isMatch g (j/key "job1" "abcdef")))
      (is (.isMatch g (j/key "job99" "abcdef")))
      (is (not (.isMatch g (j/key "job1" "group2"))))
      (is (not (.isMatch g (j/key "job1"))))))
  (testing "group-contains"
    (let [g (matchers/group-contains "bc")]
      (are [key] (is (matchers/match? g key))
        (j/key "job1" "abcdef")
        (j/key "job1" "bcolumbia")
        (j/key "job1" "abc"))
      (are [key] (is (not (matchers/match? g key)))
        (j/key "job1" "def")
        (j/key "job888")
        (j/key "job1" "generation.invoices")))))
