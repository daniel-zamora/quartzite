;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.quartzite.triggers
  (:refer-clojure :exclude [key])
  (:require    [clojurewerkz.quartzite.conversion :refer [to-job-data to-date]])
  (:import (org.quartz JobDetail JobKey Trigger TriggerBuilder TriggerKey ScheduleBuilder)
           (org.quartz.utils Key)))

;;
;; Implementation
;;

;; ...



;;
;; API
;;

(defn key
  (^TriggerKey []
     (TriggerKey. (Key/createUniqueName nil)))
  (^TriggerKey [named]
     (TriggerKey. (name named)))
  (^TriggerKey [named, group]
     (TriggerKey. (name named) (name group))))



(defn with-identity
  (^TriggerBuilder [^TriggerBuilder tb s]
     (if (instance? TriggerKey s)
       (.withIdentity tb ^TriggerKey s)
       (.withIdentity tb (key s))))
  (^TriggerBuilder [^TriggerBuilder tb s group]
     (.withIdentity tb (key s group))))

(defn with-description
  ^TriggerBuilder [^TriggerBuilder tb ^String s]
  (.withDescription tb s))


(defn with-priority
  ^TriggerBuilder [^TriggerBuilder tb ^long l]
  (.withPriority tb l))

(defn modified-by-calendar
  ^TriggerBuilder [^TriggerBuilder tb ^String s]
  (.modifiedByCalendar tb s))

(defn with-schedule
  ^TriggerBuilder [^TriggerBuilder tb ^ScheduleBuilder sb]
  (.withSchedule tb sb))

(defn start-now
  ^TriggerBuilder [^TriggerBuilder tb]
  (.startNow tb))


;; Seamless JodaTime integration is one
;; of the goals of Quartzite.
(defn start-at
  ^TriggerBuilder [^TriggerBuilder tb date]
  (.startAt tb (to-date date)))

(defn end-at
  ^TriggerBuilder [^TriggerBuilder tb date]
  (.endAt tb (to-date date)))



(defn for-job
  (^TriggerBuilder [^TriggerBuilder tb job]
   (cond (string? job) (.forJob tb ^String job)
         (instance? JobKey job) (.forJob tb ^JobKey job)
         :else (.forJob tb ^JobDetail job)))
  (^TriggerBuilder [^TriggerBuilder tb ^String job ^String group]
     (.forJob tb job group)))


(defn using-job-data
  ^TriggerBuilder [^TriggerBuilder tb m]
  (.usingJobData tb (to-job-data m)))



(defn finalize
  ^Trigger [^TriggerBuilder tb]
  (.build tb))


(defmacro build
  [& body]
  `(let [tb# (TriggerBuilder/newTrigger)]
     (finalize (-> tb# ~@body))))
