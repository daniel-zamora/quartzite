;; Copyright (c) 2011-2014 Michael S. Klishin, Alex Petrov, and the ClojureWerkz Team
;;
;; The use and distribution terms for this software are covered by the
;; Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;; which can be found in the file epl-v10.html at the root of this distribution.
;; By using this software in any fashion, you are agreeing to be bound by
;; the terms of this license.
;; You must not remove this notice, or any other, from this software.

(ns clojurewerkz.quartzite.matchers
  "Contains factory functions that produce group matchers.
   Group matchers are used to retrieve triggers and jobs from the scheduler en masse."
  (:import (org.quartz.impl.matchers GroupMatcher)
           (org.quartz.utils Key)))


;;
;; API
;;

(defn match?
  "Returns true if given group matcher matches the given key"
  [^GroupMatcher matcher ^Key key]
  (.isMatch matcher key))

(defn group-equals
  "Returns a group matcher that matches keys in the given group"
  ^GroupMatcher [^String s]
  (GroupMatcher/groupEquals s))

(defn group-starts-with
  "Returns a group matcher that matches keys in all groups that start with the given prefix"
  ^GroupMatcher [^String s]
  (GroupMatcher/groupStartsWith s))

(defn group-ends-with
  "Returns a group matcher that matches keys in all groups that end with the given suffix"
  ^GroupMatcher [^String s]
  (GroupMatcher/groupEndsWith s))

(defn group-contains
  "Returns a group matcher that matches keys in all groups that contain the given substring"
  ^GroupMatcher [^String s]
  (GroupMatcher/groupContains s))
