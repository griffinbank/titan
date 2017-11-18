(ns titan.http.response
  (:require [titan.util :refer [redef]]
            [ring.util.http-response]))

;; HTTP Standard Responses
;; http://en.wikipedia.org/wiki/List_of_HTTP_status_codes
(redef ring.util.http-response [
  ;; 100s
  continue
  switching-protocols
  processing

  ;; 200s
  ok
  created
  accepted
  non-authoritative-information
  no-content
  reset-content
  partial-content
  multi-status
  already-reported
  im-used

  ;; 300s
  multiple-choices
  moved-permanently
  found
  see-other
  not-modified
  use-proxy
  temporary-redirect
  permanent-redirect

  ;; 400s
  bad-request bad-request!
  unauthorized unauthorized!
  payment-required payment-required!
  forbidden forbidden!
  not-found not-found!
  method-not-allowed method-not-allowed!
  not-acceptable not-acceptable!
  proxy-authentication-required proxy-authentication-required!
  request-timeout request-timeout!
  conflict conflict!
  gone gone!
  length-required length-required!
  precondition-failed precondition-failed!
  request-entity-too-large request-entity-too-large!
  request-uri-too-long request-uri-too-long!
  unsupported-media-type unsupported-media-type!
  requested-range-not-satisfiable requested-range-not-satisfiable!
  expectation-failed expectation-failed!
  enhance-your-calm enhance-your-calm!
  unprocessable-entity unprocessable-entity!
  locked locked!
  failed-dependency failed-dependency!
  unordered-collection unordered-collection!
  upgrade-required upgrade-required!
  precondition-required precondition-required!
  too-many-requests too-many-requests!
  request-header-fields-too-large request-header-fields-too-large!
  retry-with retry-with!
  unavailable-for-legal-reasons unavailable-for-legal-reasons!

  ;; 500s
  internal-server-error internal-server-error!
  not-implemented not-implemented!
  bad-gateway bad-gateway!
  service-unavailable service-unavailable!
  gateway-timeout gateway-timeout!
  http-version-not-supported http-version-not-supported!
  variant-also-negotiates variant-also-negotiates!
  insufficient-storage insufficient-storage!
  loop-detected loop-detected!
  bandwidth-limit-exceeded bandwidth-limit-exceeded!
  not-extended not-extended!
  network-authentication-required network-authentication-required!
  network-read-timeout network-read-timeout!
  network-connect-timeout network-connect-timeout!
])
