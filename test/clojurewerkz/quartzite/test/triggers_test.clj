(ns clojurewerkz.quartzite.test.triggers-test
  (:refer-clojure :exclude [key])
  (:require [clojurewerkz.quartzite.jobs :as jobs]
            [clojure.test :refer [deftest is]]
            [clojurewerkz.quartzite.conversion :refer [from-job-data to-job-data]]
            [clojurewerkz.quartzite.triggers :as triggers])
  (:import (java.time Instant)
           (java.time.temporal ChronoUnit)
           (java.util Date)))


(deftest test-instantiation-of-keys
  (is (not (= (triggers/key) (triggers/key))))
  (is (not (= (triggers/key "key1") (triggers/key))))
  (is (not (= (triggers/key "key1") (triggers/key "key2"))))
  (is (not (= (triggers/key "key1" "group1") (triggers/key "key1" "group2"))))
  (is (= (triggers/key "key1" "group1") (triggers/key "key1" "group1")))
  (is (= (triggers/key "key1") (triggers/key "key1"))))


(deftest test-trigger-builder-dsl-example1
  (let [trigger (triggers/build (triggers/with-identity    "basic.trigger1" "basic.group1")
                                (triggers/with-description "A description"))]
    (is (= (triggers/key "basic.trigger1" "basic.group1") (.getKey trigger)))
    (is (= "A description" (.getDescription trigger)))))

(deftest test-trigger-builder-dsl-example2
  (let [trigger (triggers/build (triggers/with-identity    "basic.trigger2")
                                (triggers/with-description "A description")
                                (triggers/with-priority    3))]
    (is (= 3 (.getPriority trigger)))))


(deftest test-trigger-builder-dsl-example3
  (let [trigger (triggers/build (triggers/with-identity "basic.trigger3")
                                (triggers/modified-by-calendar "my.holidays.calendar"))]
    (is (= "my.holidays.calendar" (.getCalendarName trigger)))))


(deftest test-trigger-builder-dsl-example4
  (let [d       (Date.)
        trigger (triggers/build (triggers/with-identity "basic.trigger4")
                                (triggers/start-now))
        st      (.getStartTime trigger)]
    (is (= (.getYear d)    (.getYear st)))
    (is (= (.getMonth d)   (.getMonth st)))
    (is (= (.getDay d)     (.getDay st)))
    (is (= (.getHours d)   (.getHours st)))
    (is (= (.getMinutes d) (.getMinutes st)))))


(deftest test-trigger-builder-dsl-example5
  (let [start   (Date/from (Instant/now))
        end     (Date/from (.plus (Instant/now) 3 ChronoUnit/HOURS))
        trigger (triggers/build (triggers/with-identity "basic.trigger5")
                                (triggers/start-at start)
                                (triggers/end-at   end))]
    (is (= start (.getStartTime trigger)))
    (is (= end   (.getEndTime trigger)))))


(deftest test-trigger-builder-dsl-example6
  (let [trigger (triggers/build (triggers/with-identity "basic.trigger6")
                                (triggers/start-now)
                                (triggers/for-job "some.job"))]
    (is (= (jobs/key "some.job") (.getJobKey trigger)))))


(deftest test-trigger-builder-dsl-example7
  (let [trigger (triggers/build (triggers/with-identity "basic.trigger7")
                                (triggers/start-now)
                                (triggers/for-job (jobs/key "some.job")))]
    (is (= (jobs/key "some.job") (.getJobKey trigger)))))


(deftest test-trigger-builder-dsl-example8
  (let [trigger (triggers/build (triggers/with-identity "basic.trigger8")
                                (triggers/start-now)
                                (triggers/for-job "collect.underpants" "business"))]
    (is (= (jobs/key "collect.underpants" "business") (.getJobKey trigger)))))

(deftest test-trigger-builder-dsl-example9
  (let [trigger (triggers/build (triggers/with-identity "basic.trigger8")
                                (triggers/start-now)
                                (triggers/for-job "collect.underpants" "business")
                                (triggers/using-job-data { :who "Gnomes" :what "Know about business" }))]
    (is (= (to-job-data { "who" "Gnomes" "what" "Know about business" }) (.getJobDataMap trigger)))
    (is (= { "who" "Gnomes" "what" "Know about business" } (from-job-data (.getJobDataMap trigger))))))

(deftest test-job-builder-dsl-example10
  (let [tk  (triggers/key "basic.trigger10" "basic.group10")
        trigger (triggers/build (triggers/with-identity tk))]
    (is (= tk (.getKey trigger)))))

(deftest test-job-builder-dsl-example11
  (let [tk  (triggers/key "basic.trigger11")
        trigger (triggers/build (triggers/with-identity tk))]
    (is (= tk (.getKey trigger)))))