(ns clojurewerkz.quartzite.test.jobs-test
  (:refer-clojure :exclude [key])
  (:require [clojure.test :refer [deftest is]]
            [clojurewerkz.quartzite.jobs :as jobs]
            [clojurewerkz.quartzite.conversion :refer [from-job-data to-job-data]])
  (:import (org.quartz Job JobDataMap)))


;;
;; Keys
;;

(deftest test-instantiation-of-keys
  (is (not (= (jobs/key) (jobs/key))))
  (is (not (= (jobs/key "key1") (jobs/key))))
  (is (not (= (jobs/key "key1") (jobs/key "key2"))))
  (is (not (= (jobs/key "key1" "group1") (jobs/key "key1" "group2"))))
  (is (= (jobs/key "key1" "group1") (jobs/key "key1" "group1")))
  (is (= (jobs/key "key1") (jobs/key "key1"))))



;;
;; Builder DSL
;;

(defrecord AJob []
  Job
  (execute [_this _ctx]
    ;; intentional no-op
    ))

(deftest test-job-builder-dsl-example1
  (let [job (jobs/build (jobs/with-identity    "basic.job1" "basic.group1")
                        (jobs/with-description "A description")
                        (jobs/of-type AJob))]
    (is (= (jobs/key "basic.job1" "basic.group1") (.getKey job)))))


(deftest test-job-builder-dsl-example2
  (let [job (jobs/build (jobs/with-identity    "basic.job2" "basic.group2")
                        (jobs/with-description "A description")
                        (jobs/of-type AJob))]
    (is (= "A description" (.getDescription job)))))


(deftest test-job-builder-dsl-example3
  (let [job (jobs/build (jobs/with-identity    "basic.job3" "basic.group3")
                        (jobs/with-description "A description")
                        (jobs/of-type AJob))]
    (.getJobClass job)))

(deftest test-job-builder-dsl-example4
  (let [job (jobs/build (jobs/with-identity    "basic.job4" "basic.group4")
                        (jobs/store-durably)
                        (jobs/request-recovery)
                        (jobs/of-type AJob))]
    (is (.requestsRecovery job))
    (is (.isDurable job))))

(deftest test-job-builder-dsl-example5
  (let [jk  (jobs/key "basic.job5" "basic.group5")
        job (jobs/build (jobs/with-identity jk)
                        (jobs/of-type AJob)
                        (jobs/store-durably))]
    (is (= jk (.getKey job)))))

(deftest test-job-builder-dsl-example6
  (let [jk  (jobs/key "basic.job6")
        job (jobs/build (jobs/with-identity jk)
                        (jobs/of-type AJob)
                        (jobs/store-durably))]
    (is (= jk (.getKey job)))))


;;
;; Clojure <=> JobDataMap conversion
;;

(deftest test-conversion-of-clojure-maps-to-job-data-maps
  (let [input  {:long 100 "string" "Hello, Quartz" :keyword :clojure}
        output (to-job-data input)]
    (is (instance? JobDataMap output))
    (is (= :clojure        (.get output "keyword")))
    (is (= "Hello, Quartz" (.get output "string")))
    (is (= 100             (.get output "long")))))

(deftest test-conversion-of-job-data-maps-to-clojure-maps
  (let [input  (JobDataMap. {"keyword" :clojure "string" "Hello, Quartz" "long" 100})
        output (from-job-data input)]
    (is (map? output))
    (is (= :clojure        (get output "keyword")))
    (is (= "Hello, Quartz" (get output "string")))
    (is (= 100             (get output "long")))))
