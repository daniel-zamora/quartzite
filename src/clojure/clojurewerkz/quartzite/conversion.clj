;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.quartzite.conversion
  (:refer-clojure :exclude [key])
  (:import (clojure.lang IPersistentMap)
           (java.time Instant LocalDate LocalDateTime OffsetDateTime ZoneId ZonedDateTime)
           (java.util Calendar Date)
           (org.quartz JobDataMap JobExecutionContext JobDetail Trigger TriggerKey JobKey)
           (org.quartz.utils Key)))

;;
;; Implementation
;;

(defn- convert-keys-to-strings
  "Converts keys of a map to strings. Doesn't modify nested maps"
  [map]
  (->> (for [[k v] map]
         (if (keyword? k)
           [(name k) v]
           [(str k) v]))
       (into {})))

;;
;; API
;;

;; Monger and other ClojureWerkz project integration extension point. MK.
(defprotocol JobDataMapConversion
  (^JobDataMap
    to-job-data   [input] "Instantiates a JobDataMap instance from a Clojure map")
  (from-job-data [input] "Converts a JobDataMap to a Clojure map"))

(extend-protocol JobDataMapConversion
  IPersistentMap
  (to-job-data [^IPersistentMap input]
    (JobDataMap. (convert-keys-to-strings input)))


  JobDataMap
  (from-job-data [^JobDataMap input]
    (into {} (convert-keys-to-strings input)))

  JobExecutionContext
  (from-job-data [^JobExecutionContext input]
    (from-job-data (.getMergedJobDataMap input))))


(defn from-key
  "Converts a Key instance (TriggerKey, JobKey) to a Clojure map"
  [^Key key]
  {:name (.getName key)
   :group (.getGroup key)})

(defn from-job-detail
  [^JobDetail jd]
  {:key (from-key (.getKey jd))
   :description (.getDescription jd)
   :job-data (from-job-data (.getJobDataMap jd))})

(defn from-trigger
  [^Trigger t]
  {:key (from-key (.getKey t))
   :description (.getDescription t)
   :calendar-name (.getCalendarName t)
   :start-time (.getStartTime t)
   :end-time (.getEndTime t)
   :next-fire-time (.getNextFireTime t)
   :previous-fire-time (.getPreviousFireTime t)})



(defprotocol DateConversion
  (to-date [input] "Converts given input to java.util.Date"))

(extend-protocol DateConversion
  Date
  (to-date [input]
    input)

  Calendar
  (to-date [^Calendar input]
    (.getTime input))

  Instant
  (to-date [^Instant input]
    (Date/from input))

  OffsetDateTime
  (to-date [^OffsetDateTime input]
    (Date/from (.toInstant input)))

  ZonedDateTime
  (to-date [^ZonedDateTime input]
    (Date/from (.toInstant input)))

  LocalDateTime
  (to-date [^LocalDateTime input]
    (Date/from (.toInstant ^ZonedDateTime (.atZone input ZoneId/systemDefault))))

  LocalDate
  (to-date [^LocalDate input]
    (Date/from (.toInstant ^ZonedDateTime (.atStartOfDay input ZoneId/systemDefault)))))

;; Dynamically load Joda time support if possible
(defn class-exists? [sym]
  (try
    (boolean (resolve sym))
    (catch Throwable _ false)))

(when (every? class-exists? ['org.joda.time.DateTime
                             'org.joda.time.MutableDateTime
                             'org.joda.time.base.BaseDateTime])
  (eval
   `(extend-protocol DateConversion
      org.joda.time.DateTime
      (to-date [^org.joda.time.DateTime input#]
        (.toDate input#))
      org.joda.time.MutableDateTime
      (to-date [^org.joda.time.MutableDateTime input#]
        (.toDate input#))
      org.joda.time.base.BaseDateTime
      (to-date [^org.joda.time.base.BaseDateTime input#]
        (.toDate input#)))))

(defprotocol KeyCoercion
  (^TriggerKey
    to-trigger-key [input] "Converts a key to a TriggerKey instance")
  (^JobKey
    to-job-key [input] "Converts a key to a JobKey instance"))

(extend-protocol KeyCoercion
  TriggerKey
  (to-trigger-key [input]
    input)

  JobKey
  (to-job-key [input]
    input)

  String
  (to-trigger-key [input]
    (TriggerKey. input))
  (to-job-key [input]
    (JobKey. input)))
