;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.quartzite.jobs
  (:refer-clojure :exclude [key])
  (:import (org.quartz Job JobDetail JobBuilder JobKey)
           (org.quartz.utils Key))
  (:require    [clojurewerkz.quartzite.conversion :refer [to-job-data]]))


;;
;; Implementation
;;

;; ...



;;
;; API
;;

(defn key
  (^JobKey []
     (JobKey. (Key/createUniqueName nil)))
  (^JobKey [named]
     (JobKey. (name named)))
  (^JobKey [named, group]
     (JobKey. (name named) (name group))))



(defn with-identity
  (^JobBuilder [^JobBuilder jb s]
     (if (instance? JobKey s)
       (.withIdentity jb ^JobKey s)
       (.withIdentity jb (key s))))
  (^JobBuilder [^JobBuilder jb s group]
     (.withIdentity jb (key s group))))

(defn with-description
  ^JobBuilder [^JobBuilder jb ^String s]
  (.withDescription jb s))

(defn store-durably
  ^JobBuilder [^JobBuilder jb]
  (.storeDurably jb))

(defn request-recovery
  ^JobBuilder [^JobBuilder jb]
  (.requestRecovery jb))

(defn of-type
  ^JobBuilder [^JobBuilder jb clazz]
  (.ofType jb clazz))

(defn using-job-data
  ^JobBuilder [^JobBuilder tb m]
  (.usingJobData tb (to-job-data m)))

(defn finalize
  ^JobDetail [^JobBuilder jb]
  (.build jb))


(defmacro build
  [& body]
  `(let [jb# (JobBuilder/newJob)]
     (finalize (-> jb# ~@body))))

;; This macro is necessary because clojure.core/proxy and clojure.core/reify
;; do not work for this specific use case with Quartz. See https://groups.google.com/forum/#!topic/clojure/WIIcvsYLzh0
;; for the discussion. MK.
(defmacro defjob
  [jtype args & body]
  `(defrecord ~jtype []
       Job
     (execute [this ~@args]
       ~@body)))
